package org.lexxv.externaloauthservicespring.modules.externaloauth

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

/**
 * Репозиторий сущности внешней авторизации
 *
 * @author Alexey Vorobyev <mister.alex49@yandex.ru>
 * */
@Repository
interface ExternalOauthRepository : JpaRepository<ExternalOauthEntity, UUID> {
}
