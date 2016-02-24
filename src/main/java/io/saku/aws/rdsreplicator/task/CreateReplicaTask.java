package io.saku.aws.rdsreplicator.task;

import com.amazonaws.services.cognitoidentity.model.InternalErrorException;
import com.amazonaws.services.rds.AmazonRDS;
import com.amazonaws.services.rds.model.CreateDBInstanceReadReplicaRequest;
import com.amazonaws.services.rds.model.DBInstance;
import com.amazonaws.services.rds.model.DescribeDBInstancesRequest;
import com.amazonaws.services.rds.model.DescribeDBInstancesResult;
import io.saku.aws.rdsreplicator.callback.ICallback;
import io.saku.aws.rdsreplicator.checker.ChangesChecker;
import io.saku.aws.rdsreplicator.checker.CheckerResult;
import io.saku.aws.rdsreplicator.manager.SharedManager;
import io.saku.aws.rdsreplicator.request.CopyRequest;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

/**
 * Created by sakura on 2016/02/24.
 */
public class CreateReplicaTask extends BaseTask<CopyRequest> {
    final String ORIGINAL_DATABASE_IDENTIFIER = "awsclitest";

    public CreateReplicaTask(ICallback callback) {
        super(callback);
    }

    @Override
    public void processTask() {
        createReplica();
    }

    public void createReplica(){
        this.createReplica(this.ORIGINAL_DATABASE_IDENTIFIER, "copied-" + UUID.randomUUID());
    }

    public void createReplica(final String originalIdentifier, String replicaIdentifier){
        final AmazonRDS rds = SharedManager.getInstance().getRDS();

        // Check original resource
        final DescribeDBInstancesResult describeDBInstancesResult = rds.describeDBInstances(
                new DescribeDBInstancesRequest().withDBInstanceIdentifier(originalIdentifier)
        );
        if (describeDBInstancesResult.getDBInstances().size() != 1){
            throw new InternalErrorException("Specified original database " + originalIdentifier + " was not found.");
        }
        this.request.setOriginalRDS(describeDBInstancesResult.getDBInstances().get(0));

        // Create Read Replica branched from original resource
        final DBInstance replica = rds.createDBInstanceReadReplica(
                new CreateDBInstanceReadReplicaRequest()
                        .withSourceDBInstanceIdentifier(this.request.getOriginalRDS().getDBInstanceIdentifier())
                        .withDBInstanceIdentifier(replicaIdentifier)
        );
        this.request.setProcessingRDS(replica);

        System.out.printf("Replica");
        System.out.println(replica);

        // Waiting instance ready
        final Timer t = new Timer();
        final ChangesChecker<String> checker = new ChangesChecker<String>();
        final SimpleDateFormat sdf = new SimpleDateFormat("[yyyy-MM-dd HH:mm:ss.S] ");

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

                    if("available".equals(result.getNewValue())){
                        t.cancel();
                        done();
                    }
                }
            }
        }, 1000, 2000);
    }
}
