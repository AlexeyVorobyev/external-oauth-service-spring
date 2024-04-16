package org.lexxv.externaloauthservicespring.services

import com.fasterxml.jackson.databind.node.ObjectNode
import org.lexxv.externaloauthservicespring.entities.ExternalOauthEntity
import org.lexxv.externaloauthservicespring.graphql.attributes.PageableListAttributesInterface
import org.lexxv.externaloauthservicespring.graphql.inputs.FilterInputInterface
import org.lexxv.externaloauthservicespring.graphql.inputs.PaginationInputInterface
import org.lexxv.externaloauthservicespring.sort.SortableInterface
import org.springframework.stereotype.Service
import java.util.*

@Service
class ExternalOauthService: DatabaseQueryServiceInterface<ExternalOauthEntity> {
    override fun findAll(
        filter: FilterInputInterface?,
        sort: Iterable<SortableInterface>,
        pagination: PaginationInputInterface
    ): PageableListAttributesInterface<ExternalOauthEntity> {
        TODO("Not yet implemented")
    }

    override fun findById(id: UUID): ExternalOauthEntity? {
        TODO("Not yet implemented")
    }

    override fun findById(id: UUID, filter: ObjectNode): ExternalOauthEntity? {
        TODO("Not yet implemented")
    }
}