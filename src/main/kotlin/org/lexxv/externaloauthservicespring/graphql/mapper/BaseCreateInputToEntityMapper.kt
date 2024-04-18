package org.lexxv.externaloauthservicespring.graphql.mapper

import org.lexxv.externaloauthservicespring.api.attributes.EntityAttributesInterface
import org.lexxv.externaloauthservicespring.api.input.EntityCreateInputInterface
import org.lexxv.externaloauthservicespring.entities.EntityInterface

abstract class BaseCreateInputToEntityMapper<EntityCreateInput : EntityCreateInputInterface, Entity : EntityInterface> {
    abstract fun map (input: EntityCreateInput):Entity
}