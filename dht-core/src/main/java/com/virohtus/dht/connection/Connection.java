package com.virohtus.dht.connection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public class Connection {

    private static final Logger LOG = LoggerFactory.getLogger(Connection.class);
    private final ConnectionDelegate connectionDelegate;
    private final Socket socket;
    private final DataOutputStream dataOutputStream;
    private final DataInputStream dataInputStream;
    private final Future receiveFuture;

    public Connection(ConnectionDelegate connectionDelegate, ExecutorService executorService, Socket socket) throws IOException {
        this.connectionDelegate = connectionDelegate;
        this.socket = socket;
        this.dataInputStream = new DataInputStream(socket.getInputStream());
        this.dataOutputStream = new DataOutputStream(socket.getOutputStream());
        this.receiveFuture = executorService.submit(this::receive);
    }

    @Override
    public String toString() {
        byte[] addr = socket.getInetAddress().getAddress();
        return String.format("%d.%d.%d.%d:%d", addr[0], addr[1], addr[2], addr[3], socket.getPort());
    }

    public void send(byte[] data) throws IOException {
        dataOutputStream.writeInt(data.length);
        dataOutputStream.write(data);
        dataOutputStream.flush();
    }

    public void close() {
        receiveFuture.cancel(false);
    }

    private void receive() {
        Thread.currentThread().setName(Thread.currentThread().getName() + "-" + getClass().getSimpleName());
        try {
            while (!Thread.currentThread().isInterrupted()) {
                int dataLength = dataInputStream.readInt();
                byte[] data = new byte[dataLength];
                dataInputStream.readFully(data);
                connectionDelegate.dataReceived(data);
            }
        } catch(IOException e) {
            connectionDelegate.receiveDisrupted(e);
        } finally {
            try {
                dataOutputStream.close();
                dataInputStream.close();
                socket.close();
            } catch (IOException e) {
                LOG.error("error occurred when closing connection: " + e.getMessage());
            }
        }
    }
}
