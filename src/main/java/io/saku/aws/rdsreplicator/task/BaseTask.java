package io.saku.aws.rdsreplicator.task;

import io.saku.aws.rdsreplicator.callback.ICallback;
import io.saku.aws.rdsreplicator.manager.SharedManager;

/**
 * Created by sakura on 2016/02/24.
 */
abstract public class BaseTask implements ITask{
    final private ICallback callback;
    final private SharedManager manager;

    public BaseTask(final ICallback callback){
        this.callback = callback;
        this.manager = SharedManager.getInstance();
    }

    public abstract void start();

    protected final void done(){
        callback.invoke();
    }
}
