package tech.bootloader.netbasic.functions;

/**
 * Created by Administrator on 2018/3/13.
 */

 public abstract class AbstractAction implements io.reactivex.functions.Action{
    @Override
    public void run() throws Exception {


        implementRun();
    }
    /**
     * Runs the action and optionally throws a checked exception.
     * @throws Exception if the implementation wishes to throw a checked exception
     */
    public void implementRun() throws Exception {

    }

}
