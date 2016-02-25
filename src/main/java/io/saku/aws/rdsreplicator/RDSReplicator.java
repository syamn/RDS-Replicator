package io.saku.aws.rdsreplicator;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.EnvironmentVariableCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.cognitoidentity.model.InternalErrorException;
import com.amazonaws.services.rds.AmazonRDS;
import com.amazonaws.services.rds.AmazonRDSClient;
import com.amazonaws.services.rds.model.CreateDBInstanceReadReplicaRequest;
import com.amazonaws.services.rds.model.DBInstance;
import com.amazonaws.services.rds.model.DescribeDBInstancesRequest;
import com.amazonaws.services.rds.model.DescribeDBInstancesResult;
import io.saku.aws.rdsreplicator.callback.CreateReplicaCallback;
import io.saku.aws.rdsreplicator.callback.DummyCallback;
import io.saku.aws.rdsreplicator.request.CopyRequest;
import io.saku.aws.rdsreplicator.request.IRequest;
import io.saku.aws.rdsreplicator.task.CreateReplicaTask;

import java.util.UUID;

/**
 * Created by sakura on 2016/02/24.
 */
public class RDSReplicator {

    public RDSReplicator(){

    }

    public void createCopy(){
        final CopyRequest request = new CopyRequest();

        final CreateReplicaTask createReplicaTask = new CreateReplicaTask(new CreateReplicaCallback());
        createReplicaTask.start(request);


    }


}
