package org.lexxv.externaloauthservicespring.api.input

/**
 * Интерфейс Описывающий пагинацию.
 *
 * @author Alexey Vorobyev <mister.alex49@yandex.ru>
 * */
interface PaginationInputInterface {
    val page: Int
    var perPage: Int
}