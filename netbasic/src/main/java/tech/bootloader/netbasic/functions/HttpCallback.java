package tech.bootloader.netbasic.functions;

public interface HttpCallback<T> {
    /**
     * 成功回调
     *
     * @param t
     */
    void onSuccess(T t);

    /**
     * 失败回调
     *
     * @param message
     */
    void onError(String message);
}
