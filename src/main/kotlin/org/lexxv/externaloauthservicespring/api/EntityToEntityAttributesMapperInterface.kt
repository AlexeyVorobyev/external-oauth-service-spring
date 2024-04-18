package org.lexxv.externaloauthservicespring.api

import org.lexxv.externaloauthservicespring.api.attributes.EntityAttributesInterface
import org.lexxv.externaloauthservicespring.entities.EntityInterface

interface EntityToEntityAttributesMapperInterface {
    fun map(entity:EntityInterface): EntityAttributesInterface
}