package bgu.spl171.net.impl.TFTPreactor;

import bgu.spl171.net.api.MessageEncoderDecoder;
import bgu.spl171.net.impl.packet.*;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * Created by david on 1/13/17.
 */
public class EncoderDecoder implements MessageEncoderDecoder<Packet> {

    private byte[] bytes = new byte[518]; //start with 1k
    private int len;
    private int count;
    private short opCode;
    private String str;
    private short size;
    private short blockNum;



    private int counter = 0;

    public EncoderDecoder() {
        len = 0;
        count = 0;
        opCode = 0;
        str = "";
        size = 0;
        blockNum = 0;
    }

    public Packet decodeNextByte(byte nextByte) {
        count++;
        pushByte(nextByte);
        if (count >= 2) {
            opCode = byteToShort(bytes, 0, 1);
            if (opCode == 1 || opCode == 2 || opCode == 7 || opCode == 8) {
                str = byteToString(nextByte, 2);
                if (str != null) {
                    Packet tmp;
                    if (opCode == 1)
                        tmp = new RRQ(str);
                    else if (opCode == 2)
                        tmp = new WRQ(str);
                    else if (opCode == 7)
                        tmp = new LOGRQ(str);
                    else
                        tmp = new DELRQ(str);
                    reset();
                    return tmp;
                }
            } else if (opCode == 3) {
                if (count == 4) {
                    size = byteToShort(bytes, 2, 3);
                }
                if (count == 6) {
                    blockNum = byteToShort(bytes, 4, 5);
                }
                if (count == size + 6) {
                    byte[] data = Arrays.copyOfRange(bytes, 6, size + 6);
                    DATA tmp = new DATA(size, blockNum, data);
                    reset();
                    return tmp;
                }
            } else if (opCode == 4) {
                if (count == 4) {
                    blockNum = byteToShort(bytes, 2, 3);
                    ACK tmp = new ACK(blockNum);
                    reset();
                    return tmp;
                }
            } else if (opCode == 5) {
                short errCode = 0;
                if (count == 4)
                    errCode = byteToShort(bytes, 2, 3);
                if (count > 4)
                    str = byteToString(nextByte, 4);
                // error massage generares automaticly by the errcode.
                ERROR tmp = new ERROR(errCode);
                reset();
                return tmp;
            } else if (opCode == 6) {
                DIRQ tmp = new DIRQ();
                reset();
                return tmp;
            } else if (opCode == 10) {
                DISC tmp = new DISC();
                reset();
                return tmp;
            } else {
                ERROR tmp = new ERROR((short) 4);
                return tmp;
            }
        }
        return null;
    }

    public byte[] encode(Packet message) {
        short opCode = message.getOpcode();
        byte[] code = shortToBytes(opCode);
        byte[] res;
        if (opCode == 3) {
            byte[] size = shortToBytes(((DATA) message).getPacketSize());
            byte[] blockNum = shortToBytes(((DATA) message).getBlockNum());
            byte[] data = ((DATA) message).getData();
            res = merge(code, size);
            res = merge(res, blockNum);
            res = merge(res, data);
            counter += data.length;
        } else if (opCode == 4) {
            byte[] blockNum = shortToBytes(((ACK) message).getBlockNum());
            res = merge(code, blockNum);
        } else if (opCode == 5) {
            byte[] str = stringToByte(((ERROR) message).getErrMsg());
            byte[] lastByte = {0};
            byte[] errCode = shortToBytes(((ERROR) message).getErrorCode());
            res = merge(code, errCode);
            res = merge(res, str);
            res = merge(res, lastByte);
        } else {
            byte[] str = stringToByte(((BCAST) message).getFilename());
            byte[] lastByte = {0};
            byte[] flag = {((BCAST) message).getFlag()};
            res = merge(code, flag);
            res = merge(res, str);
            res = merge(res, lastByte);
        }
        return res;
    }

    public static byte[] merge(byte[] first, byte[] second) {
        byte[] res = new byte[first.length + second.length];
        for (int i = 0; i < res.length; i++) {
            if (first.length > i)
                res[i] = first[i];
            else
                res[i] = second[i - first.length];
        }
        return res;
    }

    public short byteToShort(byte[] byteArr, int a, int b) {
        short res = (short) ((byteArr[a] & 0xff) << 8);
        res += (short) (byteArr[b] & 0xff);
        return res;
    }

    public static byte[] shortToBytes(short message) {
        byte[] bytesArr = new byte[2];
        bytesArr[0] = (byte) ((message >> 8) & 0xFF);
        bytesArr[1] = (byte) (message & 0xFF);
        return bytesArr;
    }

    public String byteToString(byte nextByte, int start) {
        //notice that the top 128 ascii characters have the same representation as their utf-8 counterparts
        //this allow us to do the following comparison
        if (nextByte == '\0') {
            //notice that we explicitly requesting that the string will be decoded from UTF-8
            //this is not actually required as it is the default encoding in java.
            String result = new String(bytes, start, len - 3, StandardCharsets.UTF_8);
            len = 0;
            return result;
        }
        return null; //not a line yet
    }

    public static byte[] stringToByte(String message) {
        return (message + "\n").getBytes(); //uses utf8 by default
    }


    private void pushByte(byte nextByte) {
        if (len >= bytes.length)
            bytes = Arrays.copyOf(bytes, len * 2);
        bytes[len++] = nextByte;
    }

    private void reset() {
        bytes = new byte[518]; //start with 1k
        len = 0;
        count = 0;
        opCode = 0;
        str = null;
        size = 0;
        blockNum = 0;
    }
}
