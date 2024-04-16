package org.lexxv.externaloauthservicespring.filters

import com.fasterxml.jackson.databind.JsonNode
import jakarta.persistence.criteria.CriteriaBuilder
import jakarta.persistence.criteria.CriteriaQuery
import jakarta.persistence.criteria.Path
import jakarta.persistence.criteria.Predicate
import jakarta.persistence.criteria.Root
import org.lexxv.externaloauthservicespring.entities.EntityInterface
import org.lexxv.externaloauthservicespring.graphql.inputs.DateTimeRangeInput
import org.lexxv.externaloauthservicespring.repositories.functions.ILikeFunction
import org.lexxv.lib.jsonmapper.JsonMapper
import org.springframework.data.jpa.domain.Specification
import java.time.OffsetDateTime
import java.util.UUID

/**
 * Базовый класс формирования спецификаций фильтра сущностей.
 *
 * @author Alexey Vorobyev <mister.alex49@yandex.ru>
 */
@Suppress("TooManyFunctions", "SpreadOperator")
abstract class BaseSpecifications<Entity : EntityInterface> : SpecificationsInterface<Entity> {
    /**
     * Условия объединения спецификаций по условию `И` или `ИЛИ`
     */
    companion object {
        /**
         * Преобразование текстового значения в UUID.
         *
         * @return UUID идентификатор
         */
        fun JsonNode.asUUID(): UUID = JsonMapper.asUUID(this)

        /**
         * Приведение значений идентификаторов поиска к валидному виду.
         *
         * Атрибуты-идентификаторы некоторых сущностей могут хранить как идентификатор, так и `NULL`. Поиск по таким
         * атрибутам должен быть по условию `OR`. При этом на вход приходит общий список. Например:
         * ```text
         * ["20bc581e-de9b-433a-8768-519c02fbbce9", ""]
         * ```
         * Метод отсекает пустые значения и возвращает список только идентификаторов.
         *
         * @param value Значение поиска
         *
         * @return Нормализованные значения
         */
        fun normalizeUUIDValue(value: Iterable<JsonNode>?): List<UUID>? {
            val list = value?.filterNot { it.asText(null).isNullOrEmpty() || it.asText() == "null" }
            return if (!list.isNullOrEmpty()) list.map { it.asUUID() } else null
        }
    }

    override fun createSpecifications(filter: JsonNode?): Specification<Entity?>? =
        listOf(
            simpleFilter(Filter.getSimpleFilter(filter)),
            autocompleteFilter(Filter.getAutocompleteFilter(filter)),
            entityFilter(Filter.getEntityFilter(filter)),
        ).joinSpecsAnd()

    /**
     * Возвращает спецификацию для простого фильтра.
     *
     * @param filter Параметры фильтра
     *
     * @return Объект спецификации
     */
    abstract fun simpleFilter(filter: JsonNode? = null): Specification<Entity?>?

    /**
     * Возвращает спецификацию для фильтра для выпадающих список с автодополнением.
     *
     * @param filter Параметры фильтра
     *
     * @return Объект спецификации
     */
    abstract fun autocompleteFilter(filter: JsonNode? = null): Specification<Entity?>?

    /**
     * Возвращает спецификацию для полного фильтра.
     *
     * @param filter Параметры фильтра
     *
     * @return Объект спецификации
     */
    abstract fun entityFilter(filter: JsonNode? = null): Specification<Entity?>?

    /**
     * Соединяет спецификации по условию `И`.
     *
     * @return Общая спецификация
     */
    protected fun List<Specification<Entity?>?>.joinSpecsAnd(): Specification<Entity?>? =
        fold(null as Specification<Entity?>?) { acc, specification ->
            acc?.let { specification?.let { acc.and(it) } ?: acc } ?: specification
        }

    /**
     * Соединяет спецификации по условию `ИЛИ`.
     *
     * @return Общая спецификация
     */
    protected fun List<Specification<Entity?>?>.joinSpecsOr(): Specification<Entity?>? =
        fold(null as Specification<Entity?>?) { acc, specification ->
            acc?.let { specification?.let { acc.or(it) } ?: acc } ?: specification
        }

    /**
     * Возвращает значение параметра простого фильтра.
     *
     * @param filter Параметры простого фильтра
     *
     * @return Объект спецификации
     */
    protected fun getSimpleFilterQuery(filter: JsonNode? = null): String? {
        val queryString = Filter.getValueText(filter, "query")
        return if (queryString?.isBlank() == true) null else queryString
    }

    /**
     * Возвращает спецификацию поиска по идентификаторам.
     *
     * @param filter Параметры фильтра
     *
     * @return Объект спецификации
     */
    protected fun inId(filter: JsonNode?): Specification<Entity?>? =
        normalizeUUIDValue(Filter.getValueIterable(filter, "id"))?.let {
            Specification<Entity?> { model: Root<Entity?>, _: CriteriaQuery<*>?, _: CriteriaBuilder ->
                model.get<String>("id").`in`(it)
            }
        }

