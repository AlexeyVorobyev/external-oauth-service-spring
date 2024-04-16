package org.lexxv.externaloauthservicespring.graphql.attributes

import org.lexxv.externaloauthservicespring.graphql.enums.OperationStatusEnum
import org.lexxv.externaloauthservicespring.graphql.problems.Problem
import java.util.UUID

/**
 * Интерфейс объектов ответа на запросы записи по GraphQL (мутации, подписки).
 *
 * @property operationId Идентификатор операции
 * @property error Ошибка
 *
 * @author Alexey Vorobyev <mister.alex49@yandex.ru>
 */
interface OperationMetaAttributesInterface {
    val operationId: UUID
    var status: OperationStatusEnum
    var error: Problem?
}
