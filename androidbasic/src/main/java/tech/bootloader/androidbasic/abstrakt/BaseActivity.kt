package tech.bootloader.androidbasic.abstrakt;



import android.app.Dialog
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast

import androidx.appcompat.app.AppCompatActivity
import tech.bootloader.androidbasic.collector.ActivityCollector
import tech.bootloader.androidbasic.utils.ProgressDialogUtils


/**
 * activity 基类
 *
 * @author WL
 */
open class BaseActivity : AppCompatActivity() {
    private var progressDialog: Dialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityCollector.addActivity(this)

    }


    override fun onDestroy() {
        super.onDestroy()
        ActivityCollector.romoveActivity(this)
        closeProgressDialog()
    }

    fun toastMessageLong(message: String) {
        if (TextUtils.isEmpty(message))return
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
//        ToastUtils.show(message)
    }

    fun toastMessageShort(message: String) {
        if (TextUtils.isEmpty(message))return
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
//        ToastUtils.show(message)
    }


    fun showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = ProgressDialogUtils.createLoadingDialog(this@BaseActivity, "")
            progressDialog?.setCancelable(true)
        } else {
            if (!progressDialog!!.isShowing) {
                progressDialog!!.show()
            }
        }
    }

    fun closeProgressDialog() {
        ProgressDialogUtils.closeDialog(progressDialog)
    }



}
