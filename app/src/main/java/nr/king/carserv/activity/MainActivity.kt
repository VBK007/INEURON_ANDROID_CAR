package nr.king.carserv.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import dagger.hilt.android.AndroidEntryPoint
import nr.king.carserv.R
import nr.king.carserv.common.showToast
import nr.king.carserv.databinding.ActivityMainBinding
import nr.king.carserv.model.RegisterModel
import nr.king.carserv.viewModel.BaseViewModel
import nr.king.carserv.viewModel.LoginViewModel

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    lateinit var binding:ActivityMainBinding
    private val loginViewModel by viewModels<LoginViewModel>()
 lateinit   var  bottomSheet: RegisterBottomSheet
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =DataBindingUtil.setContentView(this,R.layout.activity_main)
        binding.apply { 
          observer()
            signInbtn.setOnClickListener {
                callSignInBottomSheeet()
            }
            registerAccount.setOnClickListener {
                initBottomSheet()
            }

        }
    }

    private fun callSignInBottomSheeet() {
     val bottomSheet = BottomDialogFragment(this)
     {
         loginViewModel.doLogin(
             RegisterModel(
                 userName = it.userName,
                 password = it.password
             )
         )
     }
        bottomSheet.show(supportFragmentManager, BottomDialogFragment.TAG)
    }


    private fun observer() {
        binding.apply {
            loginViewModel.apply {
                loader.observe(this@MainActivity){
                    when(it)
                    {
                        BaseViewModel.SHOW_LOADER->
                        {

                            window.setFlags(
                                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                            )
                            binding.apply {
                                llStoreView.alpha = 0.5F
                                progressBar.visibility = View.VISIBLE
                            }
                        }
                        BaseViewModel.HIDE_LOADER->
                        {

                                binding.apply {
                                    llStoreView.alpha = 1F
                                    progressBar.visibility = View.GONE
                                }
                                window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)


                        }

                    }
                }

                setUI.observe(this@MainActivity){
                    when(it)
                    {
                       LoginViewModel.MOVE_TO_REGISTER_PICKUP->{
                           bottomSheet.dismiss()
                           showToast("User Creted Succesfully...!")
                           apiRegisterResponse.value.apply {
                               preferenceManager.setUserId(this?.payload?.id?:"")
                           }
                       }
                        LoginViewModel.MOVE_TO_HOME->
                        {
                            showToast("Login Successfully")
                            startActivity(Intent(this@MainActivity,HomeActivityOwner::class.java)).apply {
                                intent.flags=Intent.FLAG_ACTIVITY_NEW_TASK
                            }
                        }
                        LoginViewModel.MOVE_TO_HOME_USER->{
                            showToast("Login Successfully")
                            startActivity(Intent(this@MainActivity,HomeActivityOwner::class.java)).apply {
                                intent.flags=Intent.FLAG_ACTIVITY_NEW_TASK
                            }
                        }

                    }
                }

                error.observe(this@MainActivity)
                {
                    when(it.apiNo)
                    {

                    }
                }

            }

        }




    }

    private fun initBottomSheet() {
         bottomSheet = RegisterBottomSheet(this)
        {
            loginViewModel.doRegister(
             it
            )
        }
        bottomSheet.show(supportFragmentManager, RegisterBottomSheet.TAG)
    }


}