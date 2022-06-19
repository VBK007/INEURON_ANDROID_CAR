package nr.king.carserv.viewModel

import androidx.annotation.Keep
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

open class BaseViewModel:ViewModel() {

    val setUI:MutableLiveData<Int> = MutableLiveData()
    val  loader:MutableLiveData<Int> = MutableLiveData()
    val  error : MutableLiveData<ApiError> = MutableLiveData()

    fun showLoader()
    {
        loader.postValue(SHOW_LOADER)
    }

    fun hideLoader()
    {
        loader.postValue(HIDE_LOADER)
    }

    fun setUi(uiValue: Int)
    {
        setUI.postValue(uiValue)
    }

    companion object{
        const val SHOW_LOADER = -1001
        const val  HIDE_LOADER = -1002
    }


}


@Keep
data class ApiError(
    var apiNo:Int,
    var exception: String
)