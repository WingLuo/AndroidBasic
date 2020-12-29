package tech.bootloader.netbasic.functions;



/**
 * @author luyong
 */

 public abstract class AbstractConsumer<T> implements io.reactivex.functions.Consumer<T> {
     private final String TAG = getClass().getSimpleName();

     @Override
     public void accept(T t) throws Exception {
         implementAccept(t);
     }

     /**
     * Consume the given value.
     * @param t the value
     * @throws Exception on error
     */
     public abstract  void implementAccept(T t);
}
