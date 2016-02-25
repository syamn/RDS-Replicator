package io.saku.aws.rdsreplicator.callback;

import io.saku.aws.rdsreplicator.request.IRequest;

/**
 * Created by sakura on 2016/02/24.
 */
abstract public class BaseCallback<T extends IRequest> {
    abstract public void invoke(T request);
}
