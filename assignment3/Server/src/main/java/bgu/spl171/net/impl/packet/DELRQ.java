package bgu.spl171.net.impl.packet;

/**
 * Created by david on 1/14/17.
 */
public class DELRQ extends Packet{

    private String filename;


    public DELRQ(String filename) {
        setOpcode((short) 8);
        this.filename = filename;
    }

    public String getFilename() {
        return filename;
    }

}


