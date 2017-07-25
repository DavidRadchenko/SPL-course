#include "../include/Card.h"
#include <iostream>

FigureCard::~FigureCard() {}

FigureCard::FigureCard() : Card(), figure(), N(0) {}

FigureCard::FigureCard(std::string &c, int n) : Card(c), figure(), N(n){
    if (c.at(0) == 'K') {
        figure = King;
        value = value + (N + 3) * 10;
    }

    if (c.at(0) == 'Q') {
        figure = Queen;
        value = value + (N + 2) * 10;

    }
    if (c.at(0) == 'J') {
        figure = Jack;
        value = value + (N + 1) * 10;
    }

    if (c.at(0) == 'A') {
        figure = Ace;
        value = value + (N + 4) * 10;
    }
}

int FigureCard::getValue() const {
    return value;
}

std::string FigureCard::toString() {
    string x = "A";
    if (figure == Jack)
        x = "J";
    if (figure == Queen)
        x = "Q";
    if (figure == King)
        x = "K";
    return x + "" + Card::getShape();
}

FigureCard::FigureCard(const FigureCard& other): Card(other), figure(other.figure), N(other.N) {}

FigureCard& FigureCard::operator=(const FigureCard& other) {
    if(this == &other) {
        return *this;
    }
    Card::operator=(other);
    this->figure = other.figure;
    return *this;
}

Card* FigureCard::create() {
    return new FigureCard(*this);
}

bool FigureCard::operator<(Card* c) {
    return value < c->getValue();
}