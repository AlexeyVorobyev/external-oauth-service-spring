package org.lexxv.externaloauthservicespring.graphql.queries

import com.fasterxml.jackson.databind.node.ObjectNode
import org.lexxv.externaloauthservicespring.entities.EntityInterface
import org.lexxv.externaloauthservicespring.api.attributes.EntityListAttributesInterface
import org.lexxv.externaloauthservicespring.api.input.EntityListInputInterface
import org.lexxv.externaloauthservicespring.api.input.PaginationInputInterface
import org.lexxv.externaloauthservicespring.sort.SortableInputInterface
import org.lexxv.lib.jsonmapper.JsonMapper
import java.util.*

/**
 * Базовый класс обработчиков GraphQL-запросов на чтение.
 *
 * @property dbService Сервис получения данных из БД
 *
 * @author Alexey Vorobyev <mister.alex49@uandex.ru>
 */
abstract class BaseQueries<
        Entity : EntityInterface,
        ResultList : EntityListAttributesInterface<Entity>,
        Sortable: SortableInputInterface,
        ListInput: EntityListInputInterface<Sortable>
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
        return dbService.findAll(input.filter, getSort(input.sort), getPagination(input.pagination)) as ResultList
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
