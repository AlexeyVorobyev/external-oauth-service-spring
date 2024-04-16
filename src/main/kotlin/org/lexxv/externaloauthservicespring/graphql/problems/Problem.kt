package org.lexxv.externaloauthservicespring.graphql.problems

import org.lexxv.externaloauthservicespring.Messages

/**
 * Данные ошибки.
 *
 * @author Alexey Vorobyev <mister.alex49@yandex.ru>
 */
class Problem(
    var name: String,
    var message: String? = null,
    var code: Int = 0,
    messageVars: Array<String>? = null
) {
    init {
        message = Messages.get(name, messageVars)
    }
}
