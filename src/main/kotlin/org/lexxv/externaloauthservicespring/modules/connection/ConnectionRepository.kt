package org.lexxv.externaloauthservicespring.modules.connection

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface ConnectionRepository : JpaRepository<ConnectionEntity, UUID> {
}
