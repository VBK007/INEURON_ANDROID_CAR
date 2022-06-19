package nr.king.carserv.di

import nr.king.carserv.di.GetHomeInterface
import nr.king.carserv.di.URL_TRACK
import nr.king.carserv.model.RegisterModel
import nr.king.carserv.model.UserModel
import retrofit2.http.Body
import javax.inject.Inject

class CommonRepo @Inject constructor(
    @URL_TRACK var apiTracker: GetHomeInterface
) {
      suspend fun getAllHomes( password:String, email:String) = apiTracker.getALlHomes(password,email)
       suspend  fun doRegisteration( registerModel: UserModel) = apiTracker.doRegisteration(registerModel)
        suspend fun getDetails(deviceUserModel: RegisterModel) = apiTracker.getDetails(authToken = deviceUserModel.id?:"",deviceUserModel.userName?:"")
        suspend fun  getallBilllist() = apiTracker.getallBills()
}