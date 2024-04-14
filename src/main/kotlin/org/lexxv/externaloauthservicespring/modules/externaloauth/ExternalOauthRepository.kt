package org.lexxv.externaloauthservicespring.modules.externaloauth

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface ExternalOauthRepository : JpaRepository<ExternalOauthEntity, UUID> {
}
