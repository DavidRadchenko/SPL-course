package bgu.spl171.net.impl.packet;

/**
 * Created by david on 1/13/17.
 */
public class ACK extends Packet{

    private short blockNum;

    public ACK(short blockNum){
        setOpcode((short) 4);
        this.blockNum = blockNum;
    }

    public short getBlockNum() {
        return blockNum;
    }


}
