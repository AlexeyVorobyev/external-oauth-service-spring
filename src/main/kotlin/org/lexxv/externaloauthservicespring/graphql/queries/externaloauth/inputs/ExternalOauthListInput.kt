package org.lexxv.externaloauthservicespring.graphql.queries.externaloauth.inputs

import org.lexxv.externaloauthservicespring.graphql.inputs.BasePageableListInput
import org.lexxv.externaloauthservicespring.graphql.inputs.FilterInputInterface
import org.lexxv.externaloauthservicespring.graphql.inputs.PageableListInputInterface
import org.lexxv.externaloauthservicespring.graphql.inputs.PaginationInput
import org.lexxv.externaloauthservicespring.sort.ExternalOauthSortEnum

class ExternalOauthListInput(
    override val pagination: PaginationInput = PaginationInput(),
    override val filter: FilterInputInterface?,
    override val sort: Iterable<ExternalOauthSortEnum>?
): BasePageableListInput<ExternalOauthSortEnum>(pagination, filter, sort)