
java -cp bin/:../FlexSC/lib/*:../FlexSC/bin util.GenRunnable ../FlexSC task2.task2a.$1.Task2 -f data/hu604D39.snp $2 -p 10 &

java -cp bin/:../FlexSC/lib/*:../FlexSC/bin util.EvaRunnable ../FlexSC task2.task2a.$1.Task2 -f data/hu661AD0.snp $2 -p 10
