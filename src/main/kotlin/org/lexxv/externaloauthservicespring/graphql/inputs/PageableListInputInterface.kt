package org.lexxv.externaloauthservicespring.graphql.inputs

import org.lexxv.externaloauthservicespring.sort.SortableInterface

/**
 * Интерфейс описывающий стандартный инпут для метода получения всех сущностей
 *
 * @author Alexey Vorobyev <mister.alex49@yandex.ru>
 * */
interface PageableListInputInterface<Sortable : SortableInterface> {
    val filter: FilterInputInterface?
    val sort: Iterable<Sortable>?
    val pagination: PaginationInputInterface?
}