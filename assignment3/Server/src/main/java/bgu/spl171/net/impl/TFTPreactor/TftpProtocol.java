package bgu.spl171.net.impl.TFTPreactor;

import bgu.spl171.net.api.bidi.BidiMessagingProtocol;
import bgu.spl171.net.api.bidi.Connections;
import bgu.spl171.net.impl.packet.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static bgu.spl171.net.impl.TFTPreactor.EncoderDecoder.merge;

/**
 * Created by david on 1/13/17.
 */
public class TftpProtocol implements BidiMessagingProtocol<Packet> {

    private boolean shouldTerminate = false;
    private int connectionId;
    private Connections<Packet> connections;
    private ArrayList<byte[]> dataobj = new ArrayList<>();
    private String filename;
    private short blockNum = 0;
    private final String dir = System.getProperty("user.dir") + File.separator +"Files"+File.separator;
    private boolean logFirst;
    private String userName;
    private Vector<DATA> dapa = new Vector<DATA>();
    private ArrayList<String> fileNames = new ArrayList<String>();
    private ArrayList<DATA> dataPackets = new ArrayList<DATA>();
    private ConcurrentHashMap<Integer, String> map = new ConcurrentHashMap<Integer, String>();

    public TftpProtocol() {
        this.logFirst = false;
    }

    @Override
    public void start(int connectionId, Connections<Packet> connections) {
        this.connections = connections;
        this.connectionId = connectionId;
    }

    @Override
    public void process(Packet message) throws IOException {
        short opCode = message.getOpcode();
        System.out.println("received "+ opCode);
        if (opCode < 0 || opCode > 10)
            sendErr((short) 4);
        switch (opCode) {
            case 1:
                processRRQ(message);
                break;
            case 2:
                processWRQ(message);
                break;
            case 3:
                processDATA(message);
                break;
            case 4:
                processACK(message);
                break;
            case 5:
                processERROR(message);
                break;
            case 6:
                processDIRQ(message);
                break;
            case 7:
                processLOGRQ(message);
                break;
            case 8:
                processDELRQ(message);
                break;
            case 10:
                processDISC(message);
                break;
        }

    }

    /*
    create ACK with blockNum = 0, and send to client
    send error if no such file exist
     */
    private void processRRQ(Packet message) throws IOException {
        if (logFirst) {
            reset();
            filename = ((RRQ) message).getFilename();
            File f = new File(dir + filename);
            if (f.exists()) {
                Path filePath = f.toPath();
                byte[] array = Files.readAllBytes(filePath);
                short num = 1;
            for(int i = 0; i < array.length; i = i + 512) {
                    short size = (short) Math.min(512, array.length - i);
                    byte[] b1 = Arrays.copyOfRange(array, i, size + i);
                    DATA t = new DATA(size, num, b1);
                    dapa.add(t);
                    num++;
                }
                System.out.println("num of packets " + dapa.size());
                DATA t = dapa.get(0);
                connections.send(connectionId, t);
                System.out.println("send data packet "+ 0);
            }
            else // No such file exist
                sendErr((short) 1);
        } else
            sendErr((short) 6);
    }

    private void processACK(Packet message) {
        if (logFirst) {
            short ackNum = ((ACK) message).getBlockNum();
            System.out.println("ACK " + ackNum);
            if (blockNum != ackNum - 1)
                sendErr((short) 6);
            else {
                blockNum++;
                if (dapa.size() > blockNum) {
                    DATA t = dapa.get(ackNum);
                    connections.send(connectionId, t);
                }
            }
        }
    }

