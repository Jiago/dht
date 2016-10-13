package com.virohtus.dht.core;

import com.virohtus.dht.core.network.NodeIdentity;

import java.io.IOException;
import java.net.SocketAddress;
import java.util.concurrent.ExecutionException;

public interface DhtNode {
    void start() throws ExecutionException, InterruptedException, IOException;
    void shutdown();
    void joinNetwork(SocketAddress socketAddress) throws IOException, InterruptedException;
    NodeIdentity getNodeIdentity();
}
