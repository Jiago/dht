package com.virohtus.dht.core.network;

import com.virohtus.dht.core.event.EventSerializable;
import com.virohtus.dht.core.transport.connection.ConnectionInfo;
import com.virohtus.dht.core.util.DhtInputStream;
import com.virohtus.dht.core.util.DhtOutputStream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class NodeIdentity implements EventSerializable {

    private String nodeId;
    private ConnectionInfo connectionInfo;

    public NodeIdentity() {}

    public NodeIdentity(byte[] data) throws IOException {
        try (
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data);
            DhtInputStream inputStream = new DhtInputStream(byteArrayInputStream)
        ) {
            nodeId = inputStream.readString();
            connectionInfo = new ConnectionInfo(inputStream.readSizedData());
        }
    }

    public NodeIdentity(String nodeId, ConnectionInfo connectionInfo) {
        this.nodeId = nodeId;
        this.connectionInfo = connectionInfo;
    }

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    public ConnectionInfo getConnectionInfo() {
        return connectionInfo;
    }

    public void setConnectionInfo(ConnectionInfo connectionInfo) {
        this.connectionInfo = connectionInfo;
    }

    @Override
    public byte[] getBytes() throws IOException {
        try (
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            DhtOutputStream outputStream = new DhtOutputStream(byteArrayOutputStream)
        ) {
            outputStream.writeString(nodeId);
            outputStream.writeSizedData(connectionInfo.getBytes());
            outputStream.flush();
            return byteArrayOutputStream.toByteArray();
        }
    }
}
