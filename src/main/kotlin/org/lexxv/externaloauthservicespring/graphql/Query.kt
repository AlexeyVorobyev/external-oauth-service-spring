package org.lexxv.externaloauthservicespring.graphql

import graphql.kickstart.tools.GraphQLQueryResolver
import org.lexxv.externaloauthservicespring.graphql.queries.externaloauth.ExternalOauthQueries
import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Service


/**
 * Корневой Компонент обработки `GraphQL` запросов чтения.
 *
 * @author Alexey Vorobyev <mister.alex49@yandex>
 */
@Service
class Query(
    @Lazy val externalOauthQueries: ExternalOauthQueries
) : GraphQLQueryResolver
