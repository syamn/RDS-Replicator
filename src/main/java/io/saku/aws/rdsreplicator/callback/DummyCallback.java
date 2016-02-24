package io.saku.aws.rdsreplicator.callback;

/**
 * Created by sakura on 2016/02/24.
 */
public class DummyCallback extends BaseCallback {
    public void invoke() {
        System.out.println("Invoked DummyCallback!");
    }
}
