package org.lexxv.externaloauthservicespring.graphql.queries.externaloauth

import org.lexxv.externaloauthservicespring.entities.ExternalOauthEntity
import org.lexxv.externaloauthservicespring.graphql.inputs.PageableListInputInterface
import org.lexxv.externaloauthservicespring.graphql.inputs.PaginationInput
import org.lexxv.externaloauthservicespring.graphql.inputs.PaginationInputInterface
import org.lexxv.externaloauthservicespring.graphql.queries.BaseQueries
import org.lexxv.externaloauthservicespring.graphql.queries.externaloauth.attributes.ExternalOauthListAttributes
import org.lexxv.externaloauthservicespring.graphql.queries.externaloauth.inputs.ExternalOauthListInput
import org.lexxv.externaloauthservicespring.services.ExternalOauthService
import org.lexxv.externaloauthservicespring.sort.ExternalOauthSortEnum
import org.lexxv.externaloauthservicespring.sort.SortableInterface
import org.springframework.stereotype.Service
import java.util.*

/**
 * Запросы получения данных внешних сервисов авторизации.
 *
 * @property dbService Сервис получения данных внешних сервисов авторизации из БД
 *
 * @author Alexey Vorobyev <mister.alex49@yandex.ru>
 */
@Service
class ExternalOauthQueries(
    override val dbService: ExternalOauthService,
) : BaseQueries<ExternalOauthEntity, ExternalOauthListAttributes, ExternalOauthSortEnum, ExternalOauthListInput>(dbService) {

    override fun list(input: ExternalOauthListInput): ExternalOauthListAttributes {
        return super.list(input)
    }

    override fun record(id: UUID): ExternalOauthEntity? {
        return super.record(id)
    }

    override fun getSort(sort: Iterable<ExternalOauthSortEnum>?): Iterable<ExternalOauthSortEnum> {
        return sort ?: listOf(ExternalOauthSortEnum.NAME_ASC)
    }

    override fun getPagination(pagination: PaginationInputInterface?): PaginationInputInterface {
        return pagination ?: PaginationInput()
    }
}
