package org.lexxv.externaloauthservicespring.filters

import com.fasterxml.jackson.databind.JsonNode
import org.lexxv.externaloauthservicespring.entities.EntityInterface
import org.springframework.data.jpa.domain.Specification


/**
 * Интерфейс спецификаций для фильтрации сущностей из БД.
 *
 * @author Alexey Vorobyev <mister.alex49@yandex.ru>
 */
interface SpecificationsInterface<Entity : EntityInterface> {
    /**
     * Создание спецификаций фильтра.
     *
     * @param filter Параметры фильтра
     *
     * @return Объект спецификации
     */
    fun createSpecifications(filter: JsonNode? = null): Specification<Entity?>?
}
