package org.lexxv.externaloauthservicespring.graphql.attributes

import org.lexxv.externaloauthservicespring.api.attributes.EntityAttributesInterface
import org.lexxv.externaloauthservicespring.api.attributes.EntityListAttributesInterface
import org.springframework.data.domain.Page
import org.lexxv.externaloauthservicespring.entities.EntityInterface

/**
 * Базовый класс возвращаемых клиенту списков с постраничной навигацией.
 *
 * На вход принимает объект [Page], полученный от какого-либо репозитория (БД, кеш).
 *
 * @param page Результат выборки данных от репозитория
 * @param mapper Преобразователь Entity -> EntityAttributes
 *
 * @author Alexey Vorobyev <mister.alex49@yandex.ru>
 */
abstract class BaseEntityListAttributes<
        Entity : EntityInterface,
        EntityAttributes : EntityAttributesInterface
        >(
    page: Page<Entity>,
    mapper: (Entity) -> EntityAttributes,
    final override var pageInfo: PaginationInfoAttributes? = null,
    final override var items: Iterable<EntityAttributes>? = null
) : EntityListAttributesInterface<EntityAttributes> {
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
        items = page.content.map { mapper(it) }
    }
}
