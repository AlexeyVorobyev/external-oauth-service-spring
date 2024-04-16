package org.lexxv.externaloauthservicespring.filters

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.NullNode
import com.fasterxml.jackson.databind.node.ObjectNode
import org.lexxv.lib.jsonmapper.JsonMapper

/**
 * Класс работы с фильтром входных данных.
 *
 * @author Alexey Vorobyev <mister.alex49@yandex.ru>
 */
object Filter {
    /**
     * Ключ объединения фильтров по условию `И` или `ИЛИ`.
     */
    private const val FILTER_JOIN = "filterJoin"

    /**
     * Ключ фильтра простого поиска.
     */
    private const val SIMPLE_KEY = "simpleFilter"

    /**
     * Ключ фильтра простого поиска с автодополнением
     */
    private const val AUTOCOMPLETE_KEY = "autocompleteFilter"

    /**
     * Ключ фильтра полного поиска.
     */
    private const val ENTITY_KEY = "entityFilter"

    /**
     * Ключ фильтра связанной сущности поиска.
     */
    private const val TAG_KEY = "tagFilter"

    /**
     * Создание фильтра.
     *
     * @param simpleVars Параметры простого поиска
     * @param entityVars Параметры полного поиска
     *
     * @return Объект фильтра
     */
    fun createFilter(simpleVars: Map<String, Any>? = null, entityVars: Map<String, Any>? = null): ObjectNode {
        val filter = JsonMapper.empty()
        if (simpleVars != null) {
            filter.set<JsonNode>(SIMPLE_KEY, JsonMapper.asJson(simpleVars))
        }
        if (entityVars != null) {
            filter.set<JsonNode>(ENTITY_KEY, JsonMapper.asJson(entityVars))
        }
        return filter
    }

    /**
     * Создание фильтра если он пустой.
     *
     * @param filter Параметры фильтра
     *
     * @return Объект фильтра
     */
    fun createFilterIfNotExist(filter: JsonNode?): ObjectNode =
        if (filter == null || filter is NullNode) JsonMapper.empty() else filter as ObjectNode

    /**
     * Возвращает параметры простого поиска.
     *
     * @param filter Параметры фильтра
     *
     * @return Параметры
     */
    fun getSimpleFilter(filter: JsonNode?): ObjectNode? = getFilter(filter, SIMPLE_KEY)

    /**
     * Возвращает параметры простого поиска для выпадающих списков с автодополнением.
     *
     * @param filter Параметры фильтра
     *
     * @return Параметры
     */
    fun getAutocompleteFilter(filter: JsonNode?): ObjectNode? = getFilter(filter, AUTOCOMPLETE_KEY)

    /**
     * Возвращает параметры полного поиска.
     *
     * @param filter Параметры фильтра
     *
     * @return Параметры
     */
    fun getEntityFilter(filter: JsonNode?): ObjectNode? = getFilter(filter, ENTITY_KEY)

    /**
     * Возвращает параметры дополнительного поиска по тегам.
     *
     * @param filter Параметры фильтра
     *
     * @return Параметры
     */
    fun getTagFilter(filter: JsonNode?): ObjectNode? = getFilter(filter, TAG_KEY)

    /**
     * Возвращает параметры основного ключа фильтра.
     *
     * @param filter Параметры фильтра
     *
     * @return Параметры
     */
    private fun getFilter(filter: JsonNode?, key: String): ObjectNode? =
        if (filter != null && filter.hasNonNull(key)) filter[key] as ObjectNode else null

    /**
     * Создание ключа фильтра простого поиска при необходимости.
     *
     * @param filter Параметры фильтра
     */
    private fun createSimpleIfNotExist(filter: ObjectNode) {
        if (!filter.has(SIMPLE_KEY)) {
            filter.putObject(SIMPLE_KEY)
        }
    }

    /**
     * Создание ключа фильтра полного поиска при необходимости.
     *
     * @param filter Параметры фильтра
     */
    fun createEntityIfNotExist(filter: ObjectNode) {
        if (!filter.has(ENTITY_KEY)) {
            filter.putObject(ENTITY_KEY)
        }
    }

