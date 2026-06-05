package br.com.prumoapp.data.extensions

import dev.gitlive.firebase.firestore.Timestamp
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Instant

internal fun LocalDate.toUtcTimestamp(): Timestamp {
    val instant = this.atStartOfDayIn(TimeZone.UTC)
    return Timestamp(instant.epochSeconds, instant.nanosecondsOfSecond)
}

internal fun Instant.toUtcTimestamp(): Timestamp =
    Timestamp(this.epochSeconds, this.nanosecondsOfSecond)

internal fun Timestamp.toLocalDate(): LocalDate {
    val instant = Instant.fromEpochSeconds(seconds, nanoseconds)
    return instant.toLocalDateTime(TimeZone.UTC).date
}

internal fun Timestamp.toInstant(): Instant =
    Instant.fromEpochSeconds(seconds, nanoseconds)