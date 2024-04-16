package org.lexxv.externaloauthservicespring.graphql.attributes

import org.lexxv.externaloauthservicespring.graphql.problems.Problem
import java.util.*

/**
 * Интерфейс любого объекта ответа на мутацию.
 *
 * @property recordId Идентификатор записи сущности
 *
 * @author Alexey Vorobyev <mister.alex49@yandex.ru>
 */
interface MutationMetaAttributesInterface : OperationMetaAttributesInterface {
    val recordId: UUID
}
