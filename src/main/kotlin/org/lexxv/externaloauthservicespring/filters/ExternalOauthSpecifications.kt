package org.lexxv.externaloauthservicespring.filters

import com.fasterxml.jackson.databind.JsonNode
import org.lexxv.externaloauthservicespring.entities.ExternalOauthEntity
import org.springframework.data.jpa.domain.Specification

/**
 * Спецификации фильтра по внешним авторизационным сервисам.
 *
 * @author Alexey Vorobyev <mister.alex49@yandex.ru>
 */
@Suppress("SpreadOperator")
object ExternalOauthSpecifications : BaseSpecifications<ExternalOauthEntity>() {
    override fun simpleFilter(filter: JsonNode?): Specification<ExternalOauthEntity?>? {
        val value = getSimpleFilterQuery(filter)
        return listOf(
            hasInField("name", value),
            hasInField("description", value),
        ).joinSpecsOr()
    }

    override fun autocompleteFilter(filter: JsonNode?): Specification<ExternalOauthEntity?>? {
        val value = getSimpleFilterQuery(filter)
        return hasInField("name", value)
    }

    override fun entityFilter(filter: JsonNode?): Specification<ExternalOauthEntity?>? =
        listOf(
            inId(filter),
            isCreatedAt(filter),
            isUpdatedAt(filter),
        ).joinSpecsAnd()
}
