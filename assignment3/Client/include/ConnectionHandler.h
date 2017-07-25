#ifndef CONNECTION_HANDLER__
#define CONNECTION_HANDLER__
                                           
#include <string>
#include <iostream>
#include <boost/asio.hpp>
#include <mutex>
#include "Packet.h"
#include "EncoderDecoder.h"
#include "Protocol.h"
#include <fstream>
#include <stdio.h>

using boost::asio::ip::tcp;

class ConnectionHandler {
private:
	const std::string host_;
	const short port_;
	boost::asio::io_service io_service_;   // Provides core I/O functionality
	tcp::socket socket_;
    Protocol ptc;
    bool connected;
    EncoderDecoder endec;
    std::vector<DATA*> dapa;
    bool Wrq = false;
    bool Disc = false;

public:
    ConnectionHandler(std::string host, short port);
    virtual ~ConnectionHandler();
 
    // Connect to the remote machine
    bool connect();
 
    // Read a fixed number of bytes from the server - blocking.
    // Returns false in case the connection is closed before bytesToRead bytes can be read.
    bool getBytes(char bytes[], unsigned int bytesToRead);
 
	// Send a fixed number of bytes from the client - blocking.
    // Returns false in case the connection is closed before all the data is sent.
    bool sendBytes(const char bytes[], int bytesToWrite);
	
    // Read an ascii line from the server
    // Returns false in case connection closed before a newline can be read.
    bool getLine(std::string& line);
	
	// Send an ascii line from the server
    // Returns false in case connection closed before all the data is sent.
    bool sendLine(std::string& line);
 
    // Get Ascii data from the server until the delimiter character
    // Returns false in case connection closed before null can be read.
    bool getFrameAscii(std::string& frame, char delimiter);
 
    // Send a message to the remote host.
    // Returns false in case connection is closed before all the data is sent.
    bool sendFrameAscii(const std::string& frame, char delimiter);

    // Close down the connection properly.
    void close();

    bool receiveMsg(Packet* pack);

    Packet* getDecodedMessage();

    bool sendInput(Packet *p);

    bool respond(Packet *p);

    bool sendMessage(Packet * p);

    bool inputCheck(string &s);

    bool isConnected();

    Packet* createPacket(string line);

    vector<string> fileToVector(string fileName);

    bool checkWRQ(string &s);

    void fileToDataPackets(string fileName);

    std::string get_file_contents(const char *filename);

}; //class ConnectionHandler
 
#endif