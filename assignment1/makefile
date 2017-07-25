CC = g++
CFLAGS  = -g -Wall -Weffc++ -std=c++11
LFLAGS  = -L/usr/lib

# All Targets
all: reviiot

# Tool invocations
# Executable depends on the files :
reviiot: bin/reviiyot.o bin/Card.o bin/FigureCard.o bin/NumericCard.o bin/Hand.o bin/Deck.o bin/Player.o bin/PlayerType1.o bin/PlayerType2.o bin/PlayerType3.o bin/PlayerType4.o bin/Game.o

#	@echo 'Building target: reviiot'
#	@echo 'Invoking: C++ Linker'
	$(CC) -o bin/reviiot bin/reviiyot.o bin/Card.o bin/FigureCard.o bin/NumericCard.o bin/Deck.o bin/Hand.o bin/Player.o bin/PlayerType1.o bin/PlayerType2.o bin/PlayerType3.o bin/PlayerType4.o bin/Game.o $(LFLAGS)
#	@echo 'Finished building target: reviiot'
#	@echo ' '

# Depends on the source and header files
bin/Card.o: src/Card.cpp
	$(CC) $(CFLAGS) -c -Iinclude -o bin/Card.o src/Card.cpp

bin/FigureCard.o: src/FigureCard.cpp
	$(CC) $(CFLAGS) -c -Iinclude -o bin/FigureCard.o src/FigureCard.cpp

bin/NumericCard.o: src/NumericCard.cpp
	$(CC) $(CFLAGS) -c -Iinclude -o bin/NumericCard.o src/NumericCard.cpp

bin/Deck.o: src/Deck.cpp
	$(CC) $(CFLAGS) -c -Iinclude -o bin/Deck.o src/Deck.cpp

bin/Hand.o: src/Hand.cpp
	$(CC) $(CFLAGS) -c -Iinclude -o bin/Hand.o src/Hand.cpp

bin/Player.o: src/Player.cpp
	$(CC) $(CFLAGS) -c -Iinclude -o bin/Player.o src/Player.cpp

bin/PlayerType1.o: src/PlayerType1.cpp
	$(CC) $(CFLAGS) -c -Iinclude -o bin/PlayerType1.o src/PlayerType1.cpp

bin/PlayerType2.o: src/PlayerType2.cpp
	$(CC) $(CFLAGS) -c -Iinclude -o bin/PlayerType2.o src/PlayerType2.cpp

bin/PlayerType3.o: src/PlayerType3.cpp
	$(CC) $(CFLAGS) -c -Iinclude -o bin/PlayerType3.o src/PlayerType3.cpp

bin/PlayerType4.o: src/PlayerType4.cpp
	$(CC) $(CFLAGS) -c -Iinclude -o bin/PlayerType4.o src/PlayerType4.cpp

bin/Game.o: src/Game.cpp
	$(CC) $(CFLAGS) -c -Iinclude -o bin/Game.o src/Game.cpp

bin/reviiyot.o: src/reviiyot.cpp
	$(CC) $(CFLAGS) -c -Iinclude -o bin/reviiyot.o src/reviiyot.cpp


#Clean the build directory
clean: 
	rm -f bin/*
 





# Depends on the source and header files 
