#include <iostream>
#include <vector>
#include "../include/Hand.h"
#include "../include/Card.h"
#include "../include/Player.h"

using namespace std;

Player::Player(string &name, int playerNum, vector<Card *> *other):
        Hand(other), name(name), playerIndex(playerNum){}

Player::~Player() {}

string Player::getName() {
    return name;
}

int Player::getPlayerIndex() {
    return playerIndex;
}

int Player::amountOfCardsAtHand() {
    return Hand::getNumberOfCards();
}

Player::Player(const Player &other): Hand(other), name(other.name), playerIndex(other.playerIndex) {}

/*Player & Player::operator=(const Player &other) {
    if(this == &other) {
        return *this;
    }
    Hand::operator=(other.hand);
    name = other.getName();
    playerIndex = other.playerIndex;
    return *this;
}



*/