package org.lexxv.externaloauthservicespring.graphql

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import graphql.kickstart.tools.*
import graphql.scalars.ExtendedScalars
import graphql.schema.GraphQLScalarType
import graphql.validation.rules.OnValidationErrorStrategy
import graphql.validation.rules.ValidationRules
import graphql.validation.schemawiring.ValidationSchemaWiring
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.lexxv.externaloauthservicespring.graphql.inputs.AutocompleteFilterInput
import org.lexxv.externaloauthservicespring.graphql.inputs.DateTimeRangeInput
import org.lexxv.externaloauthservicespring.graphql.inputs.SimpleFilterInput
import org.lexxv.externaloauthservicespring.graphql.problems.Problem
import org.lexxv.externaloauthservicespring.graphql.queries.externaloauth.attributes.ExternalOauthListAttributes
import org.lexxv.externaloauthservicespring.graphql.queries.externaloauth.inputs.ExternalOauthEntityFilterInput
import org.lexxv.externaloauthservicespring.graphql.queries.externaloauth.inputs.ExternalOauthFilterInput
import org.lexxv.externaloauthservicespring.graphql.queries.externaloauth.inputs.ExternalOauthListInput
import org.lexxv.externaloauthservicespring.graphql.scalars.DateTimeScalar
import org.lexxv.externaloauthservicespring.sort.ExternalOauthSortEnum
import org.springframework.beans.factory.BeanFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.*

/**
 * Конфигурация объекта обработки схемы GraphQL.
 *
 * @property beanFactory Фабрика бинов
 *
 * @author Alexey Vorobyev <mister.alex49@yandex.ru>
 */
@Configuration
@Suppress("TooManyFunctions")
class GraphQLConfig(private val beanFactory: BeanFactory) {
    /**
     * Дополнение параметров обработчика схемы.
     *
     * Можно добавить свои опции, скалярные типы.
     *
     * @return Объект обработки схемы GraphQL
     */
    @ExperimentalCoroutinesApi
    @Bean
    fun schemaParser(): SchemaParser =
        SchemaParser.newParser()
            .schemaString(javaClass.getResource("/schema.graphqls")!!.readText())
            .resolvers(getResolvers())
            .scalars(getScalars())
            .dictionary(getDictionaries())
            .options(getOptions())
            .directiveWiring(
                ValidationSchemaWiring(
                    ValidationRules.newValidationRules()
                        .onValidationErrorStrategy(OnValidationErrorStrategy.RETURN_NULL)
                        .locale(Locale.forLanguageTag("ru-RU"))
                        .build()
                )
            )
            .build()

    /**
     * Возвращает обработчики объектов.
     *
     * @return Список обработчиков
     */
    private fun getResolvers(): List<GraphQLResolver<*>> =
        listOf(
            getMainResolvers(),
        ).flatten()

    /**
     * Возвращает основные обработчики.
     *
     * @return Список обработчиков
     */
    private fun getMainResolvers(): List<GraphQLResolver<*>> =
        getResolversBeans(setOf("query", "mutation"), "")


    /**
     * Возвращает список бинов обработчиков.
     *
     * @param names Список имён обработчиков
     *
     * @return Список бинов
     */
    private fun getResolversBeans(names: Set<String>, suffix: String = "Resolver"): List<GraphQLResolver<*>> =
        names.map { beanFactory.getBean("${it}$suffix", GraphQLResolver::class.java) }

    /**
     * Возвращает скалярные типы.
     *
     * @return Список скалярных типов
     */
    private fun getScalars(): List<GraphQLScalarType> =
        listOf(
            ExtendedScalars.NonNegativeFloat,
            ExtendedScalars.NonNegativeInt,
            ExtendedScalars.PositiveInt,
            ExtendedScalars.UUID,
            DateTimeScalar().get(),
        )

    /**
     * Возвращает словарные типы.
     *
     * Типы, которые библиотека не может вычислить самостоятельно. Например, интерфейсы ошибок, или входные типы.
     *
     * @return Список типов
     */
    private fun getDictionaries(): List<Class<*>> =
        listOf(getTypes(), getProblems(), getInputs(), getFilters()).flatten()

    /**
     * Возвращает неопределённые типов объектов.
     *
     * @return Список типов
     */
    private fun getTypes(): List<Class<*>> =
        listOf(
            ExternalOauthListAttributes::class.java,
            ExternalOauthSortEnum::class.java
        )

    /**
     * Возвращает типы ошибок.
     *
     * @return Список типов ошибок
     */
    private fun getProblems(): List<Class<*>> =
        listOf(
            Problem::class.java
        )

    /**
     * Возвращает типы входных параметров.
     *
     * @return Список типов
     */
    private fun getInputs(): List<Class<*>> =
        listOf(
            DateTimeRangeInput::class.java,
            ExternalOauthListInput::class.java
        )

    /**
     * Возвращает типы фильтров.
     *
     * @return Список типов фильтров
     */
    private fun getFilters(): List<Class<*>> =
        listOf(
            ExternalOauthEntityFilterInput::class.java,
            ExternalOauthFilterInput::class.java,
            SimpleFilterInput::class.java,
            AutocompleteFilterInput::class.java,
        )

    /**
     * Возвращает опции разборщика запроса.
     *
     * @return Объект разборщика
     */
    @ExperimentalCoroutinesApi
    private fun getOptions(): SchemaParserOptions =
        SchemaParserOptions.newOptions()
            .preferGraphQLResolver(true)
            .objectMapperConfigurer(
                ObjectMapperConfigurer { mapper: ObjectMapper, _: ObjectMapperConfigurerContext? ->
                    mapper.registerModule(JavaTimeModule()).registerKotlinModule()
                }
            )
            .build()
}
