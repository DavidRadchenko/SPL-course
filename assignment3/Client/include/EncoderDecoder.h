//
// Created by evnin on 1/18/17.
//
#ifndef BOOST_ECHO_CLIENT_ENCODERDECODER_H
#define BOOST_ECHO_CLIENT_ENCODERDECODER_H

#include <iostream>
#include <array>
#include "Packet.h"

using namespace std;


/// <summary>
/// Created by david on 1/13/17.
/// </summary>
class EncoderDecoder {

private:
    vector<char> bytes;
    int count = 0;
    short opCode = 0;
    string str = "";
    short size = 0;
    short blockNum = 0;
    short errCode = 0;


public:
    Packet* decodeNextByte(char nextByte);

    char* encode(Packet &message, int& l);

    short byteToShort(std::vector<char> bytesArr, int a, int b);

    char* shortToBytes(short num);

    void reset();

};


#endif //BOOST_ECHO_CLIENT_ENCODERDECODER_H
