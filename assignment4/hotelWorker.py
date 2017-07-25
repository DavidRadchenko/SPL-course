import os
import sys
import sqlite3
import time

def dohoteltask(taskname, parameter):
    db = sqlite3.connect('cronhoteldb.db')
    cursor = db.cursor()


    if taskname != 'clean':

        fullName = cursor.execute('SELECT FirstName, Lastname FROM Residents WHERE RoomNumber = ?', (parameter,)).fetchall()
        name = str(fullName[0][0]) + ' ' + str(fullName[0][1])
        if taskname == 'breakfast':
            print name[0:len(name) - 1] , 'in room number' , parameter , 'has been served breakfast at', time.time()
        else:
            print name[0:len(name) - 1] , 'in room number' , parameter , 'received a wakeup call at', time.time()

        return time.time()

    else:
        residentsRooms = cursor.execute('SELECT RoomNumber FROM Residents').fetchall()
        rooms = cursor.execute('SELECT RoomNumber FROM Rooms').fetchall()

        emptyRooms = list()
        count = 0
        for i in range(len(rooms)):
            for j in range(len(residentsRooms)):
                if rooms[i][0] != residentsRooms[j][0]:
                    count += 1
            if count == len(residentsRooms):
                emptyRooms.append(rooms[i][0])
            count = 0

        #print str
        print 'Rooms' , str(emptyRooms).strip('[]') , 'were cleaned at' , time.time()
        return time.time()


