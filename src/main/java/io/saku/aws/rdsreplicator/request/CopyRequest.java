package io.saku.aws.rdsreplicator.request;

import com.amazonaws.services.rds.AmazonRDS;
import com.amazonaws.services.rds.model.DBInstance;

/**
 * Created by sakura on 2016/02/25.
 */
public class CopyRequest implements IRequest{
    private DBInstance originalRds = null;
    private DBInstance processingRds = null;

    public CopyRequest setOriginalRDS(final DBInstance rds){
        this.originalRds = rds;
        return this;
    }

    public DBInstance getOriginalRDS(){
        return this.originalRds;
    }

    public CopyRequest setProcessingRDS(final DBInstance rds){
        this.processingRds = rds;
        return this;
    }

    public DBInstance getProcessingRDS(){
        return this.processingRds;
    }
}
