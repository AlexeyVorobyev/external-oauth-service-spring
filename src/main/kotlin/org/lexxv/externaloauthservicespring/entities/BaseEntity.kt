package org.lexxv.externaloauthservicespring.entities

import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import org.springframework.data.util.ProxyUtils
import java.time.OffsetDateTime
import java.util.*

/**
 * Базовая сущность
 *
 * @property id Основной ключ, в формате UUID
 * @property createdAt Дата и время создания сущности
 * @property createdAt Дата и время последнего изменения сущности
 *
 * @author Alexey Vorobyev <mister.alex49@yandex.ru>
 * */
@MappedSuperclass
abstract class BaseEntity: EntityInterface {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", unique = true, nullable = false)
    override lateinit var id: UUID

    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    lateinit var createdAt: OffsetDateTime

    @UpdateTimestamp()
    @Column(name = "updated_at", nullable = false)
    lateinit var updatedAt: OffsetDateTime

    override fun equals(other: Any?): Boolean {
        if (other == null) {
            return false
        }

        if (this === other) {
            return true
        }

        if (javaClass != ProxyUtils.getUserClass(other)) {
            return false
        }

        other as BaseEntity

        return this.id == other.id
    }

    override fun toString(): String {
        return "${this.javaClass.simpleName}(id=$id)"
    }

    override fun hashCode(): Int {
        return id.hashCode() ?: 0
    }
}
