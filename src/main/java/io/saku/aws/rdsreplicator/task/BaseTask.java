package io.saku.aws.rdsreplicator.task;

import io.saku.aws.rdsreplicator.callback.ICallback;
import io.saku.aws.rdsreplicator.manager.SharedManager;
import io.saku.aws.rdsreplicator.request.IRequest;

/**
 * Created by sakura on 2016/02/24.
 */
abstract public class BaseTask<T extends IRequest>{
    final private ICallback callback;
    final private SharedManager manager;
    protected T request;

    public BaseTask(final ICallback callback){
        this.callback = callback;
        this.manager = SharedManager.getInstance();
    }

    public final T getRequest(){
        return request;
    }

    public final void start(T request){
        this.request = request;
        this.processTask();
    }

    protected abstract void processTask();

    protected final void done(){
        callback.invoke();
    }
}
