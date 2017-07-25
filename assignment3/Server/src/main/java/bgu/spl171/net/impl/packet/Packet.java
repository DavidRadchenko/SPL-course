package bgu.spl171.net.impl.packet;

import bgu.spl171.net.impl.TFTPreactor.EncoderDecoder;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * Created by david on 1/13/17.
 */
public abstract class Packet {

    protected short opcode;

    public void setOpcode(short n) {
        opcode = n;
    }
    public short extractOpcode(byte[] byteArr) {
        short res = byteToShort(byteArr);
        return res;
    }

    public short byteToShort(byte[] byteArr) {
        short res = (short)((byteArr[0] & 0xff) << 8);
        res += (short)(byteArr[1] & 0xff);
        return res;
    }

    public short getOpcode() {
        return opcode;
    }

}
