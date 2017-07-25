#include "../include/Card.h"
#include <iostream>


using namespace std;

Card::Card() : shape(), value(0){}

Card::Card(std::string &c) : shape(), value(0) {
    if (c.at(c.length() - 1) == 'C') {
        shape = Club;
        value = 1;
    }
    if (c.at(c.length() - 1) == 'D') {
        shape = Diamond;
        value = 2;

    }
    if (c.at(c.length() - 1) == 'H') {
        shape = Heart;
        value = 3;
    }

    if (c.at(c.length() - 1) == 'S') {
        shape = Spade;
        value = 4;
    }
}

Card::~Card() {}

string Card::getShape() const {
    if (shape == Club)
        return "C";
    if (shape == Spade)
        return "S";
    if (shape == Heart)
        return "H";
    return "D";
}

Card::Card(const Card& other) : shape(other.shape), value(other.value) {}

Card& Card::operator=(const Card& other) {
    if(this == &other) {
        return *this;
    }
    shape = other.shape;
    value = other.value;
    return *this;
}
