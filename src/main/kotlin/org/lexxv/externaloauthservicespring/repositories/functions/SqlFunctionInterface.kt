package org.lexxv.externaloauthservicespring.repositories.functions

/**
 * Интерфейс функций для работы с СУБД, которых нет в `JPA`.
 *
 * @property name Имя функции
 * @property template SQL шаблон
 *
 * @author Alexey Vorobyev <mister.alex49@yandex.ru>
 */
interface SqlFunctionInterface {
    val name: String

    val template: String
}
