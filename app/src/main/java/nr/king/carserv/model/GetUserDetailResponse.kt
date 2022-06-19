package nr.king.carserv.model

import androidx.annotation.Keep

@Keep
data class GetUserDetailResponse(
    val fcmKey: String,
    val firstName: String,
    val id: String,
    val lastName: String,
    val role: RoleX,
    val username: String
)