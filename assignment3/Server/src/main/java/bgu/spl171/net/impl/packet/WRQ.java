package bgu.spl171.net.impl.packet;

/**
 * Created by david on 1/13/17.
 */
public class WRQ extends Packet {

    private String filename;


    public WRQ(String filename) {
        setOpcode((short) 2);
        this.filename = filename;
    }

    public String getFilename() {
        return filename;
    }

}
