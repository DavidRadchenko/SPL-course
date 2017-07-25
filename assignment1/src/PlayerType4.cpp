#include "../include/Player.h"
#include <vector>
#include "../include/Card.h"
#include "../include/Hand.h"


string PlayerType4::toString() {
    return getName() + ": " +Hand::toString();
}


PlayerType4::PlayerType4(string &name, int playerNum, vector<Card *> *other) : Player(name, playerNum, other) {}

int PlayerType4::getValueOfCard() {
    vector<Card *> hand = getCards();
    int ans = hand[0]->getValue();
    return (ans / 10);
}

int PlayerType4::getPlayerType() { return 4; }


PlayerType4::PlayerType4(const PlayerType4 &other) : Player(other) {}

/*PlayerType4::PlayerType4 &operator=(const PlayerType4 &other) {
    if (this == &other) {
        return *this;
    }
    Player::operator=(other);
    return *this;
}
*/
Player* PlayerType4::create() {
    return new PlayerType4(*this);
}

PlayerType4::~PlayerType4() {}



