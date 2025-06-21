package com.example.nutriton.database.converters

import androidx.room.TypeConverter
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

/**
 * Convertidores de tipos para fechas que permiten a Room persistir objetos LocalDate
 * en la base de datos SQLite y viceversa.
 */
class DateConverters {

    private val formatter = DateTimeFormatter.ISO_LOCAL_DATE

    /**
     * Convierte un LocalDate a String para guardar en la base de datos
     */
    @TypeConverter
    fun fromLocalDate(date: LocalDate?): String? {
        return date?.format(formatter)
    }

    /**
     * Convierte un String a LocalDate al leer de la base de datos
     */
    @TypeConverter
    fun toLocalDate(dateString: String?): LocalDate? {
        return dateString?.let {
            return LocalDate.parse(it, formatter)
        }
    }

    /**
     * Alternativa: Convierte LocalDate a timestamp (long)
     */
    @TypeConverter
    fun fromLocalDateToTimestamp(date: LocalDate?): Long? {
        return date?.atStartOfDay(ZoneId.systemDefault())?.toInstant()?.toEpochMilli()
    }

    /**
     * Alternativa: Convierte timestamp (long) a LocalDate
     */
    @TypeConverter
    fun fromTimestampToLocalDate(timestamp: Long?): LocalDate? {
        return timestamp?.let {
            Instant.ofEpochMilli(it).atZone(ZoneId.systemDefault()).toLocalDate()
        }
    }
}
