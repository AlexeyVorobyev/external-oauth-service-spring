package org.lexxv.externaloauthservicespring.graphql.mapper

import org.lexxv.externaloauthservicespring.api.attributes.EntityAttributesInterface
import org.lexxv.externaloauthservicespring.api.input.EntityCreateInputInterface
import org.lexxv.externaloauthservicespring.api.input.EntityUpdateInputInterface
import org.lexxv.externaloauthservicespring.entities.EntityInterface

abstract class BaseUpdateInputToEntityMapper<EntityUpdateInput : EntityUpdateInputInterface, Entity : EntityInterface> {
    abstract fun map (input: EntityUpdateInput):Entity
}