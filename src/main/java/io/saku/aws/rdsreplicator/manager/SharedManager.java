package io.saku.aws.rdsreplicator.manager;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.EnvironmentVariableCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.rds.AmazonRDS;
import com.amazonaws.services.rds.AmazonRDSClient;

/**
 * Created by sakura on 2016/02/24.
 */
public class SharedManager {
    private static SharedManager instance = null;

    final private AmazonRDS rds;

    private SharedManager(){
        // Create RDS client Instance
        final AWSCredentialsProvider providor = new EnvironmentVariableCredentialsProvider();
        this.rds = Region.getRegion(Regions.AP_NORTHEAST_1).createClient(AmazonRDSClient.class, providor, new ClientConfiguration());
    }

    public static SharedManager getInstance(){
        if (instance == null){
            synchronized (SharedManager.class) {
                if (instance == null){
                    instance = new SharedManager();
                }
            }
        }
        return instance;
    }
    public static void dispose(){
        instance = null;
    }

    public AmazonRDS getRDS(){
        return this.rds;
    }

}
