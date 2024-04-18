package org.lexxv.externaloauthservicespring.api.input

import java.util.*

interface FindByIdInputInterface<Payload> {
    val id: UUID
    val payload: Payload
}