//
// Created by david on 1/18/17.
//

#ifndef TFTPCLIENT_PROTOCOL_H
#define TFTPCLIENT_PROTOCOL_H

#include <string>
#include <iostream>
#include <boost/asio.hpp>
#include <vector>
#include "Packet.h"


using namespace std;

class Protocol{
private:

    short opCode;
    short blockNum;
    short size;
    short errCode;
    string str;
    bool DirqPacket;
    string fileName;
    vector<char> collectedData;
    
    
    
    bool Del;
    

public:
    Protocol();

    ~Protocol();

    void process(Packet* p);

    void processACK(ACK* pack);

    void processBCAST(BCAST* pack);

    void processERROR(ERROR* pack);

    void processDATA(DATA *pack);

    void setDirq();

    void setDel();

    void reset();

    void setFileName(string &s);

    ;
};

#endif //TFTPCLIENT_PROTOCOL_H
