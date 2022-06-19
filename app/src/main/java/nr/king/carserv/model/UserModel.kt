package nr.king.carserv.model

import androidx.annotation.Keep

@Keep
data class UserModel(
    var firstName: String? = null,
    var lastName: String? = null,
    var mobileNumber: String? = null,
    var username: String? = null,
    var password: String? = null,
    var fcmKey: String? = null,
    var role: Role? = null
)