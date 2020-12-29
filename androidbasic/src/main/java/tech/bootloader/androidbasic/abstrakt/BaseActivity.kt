package tech.bootloader.androidbasic.abstrakt;



import android.app.Dialog
import android.os.Bundle
import android.widget.Toast

import androidx.appcompat.app.AppCompatActivity
import tech.bootloader.androidbasic.collector.ActivityCollector


/**
 * activity 基类
 *
 * @author WL
 */
open class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityCollector.addActivity(this)

    }


    override fun onDestroy() {
        super.onDestroy()
        ActivityCollector.romoveActivity(this);
    }

    fun toastMessageLong(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
//        ToastUtils.show(message)
    }

    fun toastMessageShort(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
//        ToastUtils.show(message)
    }





}
