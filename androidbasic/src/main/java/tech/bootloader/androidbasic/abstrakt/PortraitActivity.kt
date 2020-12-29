package tech.bootloader.androidbasic.abstrakt


import android.content.Context
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.os.IBinder
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import tech.bootloader.androidbasic.InterfaceInitializationView
import tech.bootloader.androidbasic.abstrakt.BaseActivity


/**
 * 启动页
 */
abstract class PortraitActivity : BaseActivity(), InterfaceInitializationView {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //竖屏
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
//            window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                    or View.SYSTEM_UI_FLAG_LAYOUT_STABLE)
//            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
//            window.statusBarColor = Color.TRANSPARENT
//        }

        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)

    }

    // 点击空白区域 自动隐藏软键盘
    override fun onTouchEvent(ev: MotionEvent): Boolean {
        if (ev.action == MotionEvent.ACTION_DOWN) {
            val v = currentFocus
            val imm =
                getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            if (ev.action == MotionEvent.ACTION_DOWN) {
                if (v != null) {
                    if (v.windowToken != null) {
                        imm.hideSoftInputFromWindow(
                            v.windowToken,
                            InputMethodManager.HIDE_NOT_ALWAYS
                        )
                    }
                }
            }
        }
        return super.onTouchEvent(ev)
    }

    override fun dispatchTouchEvent(me: MotionEvent): Boolean {
        if (me.action == MotionEvent.ACTION_DOWN) {  //把操作放在用户点击的时候
            val v = currentFocus //得到当前页面的焦点,ps:有输入框的页面焦点一般会被输入框占据
            if (isShouldHideKeyboard(v, me)) { //判断用户点击的是否是输入框以外的区域
                hideKeyboard(v!!.windowToken) //收起键盘
            }
        }
        return super.dispatchTouchEvent(me)
    }

    /**
     * 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘，因为当用户点击EditText时则不能隐藏
     *
     * @param v
     * @param event
     * @return
     */
    private  fun isShouldHideKeyboard(
        v: View?,
        event: MotionEvent
    ): Boolean {
        if (v != null && v is EditText) {  //判断得到的焦点控件是否包含EditText
            val l = intArrayOf(0, 0)
            v.getLocationInWindow(l)
            val left = l[0]
            //得到输入框在屏幕中上下左右的位置
            val top = l[1]
            val bottom = top + v.height
            val right = left + v.width
            return if (event.x > left && event.x < right && event.y > top && event.y < bottom
            ) {
                // 点击位置如果是EditText的区域，忽略它，不收起键盘。
                false
            } else {
                true
            }
        }
        // 如果焦点不是EditText则忽略
        return false
    }

    /**
     * 获取InputMethodManager，隐藏软键盘
     * @param token
     */
    private  fun hideKeyboard(token: IBinder?) {
        if (token != null) {
            val im =
                getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            im.hideSoftInputFromWindow(
                token,
                InputMethodManager.HIDE_NOT_ALWAYS
            )
        }
    }
}
