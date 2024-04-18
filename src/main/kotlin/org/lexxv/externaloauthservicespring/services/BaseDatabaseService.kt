package org.lexxv.externaloauthservicespring.services

import org.lexxv.externaloauthservicespring.api.attributes.EntityAttributesInterface
import org.lexxv.externaloauthservicespring.api.input.*
import org.lexxv.externaloauthservicespring.entities.EntityInterface
import org.lexxv.externaloauthservicespring.graphql.attributes.BaseEntityListAttributes
import org.lexxv.externaloauthservicespring.graphql.attributes.BaseFindByIdInput
import org.lexxv.externaloauthservicespring.graphql.inputs.BaseListFilterInput
import org.lexxv.externaloauthservicespring.graphql.inputs.BaseListInput
import org.lexxv.externaloauthservicespring.graphql.inputs.PaginationInput
import org.lexxv.externaloauthservicespring.sort.SortableInputInterface
import org.lexxv.externaloauthservicespring.graphql.mapper.BaseEntityToEntityAttributesMapper
import org.lexxv.externaloauthservicespring.repositories.BaseRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.jpa.domain.Specification
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import java.util.*

abstract class BaseDatabaseService<
        Entity: EntityInterface,
        Repository : BaseRepository<Entity>,
        EntityAttributes: EntityAttributesInterface,
        EntityFilterInput: EntityFilterInterface,
        SortableInput: SortableInputInterface,
        EntityCreateInput: EntityCreateInputInterface,
        EntityUpdateInput: EntityUpdateInputInterface,
        EntityToEntityAttributesMapper: BaseEntityToEntityAttributesMapper<Entity,EntityAttributes>
        >(
            private val repository: Repository,
        ) {

    @Autowired
    private lateinit var entityToEntityAttributesMapper: EntityToEntityAttributesMapper
    @Autowired
    private lateinit var updateInputToAttributesMapper: EntityToEntityAttributesMapper
    @Autowired
    private lateinit var entityToEntityAttributesMapper: EntityToEntityAttributesMapper

    abstract fun list(input: BaseListInput<EntityFilterInput, BaseListFilterInput<EntityFilterInput>, Iterable<SortableInput>>): BaseEntityListAttributes<Entity,EntityAttributes>

    abstract fun buildSpecification(filter:BaseListFilterInput<EntityFilterInput>): Specification<Entity>

    open fun create(input: EntityCreateInput) {

    }

    open fun update(input: BaseFindByIdInput<EntityUpdateInput>) {
        val toUpdate = repository.findByIdGuarantee(input.id)

    }

    open fun delete(input: UUID) {
        repository.deleteById(input)
    }

    open fun record(id: UUID): EntityAttributes {
        return entityToEntityAttributesMapper.map(repository.findByIdGuarantee(id))
    }

    private fun getPage(input: BaseListInput<EntityFilterInput, BaseListFilterInput<EntityFilterInput>, Iterable<SortableInput>>): Page<Entity> {
        val pageable: Pageable = getPageable(input.sort, input.pagination)
        val specification: Specification<Entity>? = input.filter?.let { buildSpecification(it)}

        var page: Page<Entity>

        if (specification == null) {
            page = repository.findAll(pageable)
        }
        else {
            page = (repository as JpaSpecificationExecutor<Entity>).findAll(specification, pageable)
        }

        return page
    }


    private fun getPageable(sort: Iterable<SortableInput>?, pagination: PaginationInput): Pageable {
        if (sort != null) {
            return PageRequest.of(pagination.page, pagination.perPage, getSort(sort))
        }
        else {
            return PageRequest.of(pagination.page, pagination.perPage)
        }
    }

    private fun getSort(sort: Iterable<SortableInput>): Sort {
        return sort.map { it.getDbSort() } .reduce{ acc, it -> acc.and(it) }
    }
}