    private void processDIRQ(Packet message) {
        if (logFirst) {
            reset();
            File folder = new File(dir);
            File[] listOfFiles = folder.listFiles();
            String names = listOfFiles[0].getName();
            byte[] data ;
            for (int i = 1; i < listOfFiles.length; i++)
                names = names + '\0' + listOfFiles[i].getName();
            short num = 1;
            for(int i = 0; i < names.length(); i = i + 512) {
                short size = (short) Math.min(512, names.length() - i);
                String s = names.substring(i , i + size);
                data = s.getBytes(StandardCharsets.UTF_8);
                DATA t = new DATA(size, num, data);
                dapa.add(t);
                num++;
            }
            data = names.getBytes(StandardCharsets.UTF_8);
            //DATA t = new DATA((short) data.length, num, data);
            DATA t = dapa.get(blockNum);
            connections.send(connectionId, t);
        } else
            sendErr((short) 6);
    }

    private void processWRQ(Packet message) {
        if (logFirst) {
            filename = ((WRQ) message).getFilename();
            File f = new File(dir + filename);
            if (f.exists())
                sendErr((short) 5);
            else {
                sendAck((short) 0);
            }
        } else
            sendErr((short) 6);
    }

    private void processDATA(Packet message) {
        if (logFirst) {
            blockNum = ((DATA) message).getBlockNum();
            if (((DATA) message).getData().length < 512) {
                dataobj.add(((DATA) message).getData());
                byte[] res = {};
                for (byte[] pk : dataobj)
                    res = merge(res, pk);
                try {
                    FileOutputStream file = new FileOutputStream(dir + filename);
                    try {
                        file.write(res);
                        file.close();
                    }
                    catch (IOException e) {
                        sendErr((short) 1);
                    }
                    dataobj = new ArrayList<>();
                    fileNames.add(filename);
                    byte flag = 3;
                    sendAck(blockNum);
                    sendBcast(flag, filename);
                    reset();
                } catch (Exception e) {}
            } else {
                dataobj.add(((DATA) message).getData());
                sendAck(blockNum);
            }
        } else
            sendErr((short) 6);
    }

    private void processLOGRQ(Packet message) {
        //returns username even though it says fileName
        userName = ((LOGRQ) message).getFilename();
        if (map.contains(userName))
            sendErr((short) 7);
        else {
            map.putIfAbsent(connectionId, userName);
            logFirst = true;
            sendAck((short) 0);
        }
    }

    private void processDELRQ(Packet message) {
        if (logFirst) {
            filename = ((DELRQ) message).getFilename();
            File f = new File(dir+filename);
            if (f.exists()) {
                fileNames.remove(filename);
                f.delete();
                sendAck((short) 0);
                sendBcast((byte) 2, filename);
                dataPackets = new ArrayList<DATA>();
            } else
                sendErr((short) 1);
        } else
            sendErr((short) 6);
    }

    private void processDISC(Packet message) throws IOException {
        if (logFirst) {
            map.remove(connectionId);
            shouldTerminate = true;
            sendAck((short) 0);
        } else
            sendErr((short) 6);
    }

    private void processERROR(Packet message) {
        short errCode = ((ERROR) message).getErrorCode();
        String d =  ((ERROR) message).getErrMsg();
        System.out.print("error " + errCode);
        if (errCode == 3)
            reset();
        System.out.println(d);
    }


    private void sendErr(short errCode) {
        Packet tmpERROR = new ERROR(errCode);
        System.out.println("sending err " + errCode);
        connections.send(connectionId, tmpERROR);
    }

    private void sendAck(short bNum) {
        ACK tmp = new ACK(bNum);
        System.out.println("sending ack " + bNum);
        connections.send(connectionId, tmp);
    }

    private void sendBcast(byte flag, String str) {
        BCAST tmp = new BCAST(flag, str);
        Enumeration<Integer> Users = map.keys();
        while(Users.hasMoreElements()) {
            connections.send(Users.nextElement(), tmp);
        }
    }

    @Override
    public boolean shouldTerminate() {
        return false;
    }

    private void reset() {
        blockNum = 0;
        dataPackets = new ArrayList<DATA>();
        dapa = new Vector<DATA>();
    }
}