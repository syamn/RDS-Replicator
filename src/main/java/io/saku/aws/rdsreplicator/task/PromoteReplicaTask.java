package io.saku.aws.rdsreplicator.task;

import com.amazonaws.services.cognitoidentity.model.InternalErrorException;
import com.amazonaws.services.rds.AmazonRDS;
import com.amazonaws.services.rds.model.DBInstance;
import com.amazonaws.services.rds.model.DescribeDBInstancesRequest;
import com.amazonaws.services.rds.model.DescribeDBInstancesResult;
import com.amazonaws.services.rds.model.PromoteReadReplicaRequest;
import io.saku.aws.rdsreplicator.callback.BaseCallback;
import io.saku.aws.rdsreplicator.checker.ChangesChecker;
import io.saku.aws.rdsreplicator.checker.CheckerResult;
import io.saku.aws.rdsreplicator.manager.SharedManager;
import io.saku.aws.rdsreplicator.request.CopyRequest;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by sakura on 2016/02/25.
 */
public class PromoteReplicaTask extends BaseTask<CopyRequest> {
    public PromoteReplicaTask(BaseCallback callback) {
        super(callback);
    }

    @Override
    public void processTask() {
        System.out.println("===== PromoteReplicaTask =====");

        final AmazonRDS rds = SharedManager.getInstance().getRDS();
        DBInstance response = rds.promoteReadReplica(
                new PromoteReadReplicaRequest()
                        .withDBInstanceIdentifier(this.request.getProcessingRDS().getDBInstanceIdentifier())
        );


        // Waiting instance ready
        final Timer t = new Timer();
        final ChangesChecker<String> checker = new ChangesChecker<String>();
        final SimpleDateFormat sdf = new SimpleDateFormat("[yyyy-MM-dd HH:mm:ss.S] ");
        final AtomicBoolean changedModifying = new AtomicBoolean(false);

        t.schedule(new TimerTask() {
            @Override
            public void run() {
                final DescribeDBInstancesResult response = rds.describeDBInstances(
                        new DescribeDBInstancesRequest().withDBInstanceIdentifier(request.getProcessingRDS().getDBInstanceIdentifier())
                );
                if (response.getDBInstances().size() != 1){
                    t.cancel();
                    throw new InternalErrorException("Copied instance is gone away..");
                }

                final String status = response.getDBInstances().get(0).getDBInstanceStatus();
                CheckerResult<String> result = checker.check(status);
                if (result.isChanged()){
                    System.out.println(sdf.format(new Date()) + "Instance changed to: " + String.valueOf(result.getNewValue()).toUpperCase()
                            + " (from: " + String.valueOf(result.getOldValue()).toUpperCase() + ")");

                    if (!changedModifying.get() && "modifying".equals(result.getNewValue())){
                        changedModifying.set(true);
                    }

                    if (changedModifying.get() && "available".equals(result.getNewValue())){
                        t.cancel();
                        done();
                    }
                }
            }
        }, 1000, 2000);
    }
}
