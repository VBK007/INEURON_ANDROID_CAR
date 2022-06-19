package nr.king.carserv.activity

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import nr.king.carserv.R
import nr.king.carserv.common.showToast
import nr.king.carserv.databinding.LoginScreenBinding
import nr.king.carserv.model.RegisterModel
import nr.king.carserv.viewModel.BaseViewModel
import nr.king.carserv.viewModel.LoginViewModel


class BottomDialogFragment(
    context: Context,
    var addNewNumber: ((registerModel: RegisterModel) -> Unit)
) : BottomSheetDialogFragment() {
    private var backIcon: ImageView? = null
    private var imageView: ImageView? = null
    lateinit var loginScreenBinding: LoginScreenBinding
    var mContext = context

    @Nullable
    override fun onCreateView(
        @NonNull inflater: LayoutInflater,
        @Nullable container: ViewGroup?,
        @Nullable savedInstanceState: Bundle?
    ): View {
        loginScreenBinding = DataBindingUtil.inflate<LoginScreenBinding>(
            layoutInflater,
            R.layout.login_screen,
            null,
            false
        )
        loginScreenBinding.apply {
            backIcon.setOnClickListener {
                dismiss()
            }
            setFunctionforLayout()
        }
        return loginScreenBinding.root
    }

    private fun setFunctionforLayout() {
        loginScreenBinding.apply {
            signInbtn.setOnClickListener {
                if (editTextPhoneemail.text.toString().isEmpty() || edtTxtPassword.text.toString()
                        .isEmpty()
                ) {
                    showToast("Please Fill all the Fields")
                } else {
                    addNewNumber.invoke(
                        RegisterModel(
                            userName = editTextPhoneemail.text.toString(),
                            password = editTextPhoneemail.text.toString()
                        )
                    )
                }

            }

            edtTxtPassword.addTextChangedListener {
                if (it.toString().isNotEmpty() && editTextPhoneemail.text.toString().isNotEmpty()) {
                    signInbtn.alpha = 1f
                }
            }

            edtTxtPassword.setOnFocusChangeListener { view, b ->
                if (editTextPhoneemail.text.toString()
                        .isNotEmpty() && edtTxtPassword.text.toString().isNotEmpty()
                ) {
                    signInbtn.alpha = 1f
                }
            }
        }


    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.setOnShowListener { dialogInterface ->
            val bottomSheetDialog = dialogInterface as BottomSheetDialog
            setupFullHeight(bottomSheetDialog)
        }
        return dialog
    }


    private fun setupFullHeight(bottomSheetDialog: BottomSheetDialog) {
        val bottomSheet =
            bottomSheetDialog.findViewById<View>(R.id.design_bottom_sheet) as FrameLayout?
        val behavior: BottomSheetBehavior<*> = BottomSheetBehavior.from(bottomSheet!!)
        val layoutParams = bottomSheet!!.layoutParams
        val windowHeight = getWindowHeight()
        if (layoutParams != null) {
            layoutParams.height = windowHeight
        }
        bottomSheet.layoutParams = layoutParams
        behavior.state = BottomSheetBehavior.STATE_EXPANDED
    }

    private fun getWindowHeight(): Int {
        // Calculate window height for fullscreen use
        val displayMetrics = DisplayMetrics()
        (context as Activity?)?.windowManager?.defaultDisplay?.getMetrics(displayMetrics)
        return displayMetrics.heightPixels
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onDetach() {
        super.onDetach()

    }


    companion object {
        const val TAG = "EmailOrdersDialogFrag"

        fun newInstance(
            context: Context,
            addNewNumber: ((registerModel: RegisterModel) -> Unit)
        ): BottomDialogFragment {
            //this.context = context;
            return BottomDialogFragment(context, addNewNumber)
        }
    }
}