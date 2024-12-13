package org.foi.hr.air.spotly.data

import kotlinx.serialization.Serializable

@Serializable
data class ParkingSpaceData(
    val name: String,
    val location: String,
    val zones: Map<String, Zone>
) {
    @Serializable
    data class Zone(
        val location: ZoneLocation,
        val invalid: Boolean,
        val charger: Boolean,
        val employee: Boolean
    ) {
        @Serializable
        data class ZoneLocation(
            val position: Position? = null,
            val points: String? = null,
            val size: Size? = null,
            val shape: String
        ) {
            @Serializable
            data class Position(
                val x: String,
                val y: String
            )

            @Serializable
            data class Size(
                val width: String,
                val height: String
            )
        }
    }
}