package nr.king.carserv.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import nr.king.carserv.R
import nr.king.carserv.databinding.SplashScreenBinding
import nr.king.carserv.viewModel.BaseViewModel
import nr.king.carserv.viewModel.LoginViewModel

@AndroidEntryPoint
class SplashScreenActivity:AppCompatActivity() {
   lateinit var  binding: SplashScreenBinding
    private val loginViewModel by viewModels<LoginViewModel>()

   companion object{

   }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.splash_screen)
        binding.apply {
            observer()
            getLoginDetails()

        }
    }

    fun  getLoginDetails()
    {
        //loginViewModel.getTokenWithDefaultCredentials(registerModel)
        GlobalScope.launch(Dispatchers.IO) {
            delay(3000)
            startActivity(Intent(this@SplashScreenActivity,MainActivity::class.java)).apply {
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
        }


    }

    private fun observer() {
        binding.apply {
            loginViewModel.apply {
                loader.observe(this@SplashScreenActivity){
                    when(it)
                    {
                        BaseViewModel.SHOW_LOADER->
                        {

                        }
                        BaseViewModel.HIDE_LOADER->
                        {

                        }

                    }
                }

                setUI.observe(this@SplashScreenActivity){
                    when(it)
                    {


                    }
                }

                error.observe(this@SplashScreenActivity)
                {
                    when(it.apiNo)
                    {

                    }
                }

            }

        }


    }

}