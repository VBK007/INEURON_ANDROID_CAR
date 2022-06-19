package nr.king.carserv.model

import androidx.annotation.Keep

@Keep
data class BillingResponseModel(
    var message:String?=null,
    var payload:List<Payload>?=null,
    var count:String?= null
)

