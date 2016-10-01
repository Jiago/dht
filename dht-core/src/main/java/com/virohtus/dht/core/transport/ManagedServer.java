package com.virohtus.dht.core.transport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.SocketAddress;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public class ManagedServer implements DhtServer {

    private static final Logger LOG = LoggerFactory.getLogger(ManagedServer.class);
    private final ServerDelegate serverDelegate;
    private final ExecutorService executorService;
    private final AsynchronousServerSocketChannel serverSocketChannel;

    public ManagedServer(ServerDelegate serverDelegate, ExecutorService executorService, SocketAddress socketAddress) throws IOException {
        this.serverDelegate = serverDelegate;
        this.executorService = executorService;
        AsynchronousChannelGroup group = AsynchronousChannelGroup.withCachedThreadPool(executorService, 3);
        serverSocketChannel = AsynchronousServerSocketChannel.open(group).bind(socketAddress);
    }

    @Override
    public Future serve() {
        return executorService.submit(() -> {
            try {
                while (!Thread.currentThread().isInterrupted()) {
                    Future<AsynchronousSocketChannel> future = serverSocketChannel.accept();
                    AsynchronousSocketChannel socketChannel = future.get();
                    serverDelegate.connectionOpened(socketChannel); //todo normalize into connection
                }
            } catch (Exception e) {
                LOG.warn("server socket accept disrupted: " + e.getMessage());
            }
            serverDelegate.serverShutdown();
        });
    }
}
