package org.lexxv.externaloauthservicespring.graphql.mapper

import org.lexxv.externaloauthservicespring.api.attributes.EntityAttributesInterface
import org.lexxv.externaloauthservicespring.entities.EntityInterface

abstract class BaseEntityToEntityAttributesMapper<Entity : EntityInterface, EntityAttributes : EntityAttributesInterface> {
    abstract fun map (entity: Entity):EntityAttributes
}