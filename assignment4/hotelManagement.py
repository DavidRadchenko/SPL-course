import csv
import os
import sys
import sqlite3


def main(args):
    inputfilename = args[1]
    myListOfTuples = list()
    with open(inputfilename) as inputfile:
        myListOfTuples = [tuple(line.split(',')) for line in inputfile.readlines()]

    if os.path.isfile('cronhoteldb.db'):
        return
    else:
        db = sqlite3.connect('cronhoteldb.db')
    cursor = db.cursor()
    cursor.execute(
        'CREATE TABLE TaskTimes (TaskId INTEGER PRIMARY KEY NOT NULL, DoEvery INTEGER NOT NULL, NumTimes INTEGER NOT NULL)')
    cursor.execute('''CREATE TABLE Tasks (TaskId INTEGER NOT NULL REFERENCES TaskTimes(TaskId),
                                          TaskName TEXT NOT NULL, Parameter INTEGER)''')
    cursor.execute('CREATE TABLE Rooms (RoomNumber INTEGER PRIMARY KEY NOT NULL)')
    cursor.execute('''CREATE TABLE Residents (RoomNumber INTEGER NOT NULL REFERENCES Rooms(RoomNumber),
                                           FirstName TEXT NOT NULL, LastName TEXT NOT NULL)''')
    count = 0
    for tup in myListOfTuples:
        if tup[0] == 'room':
            with db:
                cursor.execute("INSERT INTO Rooms VALUES(?) ", (tup[1],))
                if len(tup) > 2:
                    cursor.execute("INSERT INTO Residents VALUES(?,?,?)", (tup[1], tup[2], tup[3]))

        elif tup[0] == 'clean':
            with db:
                cursor.execute("INSERT INTO Tasks VALUES(?,?,?)", (count, tup[0], 0))
                cursor.execute("INSERT INTO TaskTimes VALUES(?,?,?) ", (count, tup[1], tup[2]))
                count = count + 1
        else:
            with db:
                cursor.execute("INSERT INTO TaskTimes VALUES(?,?,?)", (count, tup[1], tup[3]))
                cursor.execute("INSERT INTO Tasks VALUES(?,?,?)", (count, tup[0], tup[2]))
                count = count + 1

    db.commit()
    db.close()
if __name__ == '__main__':
    main(sys.argv)
