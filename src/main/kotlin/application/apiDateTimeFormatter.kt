package application

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.toJavaLocalDateTime
import java.time.format.DateTimeFormatter

private val apiDateTimeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")

fun LocalDateTime.toApiDateTime(): String = apiDateTimeFormatter.format(toJavaLocalDateTime())
