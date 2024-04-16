package org.lexxv.externaloauthservicespring.graphql

import graphql.kickstart.tools.GraphQLMutationResolver
import org.springframework.stereotype.Service

/**
 * Корневой компонент обработки `GraphQL` запросов на изменение.
 *
 * @author Alexey Vorobyev <mister.alex49@yandex.ru>
 */
@Service
class Mutation(
//    @Lazy val stat: StatMutations
) : GraphQLMutationResolver
