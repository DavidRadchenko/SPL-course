import csv
import os
import sys
import sqlite3
import hotelWorker
import time


def main(args):

    condition = True
    flag = 0

    while os.path.isfile('cronhoteldb.db') and condition:
        db = sqlite3.connect('cronhoteldb.db')
        cursor = db.cursor()

        all = cursor.execute('''SELECT Tasks.TaskName, TaskTimes.DoEvery, Tasks.Parameter, TaskTimes.NumTimes, Tasks.TaskId FROM Tasks
                                                INNER JOIN TaskTimes ON Tasks.TaskId = TaskTimes.TaskId
                                                WHERE TaskTimes.NumTimes > ?''', (0,)).fetchall()

        if len(all) == 0:
            condition = False


        else:
            # happens only once during the first iteration
            if flag == 0:
                tupOfTimes = []
                for i in range(0, len(all)):
                    cursor.execute('UPDATE TaskTimes set NumTimes = NumTimes -1 WHERE TaskId = ?', (i,))
                    t = hotelWorker.dohoteltask(all[i][0], all[i][2])
                    tup = [all[i][4], t]
                    tupOfTimes.append(tup)
                flag = 1

            elif flag == 1:


                for index in range(0, len(all)):
                    if round(all[index][1] + tupOfTimes[all[index][4]][1]) == round(time.time()) and all[index][3] > 0:
                        cursor.execute('UPDATE TaskTimes SET NumTimes = NumTimes -1 WHERE TaskId = ?', (all[index][4],))
                        t = hotelWorker.dohoteltask(all[index][0], all[index][2])
                        lst = list(tupOfTimes[all[index][4]])
                        lst[1] = t
                        tupOfTimes[all[index][4]] = tuple(lst)
            db.commit()

if __name__ == '__main__':
    main(sys.argv)
