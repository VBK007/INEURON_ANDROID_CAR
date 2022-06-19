package nr.king.carserv.model

import androidx.annotation.Keep

@Keep
data class RegisterModel(
    var id:String?=null,
    var fname:String?=null,
    var lname:String?=null,
    var mobileNumber:String?=null,
    var userName:String?=null,
    var password:String?=null
)