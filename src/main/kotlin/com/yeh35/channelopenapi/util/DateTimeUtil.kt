package com.yeh35.channelopenapi.util

import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.util.*

class DateTimeUtil {

    companion object {

        fun localDateToUnixMilli(date: LocalDate): Long {
            return localDateTimeToUnixMilli(LocalDateTime.of(date, LocalTime.MIN))
        }

        fun localDateTimeToUnixMilli(time: LocalDateTime): Long {
            return time.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
        }

        fun unixMilliToLocalDateTime(unixMilli: Long): LocalDateTime {
            return LocalDateTime.ofInstant(Instant.ofEpochMilli(unixMilli), TimeZone.getDefault().toZoneId())
        }

    }
}