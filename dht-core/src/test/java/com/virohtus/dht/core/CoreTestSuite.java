package com.virohtus.dht.core;

import com.virohtus.dht.core.peer.PeerTest;
import com.virohtus.dht.core.transport.TransportTestSuite;
import com.virohtus.dht.core.util.UtilTestSuite;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        PeerTest.class,
        TransportTestSuite.class,
        UtilTestSuite.class
})
public class CoreTestSuite {
}
