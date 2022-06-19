package nr.king.carserv.model

import androidx.annotation.Keep

@Keep
data class HomeResponse(
    var status:Boolean =false,
    var message:String?=null
)