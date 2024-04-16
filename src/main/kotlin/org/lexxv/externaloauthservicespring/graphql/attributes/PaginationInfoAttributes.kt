package org.lexxv.externaloauthservicespring.graphql.attributes

/**
 * Объект, содержащий информацию о текущей странице для пагинации.
 *
 * Передаётся в ответе клиенту.
 *
 * @property totalPages Общее количество страниц
 * @property totalItems Общее количество элементов
 * @property page Номер страницы (начинается с 0)
 * @property perPage Количество элементов на странице
 * @property hasNextPage Есть следующая страница
 * @property hasPreviousPage Есть предыдущая страница
 *
 * @author Alexey Vorobyev <mister.alex49@yandex.ru>
 */
data class PaginationInfoAttributes(
    val totalPages: Int,
    val totalItems: Long,
    val page: Int,
    val perPage: Int,
    val hasNextPage: Boolean,
    val hasPreviousPage: Boolean
)
