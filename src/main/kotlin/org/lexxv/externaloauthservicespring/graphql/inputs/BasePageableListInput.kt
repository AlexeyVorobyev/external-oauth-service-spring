package org.lexxv.externaloauthservicespring.graphql.inputs

import org.lexxv.externaloauthservicespring.sort.SortableInterface

/**
 * Абстрактный класс, частично реализующий интерфейс инпута для метода списка сущностей
 *
 * @author Alexey Vorobyev <mister.alex49@yandex.ru>
 * */
abstract class BasePageableListInput<Sortable : SortableInterface>(
    override val pagination: PaginationInput? = PaginationInput(),
    override val filter: FilterInputInterface?,
    override val sort: Iterable<Sortable>?
) : PageableListInputInterface<Sortable>