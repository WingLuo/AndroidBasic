package tech.bootloader.androidbasic.abstrakt

import android.app.Dialog
import android.text.TextUtils
import android.widget.Toast
import androidx.fragment.app.Fragment
import tech.bootloader.androidbasic.InterfaceInitializationView
import tech.bootloader.androidbasic.utils.ProgressDialogUtils


abstract class AbstractFragment : Fragment(), InterfaceInitializationView {
    private var progressDialog: Dialog? = null
    fun toastMessageLong(message: String) {
        if (TextUtils.isEmpty(message))return
        Toast.makeText(this.context, message, Toast.LENGTH_LONG).show()
//        ToastUtils.show(message)
    }

    fun toastMessageShort(message: String) {
        if (TextUtils.isEmpty(message))return
        Toast.makeText(this.context, message, Toast.LENGTH_SHORT).show()
//        ToastUtils.show(message)
    }

    fun showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = ProgressDialogUtils.createLoadingDialog(this.requireContext(), "")
        } else {
            if (!progressDialog!!.isShowing) {
                progressDialog!!.show()
            }
        }
    }

    fun closeProgressDialog() {
        ProgressDialogUtils.closeDialog(progressDialog)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        closeProgressDialog()
    }
}