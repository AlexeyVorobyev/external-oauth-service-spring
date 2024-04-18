package org.lexxv.externaloauthservicespring.sort

import org.springframework.data.domain.Sort

/**
 * Enumerate класс, реализующий параметры сортировки и сущности внешнего авторизационного сервиса
 * */
enum class ExternalOauthSortEnum: SortableInputInterface {
    /**
     * Наименование по возрастанию
     */
    NAME_ASC {
        override fun getDbSort(): Sort = Sort.by(Sort.Direction.ASC, "name")
    },

    /**
     * Наименование по убыванию
     */
    NAME_DESC {
        override fun getDbSort(): Sort = Sort.by(Sort.Direction.DESC, "name")
    },

}