    /**
     * Возвращает спецификацию поиска в атрибуте.
     *
     * @param field Имя атрибута сущности
     * @param value Значение поиска
     *
     * @return Объект спецификации
     */
    protected fun hasInField(field: String, value: String?): Specification<Entity?>? {
        if (value == null) return null
        return Specification<Entity?> { model: Root<Entity?>, _: CriteriaQuery<*>?, builder: CriteriaBuilder ->
            builder.isTrue(
                builder.function(
                    ILikeFunction.name,
                    Boolean::class.java,
                    model.get<String>(field),
                    builder.literal("%$value%")
                )
            )
        }
    }

    /**
     * Возвращает спецификацию поиска в атрибуте по точному совпадению логического типа.
     *
     * @param field Имя атрибута сущности
     * @param value Значение поиска
     *
     * @return Объект спецификации
     */
    protected fun isExactField(field: String, value: Boolean?): Specification<Entity?>? =
        value?.let {
            Specification<Entity?> { model: Root<Entity?>, _: CriteriaQuery<*>?, builder: CriteriaBuilder ->
                builder.equal(model.get<String>(field), it)
            }
        }

    /**
     * Возвращает спецификацию поиска по связанной сущности (по идентификаторам).
     *
     * @param filter Параметры фильтра
     * @param field Поле фильтрации
     *
     * @return Объект спецификации
     */
    protected fun inLinkedEntity(filter: JsonNode?, field: String): Specification<Entity?>? =
        containsInField("${field}Id", normalizeUUIDValue(Filter.getValueIterable(filter, "${field}Id")))

    /**
     *
     * Возвращает спецификацию поиска по списку строковых значений.
     *
     * @param filter Параметры фильтра
     * @param field Поле фильтрации
     *
     * @return Объект спецификации
     */
    protected fun inStringField(filter: JsonNode?, field: String): Specification<Entity?>? =
        containsInField(field, Filter.getValueListOfString(filter, field))

    /**
     *
     * Возвращает спецификацию поиска по списку значений перечисляемого типа.
     *
     * @param filter Параметры фильтра
     * @param field Поле фильтрации
     *
     * @return Объект спецификации
     */
    protected inline fun <reified EnumType : Enum<EnumType>> inEnumField(
        filter: JsonNode?,
        field: String
    ): Specification<Entity?>? =
        containsInField(
            field,
            Filter.getValueListOfEnum<EnumType>(filter, field)
        )

    /**
     * Возвращает спецификацию поиска в атрибуте по вхождению в список моделей.
     *
     * @param field Имя атрибута сущности
     * @param value Значение поиска
     *
     * @return Объект спецификации
     */
    protected fun containsInField(field: String, value: List<Any>?): Specification<Entity?>? =
        value?.let {
            Specification<Entity?> { model: Root<Entity?>, _: CriteriaQuery<*>?, _: CriteriaBuilder ->
                model.get<String>(field).`in`(it)
            }
        }


    /**
     * Возвращает спецификацию поиска по дате.
     *
     * @param field Поле даты
     * @param value Значение поиска
     *
     * @return Объект спецификации
     */
    private fun inDateRange(field: String, value: JsonNode?): Specification<Entity?>? =
        value?.let {
            Specification<Entity?> { model: Root<Entity?>, _: CriteriaQuery<*>?, builder: CriteriaBuilder ->
                inDateRangePredicate(model.get(field), builder, it)
            }
        }

    /**
     * Возвращает предикат для поиска в атрибуте даты.
     *
     * Переданные даты при поиске учитываются включительно.
     *
     * @param field Имя атрибута сущности
     * @param builder Построитель условий запроса
     * @param value Значение поиска
     *
     * @return Объект спецификации
     */
    private fun inDateRangePredicate(
        field: Path<OffsetDateTime>,
        builder: CriteriaBuilder,
        value: JsonNode?
    ): Predicate? {
        if (value == null || (!value.hasNonNull("start") && !value.hasNonNull("end"))) {
            return null
        }

        val date = JsonMapper.asObject<DateTimeRangeInput>(value)
        return if (date.start != null && date.end != null) {
            builder.between(field, date.startDate, date.endDate)
        } else if (date.start != null) {
            builder.greaterThanOrEqualTo(field, date.startDate)
        } else {
            builder.lessThanOrEqualTo(field, date.endDate)
        }
    }

    /**
     * Возвращает спецификацию поиска по дате создания.
     *
     * @param filter Параметры фильтра
     *
     * @return Объект спецификации
     */
    protected fun isCreatedAt(filter: JsonNode?): Specification<Entity?>? =
        inDateRange("createdAt", Filter.getValueObject(filter, "createdAt"))

    /**
     * Возвращает спецификацию поиска по дате изменения.
     *
     * @param filter Параметры фильтра
     *
     * @return Объект спецификации
     */
    protected fun isUpdatedAt(filter: JsonNode?): Specification<Entity?>? =
        inDateRange("updatedAt", Filter.getValueObject(filter, "updatedAt"))
}
