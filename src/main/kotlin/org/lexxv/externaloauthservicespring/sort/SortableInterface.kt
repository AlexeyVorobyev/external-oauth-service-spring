package org.lexxv.externaloauthservicespring.sort

import org.springframework.data.domain.Sort

/**
 * Интерфейс перечней сортировок сущностей.
 *
 * Перечни должны представлять собой перечисляемые классы, которые могут предоставить объекты сортировок для различных
 * источников: БД, очередь, кеш.
 *
 * @author Alexey Vorobyev <mister.alex49@yandex.ru>
 */
interface SortableInterface {
    /**
     * Возвращает объект сортировки для БД.
     *
     * @return Объект сортировки
     */
    fun getDbSort(): Sort
}
