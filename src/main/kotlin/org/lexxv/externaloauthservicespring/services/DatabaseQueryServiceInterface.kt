package org.lexxv.externaloauthservicespring.services

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.ObjectNode
import org.lexxv.externaloauthservicespring.entities.EntityInterface
import org.lexxv.externaloauthservicespring.graphql.inputs.PaginationInput
import org.lexxv.externaloauthservicespring.graphql.attributes.PageableListAttributesInterface
import org.lexxv.externaloauthservicespring.graphql.inputs.FilterInputInterface
import org.lexxv.externaloauthservicespring.graphql.inputs.PaginationInputInterface
import org.lexxv.externaloauthservicespring.sort.SortableInterface
import org.lexxv.lib.jsonmapper.JsonMapper
import java.util.UUID

/**
 * Интерфейс сервисов, описывающих логику взаимодействия с базой данных
 *
 * @author Alexey Vorobyev <mister.alex49@yandex.ru>
 */
interface DatabaseQueryServiceInterface<
        Entity : EntityInterface
        > {
    /**
     * Возвращает список сущностей.
     *
     * @param filter Параметры фильтра
     * @param sort Параметры сортировки
     * @param pagination Параметры пагинации
     *
     * @return Список
     */
    fun findAll(filter: FilterInputInterface? = null, sort: Iterable<SortableInterface>, pagination: PaginationInputInterface): PageableListAttributesInterface<Entity>

    /**
     * Возвращает одну сущность по идентификатору.
     *
     * @param id Идентификатор
     *
     * @return Объект сущности
     */
    fun findById(id: UUID): Entity?

    /**
     * Возвращает одну сущность по идентификатору.
     *
     * @param id Идентификатор
     * @param filter Дополнительные фильтры
     *
     * @return Объект сущности
     */
    fun findById(id: UUID, filter: ObjectNode): Entity?
}
