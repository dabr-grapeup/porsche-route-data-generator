import java.io.File
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatterBuilder
import java.time.temporal.ChronoField.*

fun main(args: Array<String>) {

    File("data/")
            .listFiles()
            .forEach {
                val startDate = LocalDateTime.parse(
                        it.name
                                .substringAfter("garmin_route_")
                                .substringBefore(".gpx"),
                        DateTimeFormatterBuilder()
                                .parseCaseInsensitive()
                                .appendValue(YEAR, 4)
                                .appendValue(MONTH_OF_YEAR, 2)
                                .appendValue(DAY_OF_MONTH, 2)
                                .appendLiteral('-')
                                .appendValue(HOUR_OF_DAY, 2)
                                .appendValue(MINUTE_OF_HOUR, 2)
                                .appendValue(SECOND_OF_MINUTE, 2)
                                .toFormatter())
                var timeLapsed: Long = 0
                it.inputStream()
                        .bufferedReader()
                        .useLines {
                            it.forEach {
                                if (!it.contains("<gpxx:rpt")) {
                                    return@forEach
                                }
                                println(startDate.plusSeconds(timeLapsed++)
                                        .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
                                        .replace('T', ' ')
                                        + ",,,,10,"
                                        + it.substringAfter("lat=")
                                        .substringBefore(' ')
                                        + ","
                                        + it.substringAfter("lon=")
                                        .substringBefore('/')
                                        + ",,,,")
                            }
                        }
            }
}