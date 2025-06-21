package com.example.nutriton.database.converters

import androidx.room.TypeConverter
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.toLocalDate
import kotlinx.datetime.toLocalDateTime

/**
 * Convertidores de tipos para las clases de fecha de kotlinx.datetime
 * que permiten a Room persistir estos objetos en la base de datos SQLite.
 */
class DateTimeConverters {

    /**
     * Convierte un LocalDate a String para guardar en la base de datos
     */
    @TypeConverter
    fun fromLocalDate(date: LocalDate?): String? {
        return date?.toString()
    }

    /**
     * Convierte un String a LocalDate al leer de la base de datos
     */
    @TypeConverter
    fun toLocalDate(dateString: String?): LocalDate? {
        return dateString?.toLocalDate()
    }

    /**
     * Convierte un LocalDateTime a String para guardar en la base de datos
     */
    @TypeConverter
    fun fromLocalDateTime(dateTime: LocalDateTime?): String? {
        return dateTime?.toString()
    }

    /**
     * Convierte un String a LocalDateTime al leer de la base de datos
     */
    @TypeConverter
    fun toLocalDateTime(dateTimeString: String?): LocalDateTime? {
        return dateTimeString?.toLocalDateTime()
    }
}
