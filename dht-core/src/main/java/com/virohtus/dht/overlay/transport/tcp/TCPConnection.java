package com.virohtus.dht.overlay.transport.tcp;

import com.virohtus.dht.event.EventFactory;
import com.virohtus.dht.overlay.node.ConnectionDelegate;
import com.virohtus.dht.overlay.transport.Connection;
import com.virohtus.dht.overlay.transport.ConnectionType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class TCPConnection extends Connection {

    private static final Logger LOG = LoggerFactory.getLogger(TCPConnection.class);
    private final DataOutputStream dataOutputStream;
    private final DataInputStream dataInputStream;
    private final EventFactory eventFactory;
    private Thread receiverThread;

    public TCPConnection(ConnectionType connectionType, ConnectionDelegate delegate, Socket socket) throws IOException {
        super(connectionType, delegate, socket);
        this.dataOutputStream = new DataOutputStream(socket.getOutputStream());
        this.dataInputStream = new DataInputStream(socket.getInputStream());
        this.eventFactory = EventFactory.getInstance();
        receive();
    }

    @Override
    public void send(byte[] data) throws IOException {
        dataOutputStream.writeInt(data.length);
        dataOutputStream.write(data, 0, data.length);
        dataOutputStream.flush();
    }

    @Override
    public void close() {
        try {
            receiverThread.interrupt();
            dataInputStream.close();
            dataOutputStream.close();
            socket.close();
            receiverThread.join();
        } catch (IOException e) {
            LOG.error("Error on TCPConnection close: " + e.getMessage());
        } catch (InterruptedException e) {
            receiverThread.interrupt();
        }
    }

    public boolean isAlive() {
        return receiverThread != null && receiverThread.isAlive();
    }

    private void receive() {
        if(isAlive()) {
            return;
        }

        receiverThread = new Thread(() -> {
            try {
                while (!Thread.currentThread().isInterrupted()) {
                    int dataLength = dataInputStream.readInt();
                    byte[] data = new byte[dataLength];
                    dataInputStream.readFully(data);
                    delegate.onEvent(getId(), eventFactory.createEvent(data));
                }
            } catch(Exception e) {
                if(!Thread.currentThread().isInterrupted()) {
                    delegate.onEvent(getId(), new ReceiveError(e));
                }
            }
        });
        receiverThread.start();
    }
}
