package io.github.yeh35.channelopenapi.util

import java.time.*
import java.util.*

class DateTimeUtil {

    companion object {

        fun localDateToUnixMilli(date: LocalDate): Long {
            return localDateTimeToUnixMilli(LocalDateTime.of(date, LocalTime.MIN))
        }

        fun localDateTimeToUnixMilli(time: LocalDateTime): Long {
            return time.atOffset(ZoneOffset.UTC).toInstant().toEpochMilli()
        }

        fun unixMilliToLocalDateTime(unixMilli: Long): LocalDateTime {
            return LocalDateTime.ofInstant(Instant.ofEpochMilli(unixMilli), TimeZone.getDefault().toZoneId())
        }

    }
}