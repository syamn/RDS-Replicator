package io.saku.aws.rdsreplicator.callback;

import io.saku.aws.rdsreplicator.request.IRequest;

/**
 * Created by sakura on 2016/02/24.
 */
public class DummyCallback extends BaseCallback {
    public void invoke(IRequest request) {
        System.out.println("Invoked DummyCallback!");
    }
}
