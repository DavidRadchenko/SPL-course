package bgu.spl171.net.api.bidi;

import bgu.spl171.net.srv.ConnectionHandler;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by david on 1/10/17.
 */
public class ConnectionsImpl<T> implements Connections<T> {

    private int counter;
    private ConcurrentHashMap<Integer, ConnectionHandler<T>> map;

    public ConnectionsImpl() {
        counter = 0;
        map = new ConcurrentHashMap<Integer, ConnectionHandler<T>>();
    }

    @Override
    public boolean send(int connectionId, Object msg) {
        ConnectionHandler<T> tmp = (ConnectionHandler) map.get(connectionId);
        if(tmp != null) {
            tmp.send((T) msg);
            return true;
        }
        return false;
    }

    @Override
    public void broadcast(Object msg) {
        for (ConnectionHandler<T> ch: map.values())
            ch.send((T) msg);
    }

    @Override
    public void disconnect(int connectionId) {
        try {
            map.get(connectionId).close();
        }
        catch (IOException e) {}
        map.remove(connectionId);
    }

    public int getCount() {
        return counter;
    }

    // add a new connection to the connections database
    public void newConnection(ConnectionHandler ch, int connectionId) {
        map.putIfAbsent(connectionId, ch);
        counter++;
    }

}
