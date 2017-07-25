#include "../include/Player.h"
#include <vector>
#include "../include/Card.h"
#include "../include/Hand.h"


string PlayerType3::toString() {
    return getName() + ": " +Hand::toString();
}

PlayerType3::PlayerType3(string &name, int playerNum, vector<Card *> *other) : Player(name, playerNum, other) {}

int PlayerType3::getValueOfCard() {
    vector<Card *> hand = getCards();
    int a = hand.size() - 1;
    int ans = hand[a]->getValue();
    return (ans / 10);
}

int PlayerType3::getPlayerType() { return 3; }

PlayerType3::PlayerType3(const PlayerType3 &other) : Player(other) {}
/*
PlayerType3::PlayerType3 &operator=(const PlayerType3 &other) {
    if (this == &other) {
        return *this;
    }
    Player::operator=(other);
    return *this;
}*/

Player* PlayerType3::create() {
    return new PlayerType3(*this);
}

PlayerType3::~PlayerType3() {}





