package org.lexxv.externaloauthservicespring.graphql.scalars

import graphql.GraphQLContext
import graphql.execution.CoercedVariables
import graphql.language.StringValue
import graphql.language.Value
import graphql.scalars.util.Kit
import graphql.schema.Coercing
import graphql.schema.CoercingParseLiteralException
import graphql.schema.CoercingParseValueException
import graphql.schema.CoercingSerializeException
import graphql.schema.GraphQLScalarType
import java.time.DateTimeException
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.Date
import java.util.Locale
import java.util.function.Function

/**
 * Объект скалярного GraphQL типа `Дата и время`.
 *
 */
class DateTimeScalar {
    /**
     * Имя типа.
     */
    private val name = "DateTime"

    /**
     * Описание типа.
     */
    private val description = "Дата и время"

    /**
     * Объект форматирования даты.
     */
    private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")

    /**
     * Объект преобразования значения из схемы и обратно.
     */
    private val coercing: Coercing<String, String> = object : Coercing<String, String> {
        /**
         * Функция преобразования значения из источника (например, БД) для клиента.
         *
         * @return Значение как оно будет в ответе клиенту
         *
         * @throws CoercingSerializeException
         */
        @Suppress("SwallowedException")
        override fun serialize(input: Any, graphQlContext: GraphQLContext, locale: Locale): String? {
            val offsetDateTime =
                when (input) {
                    is OffsetDateTime -> input
                    is ZonedDateTime -> input.toOffsetDateTime()
                    is Date -> input.toInstant().atOffset(ZoneOffset.UTC)
                    is String -> {
                        parseOffsetDateTime(input.toString()) { message: String? ->
                            CoercingSerializeException(message)
                        }
                    }

                    else -> throw CoercingSerializeException(
                        "Expected something we can convert to 'java.time.OffsetDateTime' but was '" +
                                Kit.typeName(input) + "'."
                    )
                }
            return try {
                formatter.format(offsetDateTime.toInstant().atOffset(ZoneOffset.UTC))
            } catch (e: DateTimeException) {
                throw CoercingSerializeException(
                    "Unable to turn TemporalAccessor into OffsetDateTime because of : '" + e.message + "'."
                )
            }
        }

        /**
         * Функция преобразования значения из переменных запроса клиента.
         *
         * Во внутреннем формате переменные представляют собой JSON.
         *
         * @return Значение из схемы для дальнейшей обработки приложением
         *
         * @throws CoercingParseValueException
         */
        override fun parseValue(input: Any, graphQlContext: GraphQLContext, locale: Locale): String =
            when (input) {
                is OffsetDateTime -> formatter.format(input.toInstant().atOffset(ZoneOffset.UTC))
                is ZonedDateTime -> formatter.format(input.toOffsetDateTime().toInstant().atOffset(ZoneOffset.UTC))
                is String -> {
                    parseOffsetDateTime(input.toString()) { message: String? -> CoercingParseValueException(message) }
                    input
                }

                else -> throw CoercingParseValueException(
                    "Expected a 'String' but was '" + Kit.typeName(input) + "'."
                )
            }

        /**
         * Функция преобразования значения из текста (inline) запроса клиента.
         *
         * Во внутреннем формате переменные представляют собой AST.
         *
         * @return Значение из схемы для дальнейшей обработки приложением
         *
         * @throws CoercingParseLiteralException
         */
        override fun parseLiteral(
            input: Value<*>,
            variables: CoercedVariables,
            graphQlContext: GraphQLContext,
            locale: Locale
        ): String? {
            if (input !is StringValue) {
                throw CoercingParseLiteralException(
                    "Expected AST type 'StringValue' but was '" + Kit.typeName(input) + "'."
                )
            }
            val date = input.value
            parseOffsetDateTime(date) { message: String? -> CoercingParseLiteralException(message) }
            return date
        }

        /**
         * Форматирование даты.
         *
         * @param date Дата
         * @param exception Исключение
         *
         * @return Преобразованная дата
         */
        private fun parseOffsetDateTime(date: String, exception: Function<String, RuntimeException>): OffsetDateTime {
            return try {
                LocalDateTime.parse(date, formatter).atZone(ZoneOffset.UTC).toOffsetDateTime()
            } catch (e: DateTimeParseException) {
                throw exception.apply("Invalid RFC3339 value : '" + date + "'. because of : '" + e.message + "'")
            }
        }
    }

    /**
     * @return Возвращает объект скалярного типа для обработчика схемы.
     */
    fun get(): GraphQLScalarType =
        GraphQLScalarType.newScalar().name(name).description(description).coercing(coercing).build()
}
