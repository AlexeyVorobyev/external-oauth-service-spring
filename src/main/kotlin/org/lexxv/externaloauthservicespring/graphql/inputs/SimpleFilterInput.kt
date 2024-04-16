package org.lexxv.externaloauthservicespring.graphql.inputs

import com.fasterxml.jackson.databind.node.JsonNodeFactory
import com.fasterxml.jackson.databind.node.ObjectNode

/**
 * Объект входных данных простого фильтра.
 *
 * @author Alexey Vorobyev <s.astvatsaturov@vanguardsoft.ru>
 */
class SimpleFilterInput : ObjectNode(JsonNodeFactory.instance)
