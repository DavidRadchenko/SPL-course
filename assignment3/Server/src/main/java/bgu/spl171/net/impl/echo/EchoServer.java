package bgu.spl171.net.impl.echo;

import bgu.spl171.net.srv.Server;

/**
 * Created by david on 1/10/17.
 */
public class EchoServer {
    public static void main(String[] args){
        System.out.println("Server start");
        Server s = Server.threadPerClient(
                7777, //port
                () ->  new EchoProtocol() ,//protocol factory
                () ->  new LineMessageEncoderDecoder() //message encoder decoder factory
                );
        s.serve();
    }
}
