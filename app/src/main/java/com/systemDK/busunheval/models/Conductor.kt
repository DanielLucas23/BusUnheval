package com.systemDK.busunheval.models

import com.beust.klaxon.*

private val klaxon = Klaxon()
data class Conductor (
    val id: String? = null,
    val name: String? = null,
    val lastname: String? = null,
    val email: String? = null,
    val phone: String? = null,
    var image: String? = null,
    val platenumber: String? = null,
    val colorcar: String? = null,
    val brandcar: String? = null,
    var token: String? = null
) {

    public fun toJson() = klaxon.toJsonString(this)

    companion object {
        public fun fromJson(json: String) = klaxon.parse<Conductor>(json)
    }
}