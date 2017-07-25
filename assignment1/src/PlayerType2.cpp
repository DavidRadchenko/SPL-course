#include "../include/Player.h"
#include <vector>
#include "../include/Card.h"
#include "../include/Hand.h"

string PlayerType2::toString() {
    return getName() + ": " +Hand::toString();
}

PlayerType2::PlayerType2(string& name, int playerNum ,vector<Card *>* other) : Player(name,playerNum,other) {}

/*int PlayerType2::getValueOfCard() {
    vector<Card *> tmp = gettmp();
    int count1 = 1;
    int cont = 1;
    int n = tmp.size();
    Card *curr = tmp[n-1];
    int mostCard = curr->getValue() ;
    for (int i = n - 2; i > -1; i--) {
        if ((tmp[i]->getValue() / 10) == (curr->getValue() / 10)) count1++;
        else {
            curr = tmp[i];
            if (count1 <= cont) {
                cont = count1;
                mostCard = tmp[i]->getValue();
            }
            count1 = 1;
        }
    }
    return mostCard / 10;
}*/
/*int PlayerType2::getValueOfCard() {
    vector<Card *> tmp = gettmp();
    int n = tmp.size();
    int count1 = 1;
    int cont = n - 1;
    Card *curr = tmp[n-1];
    int mostCard = curr->getValue() ;
    for (int i = 1; i < n; i++) {
        if ((tmp[i]->getValue() / 10) == (curr->getValue() / 10)) count1++;
        else {
            if (count1 <= cont) {
                cont = count1;
                mostCard = tmp[i]->getValue();
            }
        }
            count1 = 1;
    }
    return mostCard / 10;
}
*/
int PlayerType2::getValueOfCard() {
    vector<Card *> tmp = getCards();
    int cont = 4;
    int value = tmp[0]->getValue();
    int count = 1;
    int mostCard = value;
    int n = tmp.size();
    for (int i = 1; i < n; i++) {
        if (tmp[i]->getValue() / 10 == value / 10)
            count++;
        else {
            if (count < cont) {
                cont = count;
                mostCard = value;
            }
            count = 1;
            value = tmp[i]->getValue();
        }
    }
    if (count < cont)
        mostCard = value;
    return mostCard / 10;
}


int PlayerType2::getPlayerType() {return 2;}

PlayerType2::PlayerType2(const PlayerType2 &other): Player(other){}

/*PlayerType2::PlayerType2 &operator=(const PlayerType2 &other) {
    if(this == &other) {
        return *this;
    }
    Player::operator=(other);
    this->StrategyNum = other.StrategyNum;
    return *this;
}*/

Player* PlayerType2::create() {
    return new PlayerType2(*this);
}

PlayerType2::~PlayerType2() {}



