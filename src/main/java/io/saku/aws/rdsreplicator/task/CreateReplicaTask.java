package io.saku.aws.rdsreplicator.task;

import com.amazonaws.services.cognitoidentity.model.InternalErrorException;
import com.amazonaws.services.rds.AmazonRDS;
import com.amazonaws.services.rds.model.CreateDBInstanceReadReplicaRequest;
import com.amazonaws.services.rds.model.DBInstance;
import com.amazonaws.services.rds.model.DescribeDBInstancesRequest;
import com.amazonaws.services.rds.model.DescribeDBInstancesResult;
import io.saku.aws.rdsreplicator.callback.ICallback;
import io.saku.aws.rdsreplicator.manager.SharedManager;

import java.util.UUID;

/**
 * Created by sakura on 2016/02/24.
 */
public class CreateReplicaTask extends BaseTask {
    final String ORIGINAL_DATABASE_IDENTIFIER = "";

    public CreateReplicaTask(ICallback callback) {
        super(callback);
    }

    @Override
    public void start() {
        createReplica();
        done();
    }

    public DBInstance createReplica(){
        return this.createReplica(this.ORIGINAL_DATABASE_IDENTIFIER, "copied-" + UUID.randomUUID());
    }

    public DBInstance createReplica(final String originalIdentifier, String replicaIdentifier){
        final AmazonRDS rds = SharedManager.getInstance().getRDS();

        // Check original resource
        final DescribeDBInstancesResult describeDBInstancesResult = rds.describeDBInstances(
                new DescribeDBInstancesRequest().withDBInstanceIdentifier(originalIdentifier)
        );
        if (describeDBInstancesResult.getDBInstances().size() != 1){
            throw new InternalErrorException("Specified original database " + originalIdentifier + " was not found.");
        }
        final DBInstance original = describeDBInstancesResult.getDBInstances().get(0);

        // Create Read Replica branched from original resource
        final DBInstance replica = rds.createDBInstanceReadReplica(
                new CreateDBInstanceReadReplicaRequest()
                        .withSourceDBInstanceIdentifier(original.getDBInstanceIdentifier())
                        .withDBInstanceIdentifier(replicaIdentifier)
        );
        System.out.printf("Replica");
        System.out.println(replica);

        return replica;
    }
}
