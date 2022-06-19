package nr.king.carserv.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import dagger.hilt.android.scopes.FragmentScoped
import nr.king.carserv.R
import nr.king.carserv.databinding.AccountServiceBinding
import nr.king.carserv.databinding.HomeFragmentBinding

@FragmentScoped
class AccountFragment:Fragment() {

    lateinit var homeFragment: AccountServiceBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeFragment = DataBindingUtil.inflate(inflater, R.layout.account_service,null,false)
        return homeFragment.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        homeFragment.apply {

        }
    }

}