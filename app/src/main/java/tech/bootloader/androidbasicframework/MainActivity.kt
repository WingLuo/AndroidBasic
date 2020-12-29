package tech.bootloader.androidbasicframework

import android.os.Bundle
import tech.bootloader.androidbasic.abstrakt.PortraitActivity
import tech.bootloader.netbasic.rxjava2_retrofit.RxJava2RetrofitUtils

class MainActivity : PortraitActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initPresenter()
    }

    override fun initPresenter() {
        RxJava2RetrofitUtils.getInstance()

    }

    override fun initView() {

    }

    override fun initData() {

    }
}