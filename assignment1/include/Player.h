#ifndef PLAYER_H_
#define PLAYER_H_

#include <iostream>
#include <vector>
#include "../include/Hand.h"
#include "../include/Card.h"

using namespace std;

class Player : public Hand {
private:
    const string name;
    int playerIndex;   //our

public:

    Player(string &name, int playerNum, vector<Card *> *other);

    virtual ~Player();

    int getPlayerIndex();

    string getName();

    virtual string toString() = 0;

    virtual int getPlayerType() = 0;

    int amountOfCardsAtHand();

    //virtual int DrawFrom(vector<Player*>& players)=0;
    virtual int getValueOfCard() = 0;

    Player(const Player &other);

    Player &operator=(const Player &other);

    virtual Player* create() = 0;

};

class PlayerType1 : public Player {

public:
    virtual ~PlayerType1();

    virtual string toString();

    PlayerType1(string &name, int playerNum, vector<Card *> *other);

    virtual int getValueOfCard();

    virtual int getPlayerType();

    //virtual int DrawFrom(vector<Player *> &players);

    PlayerType1(const PlayerType1 &other);

    PlayerType1 &operator=(const PlayerType1 &other);

    virtual Player* create();
};

class PlayerType2 : public Player {


public:
    virtual ~PlayerType2();

    virtual string toString();

    PlayerType2(string &name, int playerNum, vector<Card *> *other);

    virtual int getValueOfCard();

    virtual int getPlayerType();

    PlayerType2(const PlayerType2 &other);

    //PlayerType2 &operator=(const PlayerType2 &other);

    virtual Player* create();
};

class PlayerType3 : public Player {

public:
    virtual ~PlayerType3();

    virtual string toString();

    PlayerType3(string &name, int playerNum, vector<Card *> *other);

    virtual int getValueOfCard();

    virtual int getPlayerType();

    PlayerType3(const PlayerType3 &other);

    //PlayerType3 &operator=(const PlayerType3 &other);

    virtual Player* create();
};

class PlayerType4 : public Player {

public:
    virtual ~PlayerType4();

    virtual string toString();

    PlayerType4(string &name, int playerNum, vector<Card *> *other);

    virtual int getValueOfCard();

    virtual int getPlayerType();

    PlayerType4(const PlayerType4 &other);

    //PlayerType4 &operator=(const PlayerType4 &other);

    virtual Player* create();
};

#endif
