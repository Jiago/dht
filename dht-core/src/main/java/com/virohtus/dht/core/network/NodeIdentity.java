package com.virohtus.dht.core.network;

import com.virohtus.dht.core.action.Wireable;
import com.virohtus.dht.core.transport.io.DhtInputStream;
import com.virohtus.dht.core.transport.io.DhtOutputStream;
import com.virohtus.dht.core.transport.protocol.DhtProtocol;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;

public class NodeIdentity implements Wireable {

    private String nodeId;
    private SocketAddress socketAddress;

    public NodeIdentity(String nodeId, SocketAddress socketAddress) {
        this.nodeId = nodeId;
        this.socketAddress = socketAddress;
    }

    public NodeIdentity(NodeIdentity other) {
        this.nodeId = other.getNodeId();
        this.socketAddress = other.getSocketAddress();
    }

    public NodeIdentity(DhtInputStream inputStream) throws IOException {
        fromWire(inputStream);
    }

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    public SocketAddress getSocketAddress() {
        return socketAddress;
    }

    public void setSocketAddress(SocketAddress socketAddress) {
        this.socketAddress = socketAddress;
    }

    @Override
    public void toWire(DhtOutputStream outputStream) throws IOException {
        outputStream.writeSizedData(nodeId.getBytes(DhtProtocol.STRING_ENCODING));
        if(!(socketAddress instanceof InetSocketAddress)) {
            throw new IllegalArgumentException("have not implemented serialization for non InetSocketAddress types!");
        }
        InetSocketAddress inetSocketAddress = (InetSocketAddress) socketAddress;
        outputStream.writeSizedData(DhtProtocol.HOSTNAME.getBytes(DhtProtocol.STRING_ENCODING));
        outputStream.writeInt(inetSocketAddress.getPort());
    }

    @Override
    public void fromWire(DhtInputStream inputStream) throws IOException {
        nodeId = new String(inputStream.readSizedData(), DhtProtocol.STRING_ENCODING);
        String hostname = new String(inputStream.readSizedData(), DhtProtocol.STRING_ENCODING);
        int port = inputStream.readInt();
        socketAddress = new InetSocketAddress(hostname, port);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NodeIdentity that = (NodeIdentity) o;

        return nodeId.equals(that.nodeId);

    }

    @Override
    public int hashCode() {
        return nodeId.hashCode();
    }
}
