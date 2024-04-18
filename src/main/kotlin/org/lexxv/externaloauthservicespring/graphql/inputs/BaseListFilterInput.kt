package org.lexxv.externaloauthservicespring.graphql.inputs

import org.lexxv.externaloauthservicespring.api.input.EntityFilterInterface
import org.lexxv.externaloauthservicespring.api.input.ListFilterInputInterface

abstract class BaseListFilterInput<EntityFilter: EntityFilterInterface>(
    val simpleFilter: String,
    val autocompleteFilter: String,
    val entityFilter: EntityFilter,
): ListFilterInputInterface