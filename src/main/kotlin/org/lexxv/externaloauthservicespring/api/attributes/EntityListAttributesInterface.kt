package org.lexxv.externaloauthservicespring.api.attributes

import org.lexxv.externaloauthservicespring.graphql.attributes.PaginationInfoAttributes

/**
 * Интерфейс списков с пагинацией.
 *
 * Все списки с постраничной навигацией должны реализовать данный интерфейс с указанием типа объекта, список которого
 * будет возвращён клиенту. Интерфейс предоставляет информацию о постраничной навигации и список элементов на текущей
 * странице.
 *
 * @property pageInfo Информация о пагинации
 * @property items Список элементов
 *
 * @author Alexey Vorobyev <mister.alex49@yandex.ru>
 */
interface EntityListAttributesInterface<EntityAttributes : EntityAttributesInterface> {
    var pageInfo: PaginationInfoAttributes?
    var items: Iterable<EntityAttributes>?
}
