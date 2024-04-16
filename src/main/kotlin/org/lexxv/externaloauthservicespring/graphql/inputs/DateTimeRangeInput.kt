package org.lexxv.externaloauthservicespring.graphql.inputs

import com.fasterxml.jackson.annotation.JsonIgnore
import org.lexxv.externaloauthservicespring.DatesFormatter
import java.time.OffsetDateTime

/**
 * Объект входных данных фильтра по датам.
 *
 * Преобразованные даты представлены как объекты `OffsetDateTime`.
 *
 * @property start Начальное значение
 * @property end Конечное значение
 * @property startDate Объект даты начального значения
 * @property endDate Объект даты конечного значения
 *
 * @author Alexey Vorobyev <mister.alex49@yandex.ru>
 */
class DateTimeRangeInput(
    val start: String? = null,
    val end: String? = null
) {
    @get:JsonIgnore
    val startDate: OffsetDateTime
        get() = DatesFormatter.parse(start!!)

    @get:JsonIgnore
    val endDate: OffsetDateTime
        get() = DatesFormatter.parse(end!!)
}
