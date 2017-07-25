//
// Created by david on 1/15/17.
//

#include "../include/Packet.h"

Packet::Packet() :opCode() {}

Packet::~Packet() {}

RRQ::RRQ(string fileName) : fileName(fileName){opCode=1;}

RRQ::~RRQ() {}

string RRQ::getFileName() {
    return fileName;
}

WRQ::WRQ(string fileName) :fileName(fileName){
    opCode = 2;
}

WRQ::~WRQ() {}

string WRQ::getFileName() {
    return fileName;
}

LOGRQ::LOGRQ(string userName) : userName(userName){
    opCode = 7;
}

LOGRQ::~LOGRQ() {}

string LOGRQ::getUserName() {
    return userName;
}

ERROR::ERROR(short errCode, string errMsg) :errCode(errCode),errMsg(errMsg) {
    opCode = 5;
}

ERROR::~ERROR() {}

short ERROR::getErrCode() {
    return errCode;
}

string ERROR::getErrMsg() {
    return errMsg;
}

DISC::DISC() {
    opCode = 10;
}

DISC::~DISC() {}

DIRQ::DIRQ() {
    opCode = 6;
}

DIRQ::~DIRQ() {}

DELRQ::DELRQ(string fileName) :fileName(fileName) {
    opCode = 8;
}

DELRQ::~DELRQ() {}

string DELRQ::getFileName() {
    return fileName;
}


DATA::DATA(short size, short blockNum, vector<char> data) :size(size), blockNum(blockNum),data(data)  {
    opCode = 3;
}

DATA::~DATA() {}

short DATA::getSize() {
    return size;
}

short DATA::getBlockNum() {
    return blockNum;
}

vector<char>& DATA::getData() {
    return data;
}

BCAST::~BCAST() {}

BCAST::BCAST(char flag, string fileName) :fileName(fileName), flag(flag) {
    opCode = 9;
}

string BCAST::getFileName() {
    return fileName;
}

char BCAST::getFlag() {
    return flag;
}

ACK::~ACK() {}

ACK::ACK(short blockNum) : blockNum(blockNum){
    opCode = 4;
}

short ACK::getBlockNum() {
    return blockNum;
}









