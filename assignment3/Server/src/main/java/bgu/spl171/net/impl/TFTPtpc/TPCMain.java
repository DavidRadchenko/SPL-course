package bgu.spl171.net.impl.TFTPtpc;

import bgu.spl171.net.impl.TFTPreactor.EncoderDecoder;
import bgu.spl171.net.impl.TFTPreactor.TftpProtocol;
import bgu.spl171.net.srv.Server;

/**
 * Created by david on 1/10/17.
 */
public class TPCMain {
    public static void main(String[] args) {
        int port = Integer.parseInt(args[0]);
        System.out.println("Server start");
        Server s = Server.threadPerClient(
                port, //port
                () ->  new TftpProtocol(),//protocol factory
                () ->  new EncoderDecoder() //message encoder decoder factory
        );
        s.serve();
    }
}
