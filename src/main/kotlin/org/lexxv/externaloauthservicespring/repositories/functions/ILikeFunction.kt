package org.lexxv.externaloauthservicespring.repositories.functions

/**
 * Оператор сравнения строк без учёта регистра.
 *
 * @author Alexey Vorobyev <mister.alex49@yandex.ru>
 */
object ILikeFunction : SqlFunctionInterface {
    override val name: String = "ILIKE"

    override val template: String = "?1 ILIKE ?2"
}
