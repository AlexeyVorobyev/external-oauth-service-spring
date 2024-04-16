package org.lexxv.externaloauthservicespring.sort

/**
 * Информация о сортировке.
 *
 * @property current Текущая сортировка
 * @property allowed Возможные варианты сортировки
 *
 * @author Alexey Vorobyev <mister.alex49@yandex.ru>
 */
class SortInfo<Sortable:SortableInterface>(
    val current: Iterable<Sortable>,
    val allowed: Iterable<Sortable>
)
