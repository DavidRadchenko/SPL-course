package bgu.spl171.net.impl.TFTPreactor;
import bgu.spl171.net.srv.Server;

/**
 * Created by david on 1/13/17.
 */
public class ReactorMain {
    public static void main(String[] args){
        int port = Integer.parseInt(args[0]);
        System.out.println("Server start");
        Server s = Server.reactor(
                Runtime.getRuntime().availableProcessors(),
                port, //port
                () ->  new TftpProtocol(),//protocol factory
                () ->  new EncoderDecoder() //message encoder decoder factory
        );
        s.serve();

    }

}

