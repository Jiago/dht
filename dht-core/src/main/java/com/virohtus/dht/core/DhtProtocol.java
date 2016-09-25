package com.virohtus.dht.core;

public interface DhtProtocol {

    String STRING_ENCODING = "UTF-8";
    long NODE_TIMEOUT = 10000;
    long STABILIZATION_PERIOD = 2000;

    int SERVER_START = 1;
    int SERVER_SHUTDOWN = 2;
    int SOCKET_CONNECT = 3;

    int PEER_CONNECTED = 4;
    int PEER_DISCONNECTED = 5;

    int NODE_IDENTITY_REQUEST = 6;
    int NODE_IDENTITY_RESPONSE = 7;

    int GET_DHT_NETWORK = 8;
    int GET_PREDECESSOR_REQUEST = 9;
    int GET_PREDECESSOR_RESPONSE = 10;
    int SET_PREDECESSOR_REQUEST = 11;
    int PREDECESSOR_DIED = 12;
    int GET_NODE_NETWORK_REQUEST = 13;
    int GET_NODE_NETWORK_RESPONSE = 14;
}
