package org.lexxv.externaloauthservicespring.graphql.inputs

import org.lexxv.externaloauthservicespring.api.input.EntityFilterInterface
import org.lexxv.externaloauthservicespring.api.input.ListInputInterface
import org.lexxv.externaloauthservicespring.api.input.PaginationInputInterface
import org.lexxv.externaloauthservicespring.sort.SortableInputInterface

abstract class BaseListInput<
        EntityFilter : EntityFilterInterface,
        Filter : BaseListFilterInput<EntityFilter>,
        Sort : Iterable<SortableInputInterface>
        >(
    override val pagination: PaginationInput = PaginationInput(),
    override val filter: Filter?,
    override val sort: Sort?
) : ListInputInterface