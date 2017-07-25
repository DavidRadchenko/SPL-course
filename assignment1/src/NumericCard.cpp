#include "../include/Card.h"
#include <fstream>
#include <cstring>
#include <sstream>
#include "../include/Player.h"

#include <stdlib.h>


NumericCard::~NumericCard() {}

NumericCard::NumericCard() : Card(), number(0) {}

NumericCard::NumericCard(std::string &c) : Card(c), number(0) {
    std::string num = c.substr(0, c.length() - 1);
    number = atoi(num.c_str());
    value = value + (number * 10);
}

int NumericCard::getValue() const {
    return value;
}

std::string NumericCard::toString() {
    return to_string(number) + "" + Card::getShape();
}

NumericCard::NumericCard(const NumericCard& other): Card(other), number(other.number) {}

NumericCard& NumericCard::operator=(const NumericCard& other) {
    if(this == &other) {
        return *this;
    }
    Card::operator=(other);
    this->number = other.number;
    return *this;
}

Card* NumericCard::create() {
    return new NumericCard(*this);
}

bool NumericCard::operator<(Card* c) {
    return value < c->getValue();
}