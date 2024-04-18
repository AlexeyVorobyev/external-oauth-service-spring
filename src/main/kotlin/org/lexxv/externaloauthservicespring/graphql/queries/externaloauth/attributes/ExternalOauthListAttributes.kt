package org.lexxv.externaloauthservicespring.graphql.queries.externaloauth.attributes

import org.lexxv.externaloauthservicespring.entities.ExternalOauthEntity
import org.lexxv.externaloauthservicespring.graphql.attributes.BaseEntityListAttributes
import org.lexxv.externaloauthservicespring.graphql.attributes.PaginationInfoAttributes
import org.lexxv.externaloauthservicespring.sort.ExternalOauthSortEnum
import org.lexxv.externaloauthservicespring.sort.SortInfo
import org.springframework.data.domain.Page

/**
 * Аттрибуты списка внешних авторизационных сервисов
 *
 * @author Alexey Vorobyev <mister.alex49@yandex.ru>
 * */
class ExternalOauthListAttributes(
    page: Page<*>,
    val sortInfo: SortInfo<ExternalOauthSortEnum>,
    pageInfo: PaginationInfoAttributes? = null,
    items: Iterable<ExternalOauthEntity>? = null
): BaseEntityListAttributes<ExternalOauthEntity>(page,pageInfo,items)