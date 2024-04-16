package org.lexxv.externaloauthservicespring

import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

/**
 * Объект преобразования дат.
 *
 * Позволяет преобразовать входные строки даты в нужные объекты языка, а также проводить форматирование.
 *
 * @author Alexey Vorobyev <mister.alex49@yandex.ru>
 */
object DatesFormatter {
    /**
     * Формат даты по умолчанию.
     */
    private const val FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"

    /**
     * Преобразование даты из строки в объект.
     *
     * @param date Дата
     * @param format Формат. По умолчанию `yyyy-MM-ddTHH:mm:ss.SSSZ`
     *
     * @return Объект даты
     */
    fun parse(date: String, format: String = FORMAT): OffsetDateTime =
        LocalDateTime.parse(date, getDateTimeFormatter(format)).atZone(ZoneOffset.UTC).toOffsetDateTime()

    /**
     * Преобразование даты в форматированную строку.
     *
     * @param date Дата
     *
     * @return Строка в формате `yyyy-MM-ddTHH:mm:ss.SSSZ`
     */
    fun format(date: OffsetDateTime): String =
        date.toInstant().atZone(ZoneOffset.UTC).format(getDateTimeFormatter(FORMAT))

    /**
     * Возвращает объект форматирования дат для `OffsetDateTime` объектов.
     *
     * @param pattern Формат
     *
     * @return Объект форматирования
     */
    private fun getDateTimeFormatter(pattern: String): DateTimeFormatter = DateTimeFormatter.ofPattern(pattern)
}
