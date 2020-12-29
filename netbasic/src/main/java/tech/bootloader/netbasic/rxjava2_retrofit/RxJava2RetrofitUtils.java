package tech.bootloader.netbasic.rxjava2_retrofit;

/**
 * @author  by Administrator on 2017/7/6.
 */

public class RxJava2RetrofitUtils extends BaseRxJava2RetrofitImp {
    private RxJava2RetrofitUtils(){
        super();

    }
    public static RxJava2RetrofitUtils getInstance() {
        return RxJava2_RetrofitUtilsInnerHolder.instance;
    }


    private static class RxJava2_RetrofitUtilsInnerHolder {
        public static RxJava2RetrofitUtils instance = new RxJava2RetrofitUtils();
    }
}
