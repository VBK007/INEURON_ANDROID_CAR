package nr.king.carserv.di

import nr.king.carserv.model.*
import okhttp3.Response
import retrofit2.http.*

interface GetHomeInterface {

    @POST("login")
    suspend fun getALlHomes(
        @Query("password")password:String, @Query("username") username:String
    ):ApiResponse<HomeResponse>


    @POST("user/save")
    suspend  fun doRegisteration(@Body registerModel: UserModel):  ApiResponse<RegisterResponse>

    @GET("/user/getLoggedInUser/{username}")
    suspend  fun getDetails(@Header("X-Auth-Token") authToken:String, @Path(value = "username")
    username: String): GetUserDetailResponse

    @GET("/booking")
    fun getallBills():BillingResponseModel

    /*  @POST("create-deviceUser")
      suspend fun doRegister(@Body deviceUserModel: DeviceUserModel): AuthTokenResponse
  */
}