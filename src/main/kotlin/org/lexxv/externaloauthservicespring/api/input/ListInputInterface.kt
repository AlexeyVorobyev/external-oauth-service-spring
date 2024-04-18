package org.lexxv.externaloauthservicespring.api.input

import org.lexxv.externaloauthservicespring.sort.SortableInputInterface

interface ListInputInterface {
    val pagination: PaginationInputInterface?
    val filter: ListFilterInputInterface?
    val sort: Iterable<SortableInputInterface>?
}