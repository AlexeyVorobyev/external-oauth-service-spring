package org.lexxv.externaloauthservicespring.graphql.inputs

/**
 * Интерфейс Описывающий пагинацию.
 *
 * @author Alexey Vorobyev <mister.alex49@yandex.ru>
 * */
interface PaginationInputInterface {
    val page: Int
    var perPage: Int
}