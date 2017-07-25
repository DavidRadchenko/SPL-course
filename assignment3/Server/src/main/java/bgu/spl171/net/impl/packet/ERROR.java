package bgu.spl171.net.impl.packet;

/**
 * Created by david on 1/13/17.
 */
public class ERROR extends Packet {

    private short ErrorCode;
    private String ErrMsg;

    public ERROR(short ErrorCode) {
        setOpcode((short) 5);
        this.ErrorCode = ErrorCode;
        switch (ErrorCode) {
            case 0:
                ErrMsg = "Not defined, see error message ().";
                break;
            case 1:
                ErrMsg = "File not found – RRQ \\ DELRQ of non-existing file";
                break;
            case 2:
                ErrMsg = "Access violation – File cannot be written, read or deleted.";
                break;
            case 3:
                ErrMsg = "Disk full or allocation exceeded – No room in disk.";
                break;
            case 4:
                ErrMsg = "Illegal TFTP operation – Unknown Opcode.";
                break;
            case 5:
                ErrMsg = "File already exists – File name exists on WRQ.";
                break;
            case 6:
                ErrMsg = "User not logged in – Any opcode received before Login completes.";
                break;
            case 7:
                ErrMsg = "User already logged in – Login username already connected.";
                break;
        }

    }

    public String getErrMsg() {
        return ErrMsg;
    }

    public short getErrorCode() {
        return ErrorCode;
    }

}
