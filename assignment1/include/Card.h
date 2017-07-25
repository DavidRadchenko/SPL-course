#ifndef CARD_H_
#define CARD_H_

#include <iostream>
using namespace std;

enum Shape {
    Club,
    Diamond,
    Heart,
    Spade
};

enum Figure {
    Jack,
    Queen,
    King,
    Ace
};

class Card {
private:
    Shape shape;

protected:
    int value;

public:
    virtual string toString() = 0; //Returns the string representation of the card "<value><shape>" exp: "12S" or "QD"
    virtual ~Card();
    Card();
    Card(std::string& c);
    string getShape() const;
    virtual int getValue() const = 0 ;
    virtual bool operator<(Card* c) = 0;
    Card(const Card& other);
    virtual Card& operator=(const Card& other);

    virtual Card* create() = 0;

};


class FigureCard : public Card {
private:
    Figure figure;
    int N;

public:
    virtual ~FigureCard();
    FigureCard();
    FigureCard(std::string& c, int n);
    virtual int getValue() const;
    virtual string toString() override;
    virtual bool operator<(Card* c);

    virtual Card* create();


    FigureCard(const FigureCard& other);
    virtual FigureCard& operator=(const FigureCard& other);
};

class NumericCard : public Card {
private:
    int number;

public:
    virtual ~NumericCard();
    NumericCard();
    NumericCard(std::string& c);
    virtual int getValue() const;
    int getNumber() const;
    virtual string toString() override;
    virtual bool operator<(Card* c);

    virtual Card* create();

    NumericCard(const NumericCard& other);
    virtual NumericCard& operator = (const NumericCard& other);
};

#endif
