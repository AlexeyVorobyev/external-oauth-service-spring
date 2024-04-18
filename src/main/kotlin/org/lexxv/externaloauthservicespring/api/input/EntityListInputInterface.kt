package org.lexxv.externaloauthservicespring.api.input

import com.fasterxml.jackson.databind.node.ObjectNode
import org.lexxv.externaloauthservicespring.sort.SortableInputInterface

/**
 * Интерфейс описывающий стандартный инпут для метода получения всех сущностей
 *
 * @author Alexey Vorobyev <mister.alex49@yandex.ru>
 * */
interface EntityListInputInterface<Sortable : SortableInputInterface> {
    val filter: ObjectNode?
    val sort: Iterable<Sortable>?
    val pagination: PaginationInputInterface?
}