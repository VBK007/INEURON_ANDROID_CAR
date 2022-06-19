package nr.king.carserv.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import nr.king.carserv.common.Common
import nr.king.carserv.di.CommonRepo
import nr.king.carserv.di.PreferenceManager
import nr.king.carserv.model.RegisterModel
import nr.king.carserv.model.RegisterResponse
import nr.king.carserv.model.UserModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    var commonRepo: CommonRepo,
    var preferenceManager: PreferenceManager
) : BaseViewModel() {

    var TAG="LoginViewModel"
    var apiRegisterResponse:MutableLiveData<RegisterResponse> = MutableLiveData()

    fun getTokenWithDefaultCredentials(registerModel: RegisterModel)
    {
       viewModelScope.launch(Dispatchers.IO)
       {
           try {
               showLoader()
                commonRepo.getDetails(registerModel).apply {
                    this.let {
                        when(it.role.id)
                        {
                            1->{
                                setUi(MOVE_TO_HOME)
                            }
                            2->{
                                setUi(MOVE_TO_HOME_USER)
                            }
                        }
                    }
                }
               hideLoader()

           }
           catch (exception:Exception)
           {
               setUi(HOME_API_ERROR)
               Log.e(TAG, "getTokenWithDefaultCredentials: ${exception.message}", exception)
           }
       }

    }

    fun doLogin(registerModel: RegisterModel) {
        viewModelScope.launch (Dispatchers.IO)
        {
            try {
                commonRepo.getAllHomes(registerModel.password?:"",registerModel.userName?:"")
                    .apply {
                        getTokenWithDefaultCredentials(registerModel)
                        //setUi(MOVE_TO_HOME)
                    }
            }
            catch (e:Exception)
            {
                Log.e(TAG, "doRegister: ${e.message}", e)
                if (Common.isTokenGetted)
                {
                    //setUi(MOVE_TO_HOME)
                    Common.isTokenGetted=false
                    registerModel.id = preferenceManager.getAuthToken()
                    getTokenWithDefaultCredentials(registerModel)
                }
                else{
                    setUi(HOME_API_ERROR)
                }
            }
        }
    }

    fun doRegister(registerModel: UserModel) {
        viewModelScope.launch (Dispatchers.IO)
        {
            try {
                commonRepo.doRegisteration(registerModel)
                    .apply{
                            apiRegisterResponse.postValue(this.data)
                            setUi(MOVE_TO_REGISTER_PICKUP)
                    }
            }
            catch (e:Exception)
            {
                Log.e(TAG, "doRegister: ${e.message}", e)
                setUi(SERVER_ERRO)
            }
        }
    }



    companion object
    {
        const val MOVE_TO_HOME=100
        const val MOVE_TO_HOME_USER=106
        const val HOME_API_ERROR=101
        const val SERVER_ERRO = 103
        const val MOVE_TO_REGISTER_PICKUP=102
        const val REGISTER_API_ERROR=104
    }


}