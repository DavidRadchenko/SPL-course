#ifndef GAME_H_
#define GAME_H_

#include <iostream>

#include "../include/Player.h"
#include "../include/Deck.h"

using namespace std;

class Game {
private:
	std::vector<Player *> players;   //The list of the players
	Deck deck;                  //The deck of the game
	int turnNumber;             //our variable
	int N;                      //highest numeric value
	std::string winner;
	std::vector<int> lastPlayer;
	int verbal;

public:
	Game(char* configurationFile);
	void init();
	void play();
	void printState();        //Print the state of the game as described in the assignment.
	void printWinner();       //Print the winner of the game as describe in the assignment.
    void printNumberOfTurns(); //Print the number of played turns at any given time.

	void singleTurn(int index, int card, int player);
	int strategy12(int index);
	int strategy34(int index);

	virtual ~Game();
	Game(const Game& other);
	Game & operator=(const Game& other);


};

#endif
