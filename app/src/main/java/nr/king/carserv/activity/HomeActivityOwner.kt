package nr.king.carserv.activity

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import nr.king.carserv.R
import nr.king.carserv.databinding.HomeActivityBinding
import nr.king.carserv.fragment.AccountFragment
import nr.king.carserv.fragment.BillingFragment
import nr.king.carserv.fragment.HomeFragment
import nr.king.carserv.fragment.ServiceFragment

@AndroidEntryPoint
class HomeActivityOwner:AppCompatActivity(),BottomNavigationView.OnNavigationItemSelectedListener {
    lateinit var binding: HomeActivityBinding
    var fragment:Fragment ?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =DataBindingUtil.setContentView(this,R.layout.home_activity)

        binding.apply {
            loadFragment(HomeFragment())
            menuIcon.setOnNavigationItemSelectedListener(this@HomeActivityOwner)
        }



    }


    fun loadFragment(fragment:Fragment)
    {
        binding.apply {
            val ft = supportFragmentManager.beginTransaction()
            ft.replace(R.id.nav_host_fragment_container, fragment, "CURR_FRAGMENT")
            ft.commit()
        }
    }

    override fun onNavigationItemSelected(it: MenuItem): Boolean {
        when(it.itemId)
        {
            R.id.dashBoard->
            {
                fragment = HomeFragment()
                loadFragment(fragment!!)
                return true
            }

            R.id.booking->
            {
                fragment = BillingFragment()
                loadFragment(fragment!!)
                return true
            }

            R.id.onService->
            {
                fragment = ServiceFragment()
                loadFragment(fragment!!)
                return true
            }

            R.id.account->
            {
                fragment = AccountFragment()
                loadFragment(fragment!!)
                return true
            }
        }
        return true
    }


}



