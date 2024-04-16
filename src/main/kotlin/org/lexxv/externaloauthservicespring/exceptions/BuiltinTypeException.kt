package org.lexxv.externaloauthservicespring.exceptions

/**
 * Исключение встроенного типа.
 *
 * @property message Сообщение об ошибке
 * @property cause Исходное исключение
 *
 * @author Alexey Vorobyev <mister.alex49@yandex.ru>
 */
class BuiltinTypeException(message: String, cause: Throwable? = null) : ApplicationException(message, cause)
