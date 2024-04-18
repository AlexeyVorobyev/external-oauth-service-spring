package org.lexxv.externaloauthservicespring.repositories

import org.lexxv.externaloauthservicespring.entities.ConnectionEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

/**
 * Репозиторий сущности подключения
 *
 * @author Alexey Vorobyev <mister.alex49@yandex.ru>
 * */
@Repository
interface ConnectionRepository : BaseRepository<ConnectionEntity> {
}
