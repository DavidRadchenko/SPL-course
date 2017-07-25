#include "../include/Player.h"
#include "../include/Card.h"
#include "../include/Hand.h"
#include <vector>

string PlayerType1::toString() {
    return getName() + ": " + Hand::toString();
}

PlayerType1::PlayerType1(string &name, int playerNum, vector<Card *> *other) : Player(name, playerNum, other) {}


int PlayerType1::getValueOfCard() {
    vector<Card *> tmp = getCards();
    int count1 = 1;
    int cont = 1;
    Card *curr = tmp[0];
    int mostCard = curr->getValue();
    int n = tmp.size();
    for (int i = 1; i < n; i++) {
        if ((tmp[i]->getValue() / 10) == (curr->getValue() / 10)) count1++;
        else {
            curr = tmp[i];
            count1 = 1;
        }
        if (count1 >= cont) {
            cont = count1;
            mostCard = tmp[i]->getValue();
        }
    }
    return mostCard / 10;
}

int PlayerType1::getPlayerType() { return 1; }

PlayerType1::PlayerType1(const PlayerType1 &other) : Player(other) {}

/*PlayerType1::PlayerType1 &operator=(const PlayerType1 &other) {
    if(this == &other) {
        return *this;
    }
    //Player *tmp;
    //tmp = other.create();
    Player::operator=(other);
    return *this;
}
*/

Player *PlayerType1::create() {
    return new PlayerType1(*this);
}


PlayerType1::~PlayerType1() {}



