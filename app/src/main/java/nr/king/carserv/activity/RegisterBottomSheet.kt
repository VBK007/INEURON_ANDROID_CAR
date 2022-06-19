package nr.king.carserv.activity

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.databinding.DataBindingUtil
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.messaging.FirebaseMessaging
import nr.king.carserv.R
import nr.king.carserv.common.showToast
import nr.king.carserv.databinding.RegisterLayoutBinding
import nr.king.carserv.model.Role
import nr.king.carserv.model.UserModel

class RegisterBottomSheet(
    context: Context,
    var addNewNumber: ((registerModel: UserModel) -> Unit)
) : BottomSheetDialogFragment() {
    private var backIcon: ImageView? = null
    private var imageView: ImageView? = null
    lateinit var loginScreenBinding: RegisterLayoutBinding
    var mContext = context

    @Nullable
    override fun onCreateView(
        @NonNull inflater: LayoutInflater,
        @Nullable container: ViewGroup?,
        @Nullable savedInstanceState: Bundle?
    ): View {
        loginScreenBinding = DataBindingUtil.inflate<RegisterLayoutBinding>(
            layoutInflater,
            R.layout.register_layout,
            null,
            false
        )
        loginScreenBinding.apply {
            backIcon.setOnClickListener {
                dismiss()
            }

        }
        return loginScreenBinding.root
    }

    private fun setFunctionforLayout() {
        loginScreenBinding.apply {
            registerBtm.setOnClickListener {
                if (editTextPhoneemail.text.toString().isEmpty() || edtTxtPassword.text.toString()
                        .isEmpty() || edtFname.text.toString().isEmpty()
                    || edttxtPhone.text.toString().isEmpty()
                ) {
                    showToast("Please Fill all the Fields")
                } else {
                    FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
                        Log.e(TAG, "setFunctionforLayout: ${task.result}", )
                        if (task.isSuccessful) {
                            addNewNumber.invoke(
                                UserModel(
                                    firstName = edtFname.text.toString(),
                                    lastName = editTextPhoneemail.text.toString(),
                                    mobileNumber = edttxtPhone.text.toString(),
                                    password = edttxtPhone.text.toString(),
                                    username = edttxtPhone.text.toString(),
                                    role = Role(
                                        id = if (userCheckBox.isChecked) 1 else 2,
                                        name = if (userCheckBox.isChecked) "SUPER_ADMIN" else "customer",
                                        description = if (userCheckBox.isChecked) "SUPER_ADMIN" else "customer"
                                    ),
                                    fcmKey =task.result
                                )
                            )

                        }
                    })



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
        setFunctionforLayout()
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
            addNewNumber: (registerModel: UserModel) -> Unit
        ): RegisterBottomSheet {
            //this.context = context;
            return RegisterBottomSheet(context, addNewNumber)
        }
    }
}