package org.lexxv.externaloauthservicespring.graphql.mutations

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.ObjectNode
import javax.validation.ConstraintViolationException
import org.lexxv.externaloauthservicespring.entities.EntityInterface
import org.lexxv.externaloauthservicespring.exceptions.BuiltinTypeException
import org.lexxv.externaloauthservicespring.exceptions.EntityNotFoundException
import org.lexxv.externaloauthservicespring.api.attributes.MutationMetaAttributesInterface
import org.lexxv.externaloauthservicespring.graphql.enums.OperationStatusEnum
import org.lexxv.externaloauthservicespring.graphql.problems.Problem
import org.lexxv.externaloauthservicespring.services.DatabaseMutationServiceInterface
import org.lexxv.lib.jsonmapper.JsonMapper
import org.springframework.dao.DataIntegrityViolationException
import org.postgresql.util.PSQLException
import java.util.*


/**
 * Базовый класс обработчиков GraphQL-запросов на мутацию.
 *
 * @property dbService Сервис получения данных из БД
 *
 * @author Alexey Vorobyev <mister.alex49@yandex.ru>
 */
abstract class BaseMutations<Entity : EntityInterface, Attributes : MutationMetaAttributesInterface>(
    protected open val dbService: DatabaseMutationServiceInterface<Entity>
) {
    /**
     * Добавление записи.
     *
     * @param input Данные записи.
     *
     * @return Объект ответа клиенту
     */
    @Suppress("TooGenericExceptionCaught")
    open fun create(input: ObjectNode): Attributes {
        val recordId = getRecordId(input)
        val payload = getRecordPayload(UUID.randomUUID(), recordId)
        try {
            input.put("id", recordId.toString())
            dbService.save(input)
            saveRelations(input)
        } catch (e: Throwable) {
            makeResponseFail(payload, getSaveProblem(e))
        }
        return payload
    }

    /**
     * Изменение записи.
     *
     * @param input Данные записи.
     *
     * @return Объект ответа клиенту
     */
    @Suppress("TooGenericExceptionCaught")
    open fun update(input: ObjectNode): Attributes {
        val payload = getRecordPayload(UUID.randomUUID(), getRecordId(input))
        try {
            dbService.update(input)
            saveRelations(input)
        } catch (e: Throwable) {
            makeResponseFail(payload, getSaveProblem(e))
        }
        return payload
    }

    /**
     * Удаление записи.
     *
     * @param id Идентификатор.
     *
     * @return Объект ответа клиенту
     */
    @Suppress("TooGenericExceptionCaught")
    open fun delete(id: UUID): Attributes {
        val payload = getRecordPayload(UUID.randomUUID(), id)
        try {
            deleteRelations(id)
            dbService.deleteById(id)
        } catch (e: Throwable) {
            makeResponseFail(payload, getDeleteProblem(e))
        }
        return payload
    }

    /**
     * Возвращает идентификатор сущности.
     *
     * Если во входных данных ключ отсутствует, то объект считается новым и генерируется новый ключ.
     *
     * @param input Входные данные
     * @param key Ключ идентификатора
     *
     * @return Идентификатор сущности
     */
    private fun getRecordId(input: JsonNode, key: String = "id"): UUID =
        JsonMapper.asUUIDOrNull(input[key]) ?: UUID.randomUUID()

    /**
     * Возвращает Payload объекта.
     *
     * @param operationId Идентификатор операции
     * @param recordId Идентификатор сущности
     *
     * @return Обновленный объект
     */
    protected abstract fun getRecordPayload(operationId: UUID, recordId: UUID): Attributes

    /**
     * Удаление связей.
     *
     * @param id Идентификатор главной сущности
     */
    protected open fun deleteRelations(id: UUID) {}

    /**
     * Сохранение связи.
     *
     * @param input Данные записи
     */
    protected open fun saveRelations(input: ObjectNode) {}

    private fun makeResponseFail(response: Attributes, error: Problem) {
        response.status = OperationStatusEnum.ERROR
        response.error = error
    }

    private fun getSaveProblem(e: Throwable): Problem =
        when (e) {
            is BuiltinTypeException -> Problem("unable_mutation_builtin_type")
            is EntityNotFoundException -> Problem("unable_update_unknown_entity")
            is ConstraintViolationException -> Problem("validate_error")
            is DataIntegrityViolationException -> {
                if (e.cause?.cause is PSQLException) {
                    getSQLProblem(e.cause?.cause as PSQLException)
                } else {
                    Problem("unable_mutation")
                }
            }
            else -> Problem("unable_mutation")
        }

    private fun getDeleteProblem(e: Throwable): Problem =
        when (e) {
            is EntityNotFoundException -> Problem("unable_delete_unknown_entity")
            is BuiltinTypeException -> Problem("unable_mutation_builtin_type")
            is DataIntegrityViolationException -> {
                /**
                 * Ошибка нарушения ссылочного ограничения (возникает при удалении одной сущности, связанной с другими)
                 */
                if (e.cause?.cause is PSQLException && (e.cause!!.cause as PSQLException).sqlState == "23503") {
                    if (e.cause!!.cause?.message!!.replace("ERROR: ", "").replace("\n", ". ")
                            .contains("parent")
                    ) {
                        Problem("unable_delete_parent")
                    } else {
                        Problem("unable_delete_linked")
                    }
                } else {
                    Problem("unable_mutation")
                }
            }

            else -> Problem("unable_mutation")
        }

    private fun getSQLProblem(exception: PSQLException): Problem {
        val message = exception.message!!.replace("ERROR: ", "").replace("\n", ". ")
        var messageVars: Array<String>? = null
        return when (exception.sqlState) {
            /**
             * Ошибка отсутствия записи в БД
             */
            "23503" -> {
                val matches = Regex("Key (.*) is not present in table").find(message)
                if (matches != null && matches.groups[1] != null) {
                    messageVars = arrayOf(matches.groupValues[1])
                }
                Problem("unable_save_unknown_foreign_key", messageVars = messageVars)
            }

            /**
             * Ошибка нарушения ограничения уникальности
             */
            "23505" -> {
                val matches = Regex("Key \\(name\\)=\\((.*)\\) already exists").find(message)
                if (matches != null && matches.groups[1] != null) {
                    messageVars = arrayOf(matches.groupValues[1])
                }
                Problem("unable_save_duplicate_name", messageVars = messageVars)
            }

            else -> Problem("unable_mutation")
        }
    }
}
