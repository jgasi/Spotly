package org.foi.hr.air.spotly.data

data class ZoneList(
    val name: String,
    val invalid: Boolean,
    val charger: Boolean,
    val employee: Boolean,
    var isClicked: Boolean,
    var isTaken: Boolean
)
