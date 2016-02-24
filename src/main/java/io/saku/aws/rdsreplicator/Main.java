package io.saku.aws.rdsreplicator;

import java.io.IOException;

/**
 * Created by sakura on 2016/02/24.
 */
public class Main {
    public static void main(String[] args) throws IOException {

        final RDSReplicator replicator = new RDSReplicator();

        replicator.createReplica();

        System.out.println("completed");

    }
}
