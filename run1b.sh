java -cp bin/:../FlexSC/lib/*:../FlexSC/bin util.GenRunnable ../FlexSC task1.task1b.Task1b -c data/case_chr2_29504091_30044866_part1.txt -t data/control_chr2_29504091_30044866_part1.txt $1 $2 &

java -cp bin/:../FlexSC/lib/*:../FlexSC/bin util.EvaRunnable ../FlexSC task1.task1b.Task1b -c data/case_chr2_29504091_30044866_part2.txt -t data/control_chr2_29504091_30044866_part2.txt $1 $2
