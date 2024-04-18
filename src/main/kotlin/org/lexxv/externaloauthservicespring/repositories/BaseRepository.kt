package org.lexxv.externaloauthservicespring.repositories

import org.lexxv.externaloauthservicespring.entities.EntityInterface
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*
import kotlin.jvm.optionals.getOrNull

interface BaseRepository<Entity : EntityInterface> : JpaRepository<Entity, UUID> {
    fun findByIdGuarantee(id: UUID): Entity {
        val result = findById(id).getOrNull()

        if (result == null) {
            throw Exception("Not found entity with id $id")
        }

        return result
    }
}