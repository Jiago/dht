package com.virohtus.dht.overlay.node;

import com.virohtus.dht.overlay.transport.ConnectionManager;
import com.virohtus.dht.overlay.transport.Server;

public abstract class OverlayNode implements ServerDelegate {

    private ConnectionManager connectionManager;
    private Server server;

    public OverlayNode(int serverPort) {
    }

    public void start() {
        server.start();
    }

    public void shutdown() {
        server.shutdown();
    }
}
