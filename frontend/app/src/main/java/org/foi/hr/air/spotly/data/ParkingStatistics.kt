import kotlinx.serialization.Serializable

@Serializable
data class ParkingStatistics(
    val ukupno: Int,
    val slobodna: Int,
    val rezervirana: Int,
    val blokirana: Int
)