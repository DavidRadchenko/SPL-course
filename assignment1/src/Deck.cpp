#include <iostream>
#include <vector>
#include <algorithm>
#include "../include/Deck.h"
#include "../include/Card.h"


using namespace std;

Card* Deck::fetchCard() {
    Card *draw = deck[0];
    deck.erase(deck.begin());
    return draw;
}

int Deck::getNumberOfCards() {
    return deck.size();
}

string Deck::toString() {
    string ans = "";
    int n = deck.size();
    for (int i = 0; i < n; i++) {
        ans = ans + deck[i]->toString()+" ";
    }
    return ans;
}

Deck::Deck(string &cards, int a) : deck(), numberOfCards((a + 3)*4) {
    string sinCard = "";
    int n = cards.length();
    for (int i = 0; i < n; i++) {
        if (cards.at(i) == ' ' || i == n - 1) {
            if(i == n - 1) sinCard += cards[i];
            if (sinCard.at(0) == 'K' || sinCard.at(0) == 'Q' || sinCard.at(0) == 'J' || sinCard.at(0) == 'A') {
                FigureCard* tmp = new FigureCard(sinCard, a);
                deck.push_back(tmp);
                //delete (tmp);
            }
            else {
                NumericCard* tmp = new NumericCard(sinCard);
                deck.push_back(tmp);
                //delete (tmp);
            }
            sinCard = "";
        }
        else sinCard += cards[i];
    }
}

Deck::Deck() : deck(), numberOfCards(0) {}

Deck::~Deck() {
    int n = deck.size();
    for (int i = 0; i < n; i++)
        delete deck[i];
    deck.clear();
}

Deck::Deck(const Deck& other): deck(), numberOfCards(other.numberOfCards) {
    int n = numberOfCards;
    for (int i = 0; i < n; i++) {
        Card* c;
        c = other.getDeck()[i]->create();
        deck.push_back(c);
    }
}


Deck & Deck::operator=(const Deck& other) {
    if(this == &other) {
        return *this;
    }
    numberOfCards = other.numberOfCards;
    Card* c;
    int n = other.deck.size();
    for(int i = 0; i < n; i++) {
       /* Card* tmp = other.getDeck()[i];
        NumericCard* n = dynamic_cast<NumericCard*>(*tmp);
        if(n == nullptr)
            c = new FigureCard(*n);
        else {
            FigureCard* n = dynamic_cast<FigureCard*>(*tmp);
            c = new NumericCard(*n);
        }

*/
        c = other.getDeck()[i]->create();
        deck.push_back(c);
    }
    return *this;
}

const vector<Card *>& Deck::getDeck() const {
    return deck;
}
