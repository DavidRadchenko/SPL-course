#include <algorithm>
#include <iostream>
#include <vector>
#include "../include/Hand.h"

using namespace std;


Hand::~Hand() {
    int n = hand.size();
    for (int i = 0; i < n; i++)
        delete hand[i];
    hand.clear();
}

int Hand::getNumberOfCards() const {
    return (int) hand.size();
}

string Hand::toString() {
    string ans = "";
    int n = hand.size();
    for (int i = 0; i < n; i++) {
        ans = ans + hand[i]->toString() + " ";
    }
    return ans;
}

Hand::Hand(vector<Card *> *other) : hand() {
    hand = *other;
}

const vector<Card *> &Hand::getCards() const {
    return hand;
}

Hand::Hand() : hand() {}

vector<Card *> Hand::passCards(int val) {
    vector<Card *> tmp;
    int n = hand.size();
    for (int i = 0; i < n; i++)
        if (hand[i]->getValue() / 10 == val)
            tmp.push_back(hand[i]);
    n = tmp.size();
    for (int i = 0; i < n; i++)
        removeCard(*tmp[0]);
    return tmp;
}

bool Hand::removeCard(Card &card) {
    int n = hand.size();
    for (int i = 0; i < n; i++)
        if (hand[i]->getValue() / 10 == card.getValue() / 10) {
            hand.erase(hand.begin() +i);
            n = hand.size();
        }
    return true;
}

bool Hand::isEmpty() { return hand.size() == 0; }

Hand &Hand::operator=(const Hand &other) {
    if (this == &other) {
        return *this;
    }
    int const n = other.getNumberOfCards();
    for (int i = n - 1; i > -1; i--)
        hand.push_back(other.hand[i]);
    return *this;
}

Hand::Hand(const Hand &other) : hand() {
    Card *c;
    int n= other.getNumberOfCards();
    for (int i = 0; i < n; i++) {
        c = other.getCards()[i]->create();
        hand.push_back(c);
    }
}

bool Hand::addCard(Card &card) {
    int counter = 1;
    int n = hand.size();
    for (int i = 0; i < n; i++)
        if (hand[i]->getValue() / 10 == card.getValue() / 10)
            counter++;
    return counter != 4;
}

void Hand::add(Card *card) {
    vector<Card *>::iterator iter = hand.begin();
    if (addCard(*card)) {
        int i = 0;
        int n = hand.size();
        while (iter < hand.end() && (i < n) && (hand[i]->getValue()) < card->getValue()) {
            i++;
            iter++;
        }
        if (iter >= hand.end()) hand.push_back(card);
        else hand.insert(iter, card);
    }
    else {
        for (int i = 0; i < 4; i++)
            removeFour(*card);
        if(card!=nullptr) delete card;
    }
}


void Hand::removeFour(Card &card) {
    vector<Card*>::iterator iter=hand.begin();
    Card* c;
    int i=0;
    while(iter<hand.end()) {
        if (card.getValue()/10 == (hand[i])->getValue()/10) {
            c = hand[i];
            hand.erase(iter);
            if(c!=nullptr)delete c;
        }
        iter++;
        i++;
    }
}


