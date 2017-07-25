#include <iostream>
#include <sstream>
#include "../include/Player.h"
#include "../include/Deck.h"
#include "../include/Game.h"
#include <fstream>
#include <cstring>
#include <stdlib.h>
#include <algorithm>

Game::Game(char *configurationFile)
        : players(), deck(), turnNumber(0), N(0), winner(""), lastPlayer(), verbal(0) {
    vector<string> data;
    string line;
    ifstream myfile(configurationFile);
    if (myfile.is_open())
        while (getline(myfile, line)) {
            if (line.length() > 0)
                if (line.at(0) != '#')
                    data.push_back(line);
        }
    myfile.close();

    verbal = atoi(data[0].c_str());
    N = atoi(data[1].c_str());

    deck = Deck(data[2], N);

    int n = data.size();
    for (int i = 3; i < n; i++) {
        char & s = data[i].at(data[i].length() - 1);
        string name = data[i].substr(0, data[i].length() - 2);
        Player *p;
        if (s == '1') {
            vector<Card *> *empty = new vector<Card *>;
            p = new PlayerType1(name, i - 3, empty);
            players.push_back(p);
            delete (empty);
        }
        if (s == '2') {
            vector<Card *> *empty = new vector<Card *>;
            p = new PlayerType2(name, i - 3, empty);
            players.push_back(p);
            delete (empty);
        }
        if (s == '3') {
            vector<Card *> *empty = new vector<Card *>;
            p = new PlayerType3(name, i - 3, empty);
            players.push_back(p);
            delete (empty);
        }
        if (s == '4') {
            vector<Card *> *empty = new vector<Card *>;
            p = new PlayerType4(name, i - 3, empty);
            players.push_back(p);
            delete (empty);
        }
    }
    n = players.size();
    for (int i = 0; i < n; i++)
        lastPlayer.push_back(-1);

}

void Game::init() {
    int n = players.size();
    for (int i = 0; i < n; i++)
        for (int j = 0; j < 7; j++)
            if (deck.getNumberOfCards() > 0)
                players[i]->add(deck.fetchCard());
}

void Game::play() {
    while (winner == "") {
        int i = turnNumber % players.size();
        turnNumber += 1;
        int playerToAsk;
        int cardToAsk;
        if (players[i]->getPlayerType() == 1 || players[i]->getPlayerType() == 2)
            playerToAsk = strategy12(i);
        else
            playerToAsk = strategy34(i);
        cardToAsk = players[i]->getValueOfCard();
        if (verbal == 1) {
            string rightPlayer = "A";
            cout << "Turn " << turnNumber << "\n";
            printState();
            if (cardToAsk > N) {
                if (cardToAsk - N == 1)
                    rightPlayer = "J";
                if (cardToAsk - N == 2)
                    rightPlayer = "Q";
                if (cardToAsk - N == 3)
                    rightPlayer = "K";
                cout << players[i]->getName() << " asked " << players[playerToAsk]->getName() << " for the value "
                     << rightPlayer << "\n";
            } else
                cout << players[i]->getName() << " asked " << players[playerToAsk]->getName() << " for the value "
                     << cardToAsk << "\n";
            cout << "\n";
        }

        singleTurn(i, cardToAsk, playerToAsk);

        if (players[i]->isEmpty())
            this->winner = "***** The Winner is: " + players[i]->getName() + " *****";
        else if (players[playerToAsk]->isEmpty())
            this->winner = "***** The Winner is: " + players[playerToAsk]->getName() + " *****";
        if (players[i]->isEmpty() && players[playerToAsk]->isEmpty())
            winner = "***** The winners are: " +
                     players[playerToAsk]->getName()
                     + " and " + players[i]->getName() +
                     " *****" + "\n";
    }
}

int Game::strategy12(int me) {
    int player = 0;
    int n = players.size();
    if (me == player)
        player++;
    int i = 0;
    while (i < n) {
        if (players[i]->amountOfCardsAtHand() >= players[player]->amountOfCardsAtHand() && i != me)
            player = i;
        if (i + 1 == me) i = i + 2;
        else i++;
    }
    return player;
}

int Game::strategy34(int me) {
    int n = players.size();
    for (int i = 0; i < n; i++)
        if ((lastPlayer[me] + 1) % n != me) {
            lastPlayer[me] = (lastPlayer[me] + 1) % n;
            return lastPlayer[me];
        } else {
            lastPlayer[me] = (lastPlayer[me] + 2) % n;
            return lastPlayer[me];
        }
    return 0;
}

void Game::singleTurn(int index, int card, int player) {
    vector<Card *> tmp = players[player]->passCards(card);
    int n = tmp.size();
    if (n == 0 && deck.getNumberOfCards() > 0)
        players[index]->add(deck.fetchCard());
    else
        for (int i = 0; i < n; i++) {
            players[index]->add(tmp[i]);
            if (deck.getNumberOfCards() > 0)
                players[player]->add(deck.fetchCard());
        }
}

void Game::printState() {
    cout << "Deck: " << deck.toString() << "\n";
    int n = players.size();
    for (int i = 0; i < n; i++) {
        cout << players[i]->toString() << "\n";
    }
}

void Game::printWinner() { cout << winner; }

void Game::printNumberOfTurns() { cout<< "\n" << "Number of turns: " << turnNumber << "\n"; }

Game::Game(const Game &other)
        : players(), deck(), turnNumber(other.turnNumber), N(other.N), winner(other.winner), lastPlayer(),
          verbal(other.verbal) {
    Player *p;
    int k;
    int n = other.players.size();
    for (int i = 0; i < n; i++) {
        p = other.players[i]->create();
        players.push_back(p);
        k = other.lastPlayer[i];
        lastPlayer.push_back(k);
    }
    deck = other.deck;
}

Game::~Game() {
    int n = players.size();
    for (int i = 0; i < n; i++)
        delete players[i];
    players.clear();
}