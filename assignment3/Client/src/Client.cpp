#include <stdlib.h>
#include <boost/thread.hpp>
#include "../include/ConnectionHandler.h"

//TODO read from server


class SrvRead {

private:
    ConnectionHandler *handle;
public:
    SrvRead(ConnectionHandler *_handle) : handle(_handle) {}

    void operator()() {

        while (handle->isConnected()) {
            Packet* pack = nullptr;
            if (!handle->receiveMsg(pack)) {
                std::cout << "Disconnected. Exiting...\n" << std::endl;
                break;
            }
        }
    }

};

//TODO read from Keybard
class InputRead {

private:
    ConnectionHandler *handle;
public:
    InputRead(ConnectionHandler *_handle) : handle(_handle) {}

    void operator()() {
        while (handle->isConnected()) {
            const short bufsize = 1024;
            char buf[bufsize];
            cin.getline(buf, bufsize); // after DISC the keyboard thread waits for input and then terminates ASK THIS
            string line(buf);
            while (!(handle->inputCheck(line))) {
                if (!handle->isConnected())
                    break;
                std::cout <<"Invalid Input"<< std::endl;
                cin.getline(buf, bufsize);
                string n(buf);
                line.swap(n);
            }
            if (!handle->isConnected())
                break;
            Packet *pack = handle->createPacket(line);
            if (!handle->sendInput(pack)) { // maybe delete
                std::cout << "Disconnected.\n" << std::endl;
                break;
            }
        }
    }
};


















int main(int argc, char *argv[]) {
    if (argc < 3) {
        std::cerr << "Usage: " << argv[0] << " host port" << std::endl << std::endl;
        return -1;
    }
    std::string host = argv[1];
    short port = atoi(argv[2]);
    ConnectionHandler connectionHandler(host, port);

    if (!connectionHandler.connect()) {
        std::cerr << "Cannot connect to " << host << ":" << port << std::endl;
        return 1;
    }

    SrvRead sr(&connectionHandler);
    InputRead in(&connectionHandler);
    boost::thread t1(sr);
    boost::thread t2(in);
    t1.join();
    t2.join();
    return 0;
}

