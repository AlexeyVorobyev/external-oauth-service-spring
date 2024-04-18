package org.lexxv.externaloauthservicespring.sort

import org.springframework.data.domain.Sort

class SortById : SortableInputInterface {
    override fun getDbSort(): Sort = Sort.by(Sort.Direction.ASC, "id")
}