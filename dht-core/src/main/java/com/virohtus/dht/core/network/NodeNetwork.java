package com.virohtus.dht.core.network;

import com.virohtus.dht.core.event.EventSerializable;
import com.virohtus.dht.core.util.DhtInputStream;
import com.virohtus.dht.core.util.DhtOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class NodeNetwork implements EventSerializable {

    private static final Logger LOG = LoggerFactory.getLogger(NodeNetwork.class);
    private NodeIdentity predecessor;
    private final List<NodeIdentity> successors;
    private final Object lock;

    public NodeNetwork() {
        predecessor = null;
        successors = new ArrayList<>();
        lock = new Object();
    }

    public NodeNetwork(byte[] data) throws IOException {
        this();
        try (
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data);
            DhtInputStream inputStream = new DhtInputStream(byteArrayInputStream)
        ) {
            boolean hasPredecessor = inputStream.readBoolean();
            if(hasPredecessor) {
                predecessor = new NodeIdentity(inputStream.readSizedData());
            }
            int successorSize = inputStream.readInt();
            for(int i = 0; i < successorSize; i++) {
                successors.add(new NodeIdentity(inputStream.readSizedData()));
            }
        }
    }

    public boolean isEmpty() {
        synchronized (lock) {
            return !hasSuccessors() && !hasPredecessor();
        }
    }

    public boolean hasSuccessors() {
        synchronized (lock) {
            return !successors.isEmpty();
        }
    }

    public boolean hasPredecessor() {
        synchronized (lock) {
            return predecessor != null;
        }
    }

    public void setPredecessor(NodeIdentity nodeIdentity) {
        synchronized (lock) {
            predecessor = nodeIdentity;
        }
    }

    public Optional<NodeIdentity> getPredecessor() {
        return Optional.ofNullable(predecessor);
    }

    public void addSuccessor(NodeIdentity nodeIdentity) {
        synchronized (lock) {
            successors.add(nodeIdentity);
        }
    }

    public void removeSuccessor(NodeIdentity nodeIdentity) {
        synchronized (lock) {
            successors.remove(nodeIdentity);
        }
    }

    public List<NodeIdentity> getSuccessors() {
        synchronized (lock) {
            return new ArrayList<>(successors);
        }
    }

    public List<NodeIdentity> clearSuccessors() {
        synchronized (lock) {
            List<NodeIdentity> cleared = getSuccessors();
            successors.clear();
            return cleared;
        }
    }

    @Override
    public byte[] getBytes() throws IOException {
        try (
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            DhtOutputStream outputStream = new DhtOutputStream(byteArrayOutputStream)
        ) {
            outputStream.writeBoolean(predecessor != null);
            if (predecessor != null) {
                outputStream.writeSizedData(predecessor.getBytes());
            }
            outputStream.writeInt(successors.size());
            for (NodeIdentity nodeIdentity : successors) {
                outputStream.writeSizedData(nodeIdentity.getBytes());
            }
            outputStream.flush();
            return byteArrayOutputStream.toByteArray();
        }
    }
}
