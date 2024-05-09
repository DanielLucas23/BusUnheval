package com.systemDK.busunheval.models
import com.beust.klaxon.*

private val klaxon = Klaxon()
data class Estudiante (
    val id: String? = null,
    val nombre: String? = null,
    val apellido: String? = null,
    val email: String? = null,
    val telefono: String? = null,
    val imagen: String? = null
) {

    public fun toJson() = klaxon.toJsonString(this)

    companion object {
        public fun fromJson(json: String) = klaxon.parse<Estudiante>(json)
    }
}