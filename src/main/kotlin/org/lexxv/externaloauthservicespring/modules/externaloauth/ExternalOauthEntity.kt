package org.lexxv.externaloauthservicespring.modules.externaloauth

import jakarta.persistence.*
import org.lexxv.externaloauthservicespring.common.database.entity.BaseEntity
import org.lexxv.externaloauthservicespring.modules.connection.ConnectionEntity

/**
 * Сущность, описывающая некоторый внешний авторизационный сервис,
 * с которым осуществляется интеграция пользователей
 *
 * @property recognitionKey Ключ, для распознавания подключения оконечным адаптером
 * @property connections Подключения пользователей к данному сервису авторизации
 * @property name Название внешнего сервиса авторизации
 *
 * @author Alexey Vorobyev <mister.alex49@yandex.ru>
 * */
@Entity
@Table(name = "external_oauth")
class ExternalOauthEntity(
    @Column(name = "recognition_key", nullable = false, unique = true)
    val recognitionKey: String? = null,

    @Column(name = "name")
    val name: String? = null,

    @OneToMany(
        cascade = [CascadeType.ALL],
        orphanRemoval = true
    )
    @JoinColumn(name = "external_oauth_id", referencedColumnName = "id")
    val connections: MutableList<ConnectionEntity> = ArrayList()
) : BaseEntity() {
}