package org.lexxv.externaloauthservicespring.services

import org.lexxv.externaloauthservicespring.api.attributes.EntityAttributesInterface
import org.lexxv.externaloauthservicespring.api.attributes.EntityListAttributesInterface
import org.lexxv.externaloauthservicespring.api.input.EntityFilterInterface
import org.lexxv.externaloauthservicespring.api.input.ListInputInterface
import org.lexxv.externaloauthservicespring.entities.ExternalOauthEntity
import org.lexxv.externaloauthservicespring.graphql.inputs.BaseListFilterInput
import org.lexxv.externaloauthservicespring.graphql.inputs.BaseListInput
import org.lexxv.externaloauthservicespring.repositories.ExternalOauthRepository
import org.lexxv.externaloauthservicespring.sort.SortableInterface
import org.springframework.stereotype.Service

@Service
class ExternalOAuthService(
    private val repository: ExternalOauthRepository,
): BaseDatabaseService<ExternalOauthEntity, ExternalOauthRepository>(repository) {
    override fun list(input: BaseListInput<EntityFilterInterface, BaseListFilterInput<EntityFilterInterface>, SortableInterface>): EntityListAttributesInterface<EntityAttributesInterface> {
        TODO("Not yet implemented")
    }
}