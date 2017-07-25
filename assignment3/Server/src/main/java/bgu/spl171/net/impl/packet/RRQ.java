package bgu.spl171.net.impl.packet;

/**
 * Created by david on 1/13/17.
 */
public class RRQ extends Packet {

    private String filename;


    public RRQ(String filename) {
        setOpcode((short) 1);
        this.filename = filename;
    }

    public String getFilename() {
        return filename;
    }

}
