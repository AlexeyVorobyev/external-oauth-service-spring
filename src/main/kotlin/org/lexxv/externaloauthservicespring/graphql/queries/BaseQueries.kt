package org.lexxv.externaloauthservicespring.graphql.queries

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.ObjectNode
import java.util.*
import org.lexxv.externaloauthservicespring.entities.EntityInterface
import org.lexxv.externaloauthservicespring.graphql.inputs.PaginationInput
import org.lexxv.externaloauthservicespring.graphql.attributes.PageableListAttributesInterface
import org.lexxv.externaloauthservicespring.graphql.inputs.PageableListInputInterface
import org.lexxv.externaloauthservicespring.graphql.inputs.PaginationInputInterface
import org.lexxv.externaloauthservicespring.services.DatabaseQueryServiceInterface
import org.lexxv.externaloauthservicespring.sort.SortableInterface
import org.lexxv.lib.jsonmapper.JsonMapper

/**
 * Базовый класс обработчиков GraphQL-запросов на чтение.
 *
 * @property dbService Сервис получения данных из БД
 *
 * @author Alexey Vorobyev <mister.alex49@uandex.ru>
 */
abstract class BaseQueries<
        Entity : EntityInterface,
        ResultList : PageableListAttributesInterface<Entity>,
        Sortable: SortableInterface,
        ListInput: PageableListInputInterface<Sortable>
        >(
    protected open val dbService: DatabaseQueryServiceInterface<Entity>
) {

    /**
     * Возвращает список сущностей.
     *
     * @param input Входные параметры запроса на лист
     *
     * @return Список
     */
    protected open fun list(input: ListInput): ResultList {
        return dbService.findAll(input.filter, getSort(input.sort as Iterable<Sortable>), getPagination(input.pagination)) as ResultList
    }

    /**
     * Возвращает сущность по идентификатору.
     *
     * @param id Идентификатор
     *
     * @return Сущность
     */
    protected open fun record(id: UUID): Entity? {
        return dbService.findById(id)
    }

    /**
     * Возвращает сущность по идентификатору и дополнительному фильтру.
     *
     * @param id Идентификатор
     * @param id Фильтр
     *
     * @return Сущность
     */
    protected fun record(id: UUID, filter: ObjectNode): Entity? {
        return record(id, JsonMapper.empty())
    }

    /**
     * Возвращает объект сортировки.
     *
     * Если клиент не передал параметры сортировки, то создаётся объект с параметрами по умолчанию. Каждый дочерний
     * класс должен предоставить свои параметры в виде класса перечислений, реализующего интерфейс [Sortable].
     *
     * @param sort Параметры сортировки
     *
     * @return Параметры сортировки
     */
    protected abstract fun getSort(sort: Iterable<Sortable>?): Iterable<Sortable>

    /**
     * Возвращает объект пагинации.
     *
     * Если клиент не передал параметры пагинации, то создаётся объект с параметрами по умолчанию.
     *
     * @param pagination Параметры пагинации
     *
     * @return Объект пагинации
     */
    abstract fun getPagination(pagination: PaginationInputInterface?): PaginationInputInterface
}
