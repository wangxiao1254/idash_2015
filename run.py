#!/usr/bin/python
import sys, subprocess


party = {'gen': "java -Xmx4000m -cp bin/:../FlexSC/lib/*:../FlexSC/bin util.GenRunnable",
'eva':"java -Xmx4000m  -cp bin/:../FlexSC/lib/*:../FlexSC/bin util.EvaRunnable"}

name = {'task1a':'task1.task1a.Task1a',
        'task1b':'task1.task1b.Task1b',
        'task2astd':'task2.task2a.std.Task2a',
        'task2abf':'task2.task2a.bf.Task2a',
        'task2bstd':'task2.task2b.std.Task2b',
        'task2bbf':'task2.task2b.bf.Task2b'}

cmd = party[sys.argv[1]]+" "+name[sys.argv[2]]+" "+" ".join(sys.argv[3:])
print "Command to run", cmd

subprocess.call(cmd, shell=True)
