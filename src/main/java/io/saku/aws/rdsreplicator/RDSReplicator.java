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

import java.util.UUID;

/**
 * Created by sakura on 2016/02/24.
 */
public class RDSReplicator {
    final String ORIGINAL_DATABASE_IDENTIFIER = "";
    final AmazonRDS rds;

    public RDSReplicator(){
        // Create RDS client Instance
        final AWSCredentialsProvider providor = new EnvironmentVariableCredentialsProvider();
        this.rds = Region.getRegion(Regions.AP_NORTHEAST_1).createClient(AmazonRDSClient.class, providor, new ClientConfiguration());
    }

    public DBInstance createReplica(){
        return this.createReplica(this.ORIGINAL_DATABASE_IDENTIFIER, "copied-" + UUID.randomUUID());
    }

    public DBInstance createReplica(final String originalIdentifier, String replicaIdentifier){
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
