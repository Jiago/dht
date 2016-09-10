package com.virohtus.dht.node;

import com.virohtus.dht.connection.ConnectionDetails;
import com.virohtus.dht.event.Event;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;

public class PeerManager implements PeerDelegate {

    private static final Logger LOG = LoggerFactory.getLogger(PeerManager.class);
    private final ExecutorService executorService;
    private final PeerDelegate peerDelegate;
    private final Map<String, Peer> peers;

    public PeerManager(ExecutorService executorService, PeerDelegate peerDelegate) {
        this.executorService = executorService;
        this.peerDelegate = peerDelegate;
        this.peers = new HashMap<>();
    }

    @Override
    public void peerEventReceived(Peer peer, Event event) {
        peerDelegate.peerEventReceived(peer, event);
    }

    @Override
    public void peerDisconnected(Peer peer) {
        synchronized (peers) {
            peers.remove(peer.getId());
        }
        peerDelegate.peerDisconnected(peer);
    }

    public Peer createPeer(PeerType peerType, Socket socket) throws IOException {
        Peer peer = new Peer(this, executorService, peerType, socket);
        synchronized (peers) {
            peers.put(peer.getId(), peer);
        }
        return peer;
    }

    public Peer getPeerByConnectionDetails(ConnectionDetails connectionDetails) {
        throw new RuntimeException("havent solved this problem yet");
        /*
        synchronized (peers) {
            return peers.values()
                    .stream()
                    .filter(peer -> peer.getConnectionDetails().equals(connectionDetails))
                    .findFirst()
                    .get();
        }
        */
    }
}