#ifndef DECK_H_
#define DECK_H_

#include <iostream>
#include <vector>
#include "../include/Card.h"

using namespace std;

class Deck {
private:
	// Declare here the collection of "Card *" of the deck
	vector<Card*> deck;
	int numberOfCards;

public:
    Card* fetchCard();   //Returns the top card of the deck and remove it from the deck
    int getNumberOfCards(); // Get the number of cards in the deck
    string toString(); // Return the cards in top-to-bottom order in a single line, cards are separated by a space ex: "12S QD AS 3H"
	Deck(string &cards, int a);
	vector<Card *>* dealCards();
    Deck();
	const vector<Card*>& getDeck() const ;
	virtual ~Deck();
	Deck & operator=(const Deck& other);
	Deck(const Deck& other);
};

#endif
