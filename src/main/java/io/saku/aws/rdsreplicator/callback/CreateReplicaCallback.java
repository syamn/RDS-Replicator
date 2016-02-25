package io.saku.aws.rdsreplicator.callback;

import io.saku.aws.rdsreplicator.request.CopyRequest;
import io.saku.aws.rdsreplicator.request.IRequest;
import io.saku.aws.rdsreplicator.task.PromoteReplicaTask;

/**
 * Created by sakura on 2016/02/25.
 */
public class CreateReplicaCallback extends BaseCallback<CopyRequest> {
    @Override
    public void invoke(CopyRequest request) {
        // Next, Promote processing instance
        final PromoteReplicaTask promoteReplicaTask = new PromoteReplicaTask(new DummyCallback());

        promoteReplicaTask.start(request);

    }
}
