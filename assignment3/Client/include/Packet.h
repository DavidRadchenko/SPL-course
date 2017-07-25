//
// Created by david on 1/15/17.
//

#ifndef BOOST_ECHO_CLIENT_PACKET_H
#define BOOST_ECHO_CLIENT_PACKET_H

#include <array>
#include <vector>

using namespace std;

class Packet {

public:
    short opCode;
    Packet();
    virtual ~Packet();
};



class WRQ : public Packet {


private:
    string fileName;

public:

    WRQ(string fileName);
    virtual ~WRQ();
    string getFileName();
};

class RRQ : public Packet {
private:
    string fileName;

public:

    RRQ(string fileName);
    virtual ~RRQ();
    string getFileName();
};

class LOGRQ : public Packet {
private:
    string userName;

public:

    LOGRQ(string userName);
    virtual ~LOGRQ();
    string getUserName();
};

class DELRQ : public Packet {
private:

    string fileName;

public:

    DELRQ(string fileName);
    virtual ~DELRQ();
    string getFileName();
};

class DATA : public Packet {
private:

    short size;
    short blockNum;
    vector<char> data;

public:

    DATA(short size, short blockNum, vector<char> data );
    virtual ~DATA();
    short getSize();
    short getBlockNum();
    vector<char>& getData();
};

class BCAST : public Packet {
private:

    string fileName;
    char flag;

public:

    BCAST(char flag, string fileName);
    virtual ~BCAST();
    string getFileName();
    char getFlag();
};

class ACK : public Packet {
private:

    short blockNum;

public:

    ACK(short blockNum);
    virtual ~ACK();
    short getBlockNum();
};

class ERROR : public Packet {
private:

    short errCode;
    string errMsg;

public:

    ERROR(short errCode, string errMsg);
    virtual ~ERROR();
    short getErrCode();
    string getErrMsg();
};


class DISC : public Packet {

public:

    DISC();
    virtual ~DISC();
};

class DIRQ : public Packet {

public:

    DIRQ();
    virtual ~DIRQ();
};




#endif //BOOST_ECHO_CLIENT_PACKET_H



