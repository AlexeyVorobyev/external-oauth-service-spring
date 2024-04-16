package org.lexxv.externaloauthservicespring.exceptions

/**
 * Исключение описывающее отсутствие сущности по заданным параметрам.
 *
 * @property message Сообщение об ошибке
 * @property cause Исходное исключение
 *
 * @author Alexey Vorobyev <mister.alex49@yandex.ru>
 */
class EntityNotFoundException(message: String, cause: Throwable? = null) : ApplicationException(message, cause)
