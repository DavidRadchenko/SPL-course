package bgu.spl171.net.impl.echo;

import bgu.spl171.net.api.MessagingProtocol;
import bgu.spl171.net.api.bidi.BidiMessagingProtocol;
import bgu.spl171.net.api.bidi.Connections;

import java.time.LocalDateTime;

public class EchoProtocol implements BidiMessagingProtocol<String> {

    private boolean shouldTerminate = false;
    private int connectionId;
    private Connections<String> connections;

    /*
    initiate the protocol with the active connections structure of
    the server and saves the owner client’s connection id.
     */
    @Override
    public void start(int connectionId, Connections<String> connections) {
        this.connections = connections;
        this.connectionId = connectionId;
    }

    @Override
    public void process(String msg) {
        shouldTerminate = "bye".equals(msg);
        String msg1 =" Fuckin bullshit server " + msg;
        connections.send(connectionId, msg1);
    }

    @Override
    public boolean shouldTerminate() {
        return shouldTerminate;
    }
}
