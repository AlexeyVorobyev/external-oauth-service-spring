package org.lexxv.externaloauthservicespring.graphql.attributes

import org.springframework.data.domain.Page
import org.lexxv.externaloauthservicespring.entities.EntityInterface

/**
 * Базовый класс возвращаемых клиенту списков с постраничной навигацией.
 *
 * На вход принимает объект [Page], полученный от какого-либо сервиса (БД, кеш).
 *
 * @param page Результат выборки данных от сервиса
 *
 * @author Alexey Vorobyev <mister.alex49@yandex.ru>
 */
abstract class BasePageableListAttributes<Entity : EntityInterface>(
    page: Page<*>,
    final override var pageInfo: PaginationInfoAttributes? = null,
    final override var items: Iterable<Entity>? = null
) : PageableListAttributesInterface<Entity> {
    init {
        pageInfo = PaginationInfoAttributes(
            page.totalPages,
            page.totalElements,
            page.number,
            page.size,
            page.hasNext(),
            page.hasPrevious()
        )
        @Suppress("UNCHECKED_CAST")
        items = items ?: page.content as Iterable<Entity>
    }
}
