package tech.bootloader.androidbasic

/**
 * UI初始化接口申明  标志
 */
interface InterfaceInitializationView {


    /**
     *This method is not called
     *
     */
    fun initPresenter()

    /**
     * 初始化视图
     * should use after initPresenter()
     * This method is not called
     */
    fun initView()

    /**
     * 初始化数据
     * should use after initView()
     *This method is not called
     */
    fun initData()
}