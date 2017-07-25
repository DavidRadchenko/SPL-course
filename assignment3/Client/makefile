CFLAGS:= -c -Wall -Weffc++ -g -std=c++11 -I./include -L/usr/local/include
LDFLAGS:= -lboost_system -lboost_locale -lboost_thread -L/usr/local/lib

all: Client
	g++ -o bin/TFTPclient bin/ConnectionHandler.o bin/Client.o bin/EncoderDecoder.o bin/Packet.o bin/Protocol.o $(LDFLAGS)

Client: bin/ConnectionHandler.o bin/Client.o bin/EncoderDecoder.o bin/Packet.o bin/Protocol.o

bin/ConnectionHandler.o: src/ConnectionHandler.cpp
	g++ $(CFLAGS) -o bin/ConnectionHandler.o src/ConnectionHandler.cpp

bin/Client.o: src/Client.cpp
	g++ $(CFLAGS) -o bin/Client.o src/Client.cpp

bin/EncoderDecoder.o: src/EncoderDecoder.cpp
	g++ $(CFLAGS) -o bin/EncoderDecoder.o src/EncoderDecoder.cpp

bin/Packet.o: src/Packet.cpp
	g++ $(CFLAGS) -o bin/Packet.o src/Packet.cpp

bin/Protocol.o: src/Protocol.cpp
	g++ $(CFLAGS) -o bin/Protocol.o src/Protocol.cpp

.PHONY: clean
clean:
	rm -f bin/*

