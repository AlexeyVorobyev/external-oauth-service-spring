package org.lexxv.externaloauthservicespring.modules.connection

import BaseEntity
import jakarta.persistence.*
import org.lexxv.externaloauthservicespring.modules.externaloauth.ExternalOauthEntity

/**
 * Сущность, описывающая интеграцию пользователя с некоторым внешним авторизационным сервисом
 *
 * @property userId Id пользователя, виртуально отсылающий к пользователю
 * @property externalOauth ссылка на сущность внешнего сервиса авторизации
 *
 * @author Alexey Vorobyev <mister.alex49@yandex.ru>
 * */

@Entity
@Table(name = "connection")
class ConnectionEntity(
    @Column(name = "user_id", nullable = false)
    val userId: String? = null,

    @ManyToOne()
    @JoinColumn(name = "external_oauth_id", referencedColumnName = "id")
    val externalOauth: ExternalOauthEntity? = null
) : BaseEntity() {}

