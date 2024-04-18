package org.lexxv.externaloauthservicespring.graphql.queries.externaloauth.inputs

import com.fasterxml.jackson.databind.node.JsonNodeFactory
import com.fasterxml.jackson.databind.node.ObjectNode
import org.lexxv.externaloauthservicespring.api.input.FilterInputInterface

/**
 * Объект входных данных фильтра по внешним авторизационным подключениям.
 *
 * @author Alexey Vorobyev <mister.alex49@yandex.ru>
 */
class ExternalOauthFilterInput : ObjectNode(JsonNodeFactory.instance), FilterInputInterface
