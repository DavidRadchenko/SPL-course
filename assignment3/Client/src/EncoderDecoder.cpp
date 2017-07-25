//
// Created by david on 1/15/17.
//

#include <cstring>
#include "../include/EncoderDecoder.h"

//ACK , DATA , ERROR , BCAST
Packet *EncoderDecoder::decodeNextByte(char nextByte) {
    count++;
    bytes.push_back(nextByte);  //THIS IS A VECTOR
    Packet *p;
    if (count > 2) {

        opCode = byteToShort(bytes, 0, 1);
        if (opCode == 3) {
            if (count == 4) {
                size = byteToShort(bytes, 2, 3);
            }
            if (count == 6) {
                blockNum = byteToShort(bytes, 4, 5);
            }
            if (count == size + 6) {
                vector<char> newVec(bytes.begin() + 6, bytes.end());
                p = new DATA(size, blockNum, newVec);
                reset();
                return p;
            }
        } else if (opCode == 4) {
            short blockNum = 0;
            if (count == 4) {
                blockNum = byteToShort(bytes, 2, 3);
                p = new ACK(blockNum);
                reset();
                return p;
            }
        } else if (opCode == 5) {
            short errCode = 0;
            if (count == 4)
                errCode = byteToShort(bytes, 2, 3);
            if (nextByte == '\0' && count > 4) {
                std::string str(bytes.begin() + 4, bytes.end() - 1);
                p = new ERROR(errCode, str);
                reset();
                return p;
            }
        } else if(opCode == 9) {
            char flag = 0;
            if (count == 3)
                flag = nextByte;


            if (nextByte == '\0' && count > 3) {
                std::string str(bytes.begin() + 3, bytes.end() - 1);
                p = new BCAST(flag, str);
                reset();
                return p;
            }

        }
    }
    return nullptr;
}

char *EncoderDecoder::encode(Packet &pack, int& l) {
    opCode = pack.opCode;
    char *code = shortToBytes(opCode);
    char* c;
    if (opCode == 7 || opCode == 8 || opCode == 1 || opCode == 2) {
        vector<char> data;
        if (opCode == 2) {
            WRQ &p = dynamic_cast<WRQ &>(pack);
            str = p.getFileName();
        } else if (opCode == 1) {
            RRQ &p = dynamic_cast<RRQ &>(pack);
            str = p.getFileName();
        } else if (opCode == 7) {
            LOGRQ &p = dynamic_cast<LOGRQ &>(pack);
            str = p.getUserName();
        } else if (opCode == 8){
            DELRQ &p = dynamic_cast<DELRQ &>(pack);
            str = p.getFileName();
        }
        vector<char> name(str.begin(), str.end());
        vector<char> res(name.size() + 3);
        res[0] = code[0];
        res[1] = code[1];
        int length = res.size() - 1;
        for (int i = 2; i < length; i++)
            res[i] = name[i - 2];
        l = res.size();
        res[res.size() - 1] = '\0';
        c = new char[res.size()];
        std::copy(res.begin(), res.end(), c);
        reset();
        return c;
    } else if (opCode == 4) {
        ACK &p = dynamic_cast<ACK &>(pack);
        short t = p.getBlockNum();
        char *block = shortToBytes(t);
        c = new char[4];
        c[0] = code[0];
        c[1] = code[1];
        c[2] = block[0];
        c[3] = block[1];
        l = 4;
        reset();
        return c;
    } else if (opCode == 3) {
        DATA &p = dynamic_cast<DATA &>(pack);
        char *size = shortToBytes(p.getSize());
        char *blockNum = shortToBytes(p.getBlockNum());
        vector<char> data = p.getData();
        vector<char> res(data.size() + 6);
        res[0] = code[0];
        res[1] = code[1];
        res[2] = size[0];
        res[3] = size[1];
        res[4] = blockNum[0];
        res[5] = blockNum[1];
        short length = res.size();
        for (int i = 6; i < length; i++) {
            res[i] = data[i - 6];
        }
        l = res.size();
        c = new char[res.size()];
        copy(res.begin(), res.end(), c);
        reset();
        return c;
    } else if (opCode == 6 || opCode == 10) {
        c = code;
        l = 2;
        reset();
        return c;
    } else {
        ERROR &p = dynamic_cast<ERROR &>(pack);
        char *errC = shortToBytes(p.getErrCode());
        str = p.getErrMsg();
        vector<char> name(str.begin(), str.end());
        vector<char> res(name.size() + 5);
        res[0] = code[0];
        res[1] = code[1];
        res[2] = errC[0];
        res[3] = errC[1];
        short l = res.size() - 1;
        for (int i = 2; i < l; i++) {
            res[i] = name[i - 4];
        }
        res[res.size() - 1] = '\0';
        c = new char[res.size()];
        copy(res.begin(), res.end(), c);
        l = res.size();
        reset();
        return c;
    }
}

short EncoderDecoder::byteToShort(vector<char> bytesArr, int a, int b) {
    short res = (short) ((bytesArr[a] & 0xff) << 8);
    res += (short) (bytesArr[b] & 0xff);
    return res;
}

char * EncoderDecoder::shortToBytes(short num) {
    char *bytesArr = new char[2];
    bytesArr[0] = ((num >> 8) & 0xFF);
    bytesArr[1] = (num & 0xFF);
    return bytesArr;
}

void EncoderDecoder::reset() {
    bytes.clear();
    count = 0;
    opCode = 0;
    str = "";
    size = 0;
    blockNum = 0;
    errCode = 0;
}