    /**
     * Добавление значения в параметры простого поиска.
     *
     * @param filter Параметры фильтра
     * @param key Ключ параметра
     * @param value Значение
     */
    fun appendToSimple(filter: ObjectNode, key: String, value: Boolean) {
        createSimpleIfNotExist(filter)
        getSimpleFilter(filter)!!.put(key, value)
    }

    /**
     * Добавление значений в списковый элемент полного поиска.
     *
     * @param filter Параметры фильтра
     * @param key Ключ параметра
     * @param value Список значений
     */
    fun appendToEntity(filter: ObjectNode, key: String, value: Iterable<String>) {
        createEntityIfNotExist(filter)
        val node = getEntityFilter(filter)!!.putArray(key)
        value.forEach { node.add(it) }
    }

    /**
     * Добавление значения в параметры полного поиска.
     *
     * @param filter Параметры фильтра
     * @param key Ключ параметра
     * @param value Значение
     */
    fun appendToEntity(filter: ObjectNode, key: String, value: Boolean) {
        createEntityIfNotExist(filter)
        getEntityFilter(filter)!!.put(key, value)
    }

    /**
     * Добавление параметра, по которому будут объединятся фильтры простого и полного поиска.
     *
     * @param filter Параметры фильтра
     *
     * @return Фильтр
     */
    fun appendFilterJoin(filter: JsonNode?): ObjectNode {
        val newFilter = createFilterIfNotExist(filter)
        appendToSimple(newFilter, FILTER_JOIN, true)
        appendToEntity(newFilter, FILTER_JOIN, true)
        return newFilter
    }

    /**
     * Возвращает значение параметра фильтра как текст.
     *
     * @param filter Параметры фильтра
     * @param key Ключ фильтра
     *
     * @return Значение параметра
     */
    fun getValueText(filter: JsonNode?, key: String) = filter?.get(key)?.asText(null)

    /**
     * Возвращает значение параметра фильтра как список строк.
     *
     * @param filter Параметры фильтра
     * @param key Ключ фильтра
     *
     * @return Значение параметра
     */
    fun getValueListOfString(filter: JsonNode?, key: String): MutableList<String>? {
        val listString: MutableList<String> = mutableListOf()
        val listObject = getValueIterable(filter, key)
        return if (listObject != null) {
            for (i in listObject)
                listString.add(i.asText())
            listString
        } else {
            null
        }
    }

    /**
     * Возвращает значение параметра фильтра как список значений перечисляемого типа.
     *
     * @param filter Параметры фильтра
     * @param key Ключ фильтра
     *
     * @return Значение параметра
     */
    inline fun <reified T : Enum<T>> getValueListOfEnum(filter: JsonNode?, key: String): MutableList<T>? {
        val listString: MutableList<T> = mutableListOf()
        val listObject = getValueIterable(filter, key)
        return if (listObject != null) {
            for (i in listObject)
                listString.add(enumValueOf(i.asText()))
            listString
        } else {
            null
        }
    }

    /**
     * Возвращает значение параметра фильтра как объект.
     *
     * @param filter Параметры фильтра
     * @param key Ключ фильтра
     *
     * @return Объект параметра
     */
    fun getValueObject(filter: JsonNode?, key: String) = filter?.get(key)

    /**
     * Возвращает значение параметра фильтра как список.
     *
     * @param filter Параметры фильтра
     * @param key Ключ фильтра
     *
     * @return Список параметра
     */
    fun getValueIterable(filter: JsonNode?, key: String) = filter?.get(key)?.asIterable()

    /**
     * Возвращает значение параметра фильтра как логический тип.
     *
     * @param filter Параметры фильтра
     * @param key Ключ фильтра
     *
     * @return Значение параметра
     */
    fun getValueBoolean(filter: JsonNode? = null, key: String) = filter?.get(key)?.asBoolean()

    /**
     * Возвращает значение параметра фильтра как вещественное число.
     *
     * @param filter Параметры фильтра
     * @param key Ключ фильтра
     *
     * @return Значение параметра
     */
    fun getValueDecimal(filter: JsonNode? = null, key: String) = getValueText(filter, key)?.toBigDecimal()
}

