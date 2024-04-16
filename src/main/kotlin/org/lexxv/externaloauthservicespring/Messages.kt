package org.lexxv.externaloauthservicespring

import java.text.MessageFormat
import java.util.ResourceBundle

/**
 * Компонент преобразования ключей сообщений в полноценные сообщения.
 *
 * В файле `messages.properties` указан список сообщений, которые могут быть возвращены пользователю. В приложении
 * указываются только ключи сообщений и параметры (если имеются). Форматирование сообщений происходит стандартными
 * средствами. При форматировании используется библиотека [ICU](http://site.icu-project.org/home), которая учитывает
 * локаль сервера в датах и числах.
 *
 * @author Alexey Vorobyev <s.astvatsaturov@vanguardsoft.ru>
 */
object Messages {
    /**
     * Возвращает отформатированное сообщение.
     *
     * @param key Ключ сообщения
     */
    fun get(
        key: String,
        data: Any? = null
    ): String {
        val bundle = ResourceBundle.getBundle("messages")
        val message = bundle.getString(key)
        return MessageFormat(message).format(data)
    }
}
