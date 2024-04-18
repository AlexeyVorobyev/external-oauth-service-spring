package org.lexxv.externaloauthservicespring.graphql.attributes

import org.lexxv.externaloauthservicespring.api.input.EntityUpdateInputInterface
import org.lexxv.externaloauthservicespring.api.input.FindByIdInputInterface
import java.util.*

abstract class BaseFindByIdInput<Payload:EntityUpdateInputInterface>(
    override val id: UUID,
    override val payload: Payload
):FindByIdInputInterface<Payload> {
}