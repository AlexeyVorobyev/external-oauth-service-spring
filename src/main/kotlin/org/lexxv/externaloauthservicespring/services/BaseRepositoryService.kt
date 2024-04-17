package org.lexxv.externaloauthservicespring.services

import com.fasterxml.jackson.databind.JsonNode
import org.lexxv.externaloauthservicespring.entities.EntityInterface
import org.lexxv.externaloauthservicespring.entities.ExternalOauthEntity
import org.lexxv.externaloauthservicespring.graphql.inputs.PaginationInputInterface
import org.lexxv.externaloauthservicespring.sort.SortableInterface
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.jpa.domain.Specification
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

/**
 * Абстрактный сервис взаимодействия с сущностями
 * */
abstract class BaseRepositoryService<Entity : EntityInterface, Repository : JpaRepository<Entity, UUID>> :
    DatabaseQueryServiceInterface<ExternalOauthEntity>, DatabaseMutationServiceInterface<ExternalOauthEntity> {

    /**
     * Возвращает спецификацию для фильтра.
     *
     * @param filter Параметры фильтра
     *
     * @return Спецификация
     */
    protected open fun getSpecifications(filter: JsonNode?): Specification<Entity?>? {
        TODO("Must be implemented in filterable child class")
    }


    /**
     * Возвращает объект пагинации и сортировки для БД.
     *
     * @param sort Параметры сортировки
     * @param pagination Параметры пагинации
     *
     * @return Объект пагинации
     */
    private fun getPageable(sort: Iterable<SortableInterface>, pagination: PaginationInputInterface): Pageable {
        return PageRequest.of(
            pagination.page, pagination.perPage, getSort(sort)
        )
    }

    /**
     * Возвращает объект сортировки.
     *
     * @param sorts Параметры сортировки
     *
     * @return Объект сортировки
     */
    private fun getSort(sorts: Iterable<SortableInterface>): Sort {
        return sorts.map { it.getDbSort() }.reduce { acc, it -> acc.and(it) }
    }
}