package com.virohtus.dht.core;

import com.virohtus.dht.core.event.EventHandler;
import com.virohtus.dht.core.network.GetDhtNetworkFailedException;
import com.virohtus.dht.core.network.NodeIdentity;
import com.virohtus.dht.core.network.NodeNetwork;
import com.virohtus.dht.core.network.event.GetDhtNetwork;
import com.virohtus.dht.core.peer.Peer;
import com.virohtus.dht.core.peer.PeerNotFoundException;
import com.virohtus.dht.core.transport.connection.ConnectionInfo;

import java.io.IOException;

public interface DhtNode {
    void start() throws IOException;
    void shutdown();
    void joinNetwork(ConnectionInfo existingNode) throws IOException;
    Peer openConnection(ConnectionInfo connectionInfo) throws IOException;
    void leaveNetwork();
    void registerEventHandler(EventHandler handler);
    void unregisterEventHandler(EventHandler handler);
    String getNodeId();
    Peer getPeer(String peerId) throws PeerNotFoundException;
    Peer getPeer(NodeIdentity nodeIdentity) throws PeerNotFoundException;
    ConnectionInfo getConnectionInfo();
    NodeIdentity getNodeIdentity();
    NodeNetwork getNodeNetwork();
    GetDhtNetwork getDhtNetwork() throws GetDhtNetworkFailedException;
}
