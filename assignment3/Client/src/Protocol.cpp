//
// Created by david on 1/18/17.
//

#include "../include/Protocol.h"
#include "../include/Packet.h"

using namespace std;

Protocol::Protocol(): opCode(0), blockNum(0), size(0), errCode(0), str(""), DirqPacket(false), fileName(""), collectedData(), Del(false) {}

Protocol::~Protocol(){}

void Protocol::process(Packet* p) {
    opCode = p->opCode;
    switch(opCode) {
        case 4 : {
            processACK((ACK *) p);
            break;
        }
        case 5 : {
            processERROR((ERROR *) p);
            break;
        }
        case 9 : {
            processBCAST((BCAST *) p);
            break;
        }
    }
}

void Protocol::processACK(ACK* pack) {
    blockNum = pack->getBlockNum();
    cout<<"ACK "<<blockNum<<endl;
    reset();
}

void Protocol::processBCAST(BCAST* pack){
    string s;

    if(Del) {
        s = "delete";
        Del = false;
    }
    else
        s = "add";

    str = pack->getFileName();
    cout<<"BCAST "<<s<<" " <<str<<endl;
    reset();
}

void Protocol::processERROR(ERROR* pack){
    errCode = pack->getErrCode();
    str = pack->getErrMsg() +  " err code ";
    str += errCode;
    cout<<"ERROR: "<<str<<endl;
    reset();
}

void Protocol::processDATA(DATA* pack){
    blockNum = pack->getBlockNum();
    size = pack->getSize();
    vector<char> &data = pack->getData();
    collectedData.insert(collectedData.end(), data.begin(), data.end());
    //last packet
    if(size < 512) {
        if(!DirqPacket) {
            FILE *file;
            file = fopen (fileName.c_str(), "a+");
            long m = collectedData.size();
            if (file != NULL)
                for(long i=0; i<m; i++)
                    fputc (collectedData[i], file);
            fclose (file);
            reset();
            data.clear();
        }
        else {
            //cout<<collectedData.size()<<" size of data "<<endl;
            int n=collectedData.size();
            for(long i=0; i<n; i++)
                str += collectedData[i];
            //std::copy(collectedData.begin(), collectedData.end(), str);
            replace(str.begin(), str.end(), '\0', '\n');
            cout<<str<<endl;
            reset();
            data.clear();
        }
    }
}


void Protocol::setDirq() {
    DirqPacket = true;
}

void Protocol::setDel() {
    Del = true;
}

void Protocol::reset() {
    opCode = 0;
    blockNum = 0;
    size = 0;
    errCode = 0;
    str = "";
    collectedData.clear();
    DirqPacket = false;
    fileName = "";
}

void Protocol::setFileName(string &str) { fileName = str; }

