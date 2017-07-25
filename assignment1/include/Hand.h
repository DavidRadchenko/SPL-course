#ifndef HAND_H_
#define HAND_H_

#include <iostream>
#include "../include/Card.h"
#include <vector>

using namespace std;




class Hand {
private:
	vector<Card*> hand;

public:
	Hand();
	Hand(vector<Card*>* other);
	Hand & operator=(const Hand& other);
	Hand(const Hand& other);
	virtual ~Hand();

	bool addCard(Card &card);
	bool removeCard(Card &card);
	void removeFour(Card &card);
	int getNumberOfCards() const ;  // Get the number of cards in hand
	string toString(); // Return a list of the cards, separated by space, in one line,in a sorted order, ex: "2S 5D 10H"
	const vector<Card*>& getCards() const ;
	vector<Card*> passCards(int val);

	void add(Card *card);
	bool isEmpty();



};

#endif
