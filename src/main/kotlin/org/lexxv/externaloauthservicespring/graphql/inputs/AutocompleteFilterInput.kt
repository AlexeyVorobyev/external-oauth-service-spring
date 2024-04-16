package org.lexxv.externaloauthservicespring.graphql.inputs

import com.fasterxml.jackson.databind.node.JsonNodeFactory
import com.fasterxml.jackson.databind.node.ObjectNode

/**
 * Объект входных данных для фильтрации с автодополнением.
 *
 * @author Alexey Vorobyev <mister.alex49@yandex.ru>
 */
class AutocompleteFilterInput : ObjectNode(JsonNodeFactory.instance)
