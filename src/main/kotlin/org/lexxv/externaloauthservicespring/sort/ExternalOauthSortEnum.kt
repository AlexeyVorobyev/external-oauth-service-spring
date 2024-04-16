package org.lexxv.externaloauthservicespring.sort

import org.springframework.data.domain.Sort

enum class ExternalOauthSortEnum: SortableInterface {
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