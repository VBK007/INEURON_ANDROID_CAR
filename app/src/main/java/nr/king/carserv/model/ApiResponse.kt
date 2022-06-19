package nr.king.carserv.model

import androidx.annotation.Keep

@Keep
data class ApiResponse<T>(
  var status:Boolean =false,
  var message:String?=null,
  var data: T
)