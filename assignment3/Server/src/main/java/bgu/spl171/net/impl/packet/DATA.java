package bgu.spl171.net.impl.packet;

/**
 * Created by david on 1/13/17.
 */
public class DATA extends Packet {
    private short packetSize;
    private short blockNum;
    private byte[] data;

    public DATA(short packetSize ,short blockNum, byte[] data){
        setOpcode((short) 3);
        this.packetSize=packetSize;
        this.blockNum=blockNum;
        this.data=data;
    }

    public byte[] getData(){
        return data;
    }
    public short getBlockNum() {
        return blockNum;
    }
    public short getPacketSize(){
        return packetSize;
    }

}
