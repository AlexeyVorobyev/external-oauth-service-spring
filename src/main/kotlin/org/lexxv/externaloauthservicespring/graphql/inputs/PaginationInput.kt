package org.lexxv.externaloauthservicespring.graphql.inputs

import org.lexxv.externaloauthservicespring.api.input.PaginationInputInterface

/**
 * Параметры пагинации.
 *
 * Входные параметры из GraphQL запросы преобразуются в данный объект.
 *
 * @property page Номер страницы (начинается с 0)
 * @property perPage Количество элементов на странице
 *
 * @author Alexey Vorobyev <mister.alex49@yandex.ru>
 */
data class PaginationInput(
    override val page: Int = 0,
    override var perPage: Int = 20
): PaginationInputInterface {
    companion object {
        /**
         * Максимально возможное количество элементов на странице.
         */
        const val MAX_PER_PAGE = 1000
    }

    /**
     * Инициализация.
     */
    init {
        this.perPage = if (perPage > MAX_PER_PAGE) MAX_PER_PAGE else perPage
    }
}
