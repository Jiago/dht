package com.virohtus.dht.rest.node;

import com.virohtus.dht.connection.ConnectionDetails;
import com.virohtus.dht.node.Node;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/node")
public class NodeController {

    @Autowired private Node node;

    @RequestMapping(path = "", method = RequestMethod.GET)
    public Node getNode() {
        return node;
    }

}
