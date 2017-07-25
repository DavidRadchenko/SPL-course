#include <mutex>
#include <boost/asio/ip/tcp.hpp>
#include <iostream>
#include "../include/ConnectionHandler.h"

using boost::asio::ip::tcp;

using std::cin;
using std::cout;
using std::cerr;
using std::endl;
using std::string;

ConnectionHandler::ConnectionHandler(string host, short port) : host_(host), port_(port), io_service_(),
                                                                socket_(io_service_), ptc(), connected(true), endec(), dapa(){}

ConnectionHandler::~ConnectionHandler() {
    close();
}

bool ConnectionHandler::connect() {
    std::cout << "Starting connect to "
              << host_ << ":" << port_ << std::endl;
    try {
        tcp::endpoint endpoint(boost::asio::ip::address::from_string(host_), port_); // the server endpoint
        boost::system::error_code error;
        socket_.connect(endpoint, error);
        if (error)
            throw boost::system::system_error(error);
    }
    catch (std::exception &e) {
        std::cerr << "Connection failed (Error: " << e.what() << ')' << std::endl;
        return false;
    }
    return true;
}

bool ConnectionHandler::getBytes(char bytes[], unsigned int bytesToRead) {
    size_t tmp = 0;
    boost::system::error_code error;
    try {
        while (!error && bytesToRead > tmp) {
            tmp += socket_.read_some(boost::asio::buffer(bytes + tmp, bytesToRead - tmp), error);
        }
        if (error)
            throw boost::system::system_error(error);
    } catch (std::exception &e) {
        std::cerr << "recv failed (Error: " << e.what() << ')' << std::endl;
        return false;
    }
    return true;
}

bool ConnectionHandler::sendBytes(const char bytes[], int bytesToWrite) {
    int tmp = 0;
    boost::system::error_code error;
    try {
        while (!error && bytesToWrite > tmp ) {
            tmp += socket_.write_some(boost::asio::buffer(bytes + tmp, bytesToWrite - tmp), error);
        }
        if(error)
            throw boost::system::system_error(error);

    } catch (std::exception& e) {
        std::cerr << "recv failed (Error: " << e.what() << ')' << std::endl;
        return false;
    }
    return true;
}

// Close down the connection properly.
void ConnectionHandler::close() {
    try {
        socket_.close();
        connected=false;
    } catch (...) {
        std::cout << "closing failed: connection already closed" << std::endl;
    }
}

bool ConnectionHandler::isConnected(){ return connected; }


bool ConnectionHandler::receiveMsg(Packet* pack) {
    pack = getDecodedMessage();
    if(pack != nullptr){
        short op = pack->opCode;
        if(op == 3) {//DATA
            DATA *p = dynamic_cast<DATA *>(pack);
            ptc.processDATA(p);
            ACK *ack = new ACK(p->getBlockNum());
            sendMessage(ack);
        }
        else if(op == 4 && Wrq) {
            ACK *p = dynamic_cast<ACK *>(pack);
            ptc.process(pack);
            short t=p->getBlockNum();
            short s=dapa.size();
            if(s > t) {
                DATA* p1 = dapa.at(p->getBlockNum());
                return sendMessage(p1);
                }
            else {
                Wrq = false;
                dapa.clear();
            }
        }else if(op == 4 && Disc) {
            cout<<"closd"<<endl;
            close();
        }
        else if(op != 3)
            ptc.process(pack);
        return true;
    }
    return false;
}

Packet* ConnectionHandler::getDecodedMessage() {
    char ch;
    Packet* p = nullptr;
    try {
        while (p == nullptr) {
            getBytes(&ch, 1);
            p = endec.decodeNextByte(ch);
        }
    }
    catch (std::exception &e) {
        std::cerr << "recv failed (Error: " << e.what() << ')' << std::endl;
    }
    return p;
}

bool ConnectionHandler::sendMessage(Packet *p) {
    int l;
    char* ch = endec.encode(*p, l);
    sendBytes(ch, l);
    return true;
}

Packet* ConnectionHandler::createPacket(string line) {
    string t1 = line.substr(0, line.find(' ', 0));
    string t2;
    Packet* p = nullptr;
    if (t1 == "DISC")
        p = new DISC();
    else if (t1 == "DIRQ")
        p = new DIRQ();
    else if (t1 == "LOGRQ") {
        string name = line.substr(line.find(' ', 0) + 1);
        p = new LOGRQ(name);
    } else if (t1 == "DELRQ") {
        string name = line.substr(line.find(' ', 0) + 1);
        p = new DELRQ(name);
    }
    else if (t1 == "RRQ") {
        string name = line.substr(line.find(' ', 0) + 1);
        ptc.setFileName(name);
        p = new RRQ(name);
    }
    else if (t1 == "WRQ") {
        string name = line.substr(line.find(' ', 0) + 1);
        p = new WRQ(name);
    }
    return p;
}

bool ConnectionHandler::inputCheck(string &s) {
    string t1 = s.substr(0, s.find(' ', 0));
    string t2;
    if (t1 == "DISC" || t1 == "DIRQ") { return true; }
    if (t1 == "LOGRQ" || t1 == "DELRQ" || t1 == "RRQ" || t1 == "WRQ") {
        t2 = s.substr(s.find(' ', 0) + 1);
        if(t1 == "WRQ" && checkWRQ(t2))return true;
        if (t2 == "LOGRQ" || t2 == "DELRQ" || t2 == "RRQ" || t2 == "WRQ") return false;
        if (t2.size() > 0) return true;
        return false;
    }
    return false;
}

bool ConnectionHandler::checkWRQ(string &s){
    FILE *file;
    file = fopen (s.c_str(), "a+");
    if (file == NULL){
        std::cout <<"Invalid Input"<< std::endl;
        return false;
    }
    fclose(file);
    return true;
}

bool ConnectionHandler::sendInput(Packet *p) {
    short op = p->opCode;
    if (op == 2) {
        Wrq = true;
        WRQ *pack = dynamic_cast<WRQ *>(p);
        string fileName = pack->getFileName();
        fileToDataPackets(fileName);
        return sendMessage(p);
    }
    else if(op == 6) {
        ptc.setDirq();
        return sendMessage(p);
    }
    else if(op == 8) {
        ptc.setDel();
        return sendMessage(p);
    }
    else if(op == 10) {
        Disc = true;
        return sendMessage(p);
    }

    else {
        return sendMessage(p);
    }
    return true;
}


void ConnectionHandler::fileToDataPackets(string fileName) {
    const char * c = fileName.c_str();
    string fileChar = get_file_contents(c);
    short bn = 1;
    short size;
    vector<char> ch;
    int j = 512;
    int n = fileChar.length();
    int i = 0;
    while(n > i) {
        j = std::min(512, n - i);
        std::vector<char> data(fileChar.begin() + i, fileChar.begin() + (i + j));
        i += 512;
        size = data.size();
        DATA *t = new DATA(size, bn, data);
        dapa.push_back(t);
        bn++;
    }
}



//FROM STUCK OVER FLOW
std::string ConnectionHandler::get_file_contents(const char *filename)
{
    std::FILE *fp = std::fopen(filename, "rb");
    if (fp)
    {
        std::string contents;
        std::fseek(fp, 0, SEEK_END);
        contents.resize(std::ftell(fp));
        std::rewind(fp);
        std::fread(&contents[0], 1, contents.size(), fp);
        std::fclose(fp);
        return(contents);
    }
    throw(errno);
}


