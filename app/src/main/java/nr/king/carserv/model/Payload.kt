package nr.king.carserv.model

data class Payload(
    val fcmKey: String,
    val firstName: String,
    val id: String,
    val lastName: String,
    val role: Role,
    val username: String
)