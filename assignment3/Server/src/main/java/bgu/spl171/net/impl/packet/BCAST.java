package bgu.spl171.net.impl.packet;

/**
 * Created by david on 1/13/17.
 */
public class BCAST extends Packet {

    private byte flag;
    private String filename;

    public BCAST(byte flag, String filename) {
        setOpcode((short) 9);
        this.filename = filename;
        this.flag = flag;
    }

    public String getFilename() {
        return filename;
    }

    public byte getFlag() {
        return flag;
    }


}
