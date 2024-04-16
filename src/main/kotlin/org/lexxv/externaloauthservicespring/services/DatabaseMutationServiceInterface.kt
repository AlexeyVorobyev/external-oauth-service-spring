package org.lexxv.externaloauthservicespring.services

import com.fasterxml.jackson.databind.node.ObjectNode
import org.lexxv.externaloauthservicespring.entities.EntityInterface
import java.util.UUID


/**
 * Интерфейс сервисов непосредственного редактирования данных в БД.
 *
 * @author Alexey Vorobyev <mister.alex49@yandex.ru>
 */
interface DatabaseMutationServiceInterface<Entity : EntityInterface> {
    /**
     * Добавляет сущность в БД.
     *
     * @param input Сущьность
     */
    fun save(input: ObjectNode)

    /**
     * Обновляет сущность в БД.
     *
     * @param input Входные данные для изменения
     */

    fun update(input: ObjectNode)

    /**
     * Удаляет сущность из БД.
     *
     * @param id Идентификатор сущности
     */
    fun deleteById(id: UUID)
}
