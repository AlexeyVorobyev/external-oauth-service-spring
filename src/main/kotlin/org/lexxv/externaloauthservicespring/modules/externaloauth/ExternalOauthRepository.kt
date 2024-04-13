package org.lexxv.externaloauthservicespring.modules.externaloauth

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface ExternalOauthRepository : JpaRepository<ExternalOauthRepository, UUID> {
}
