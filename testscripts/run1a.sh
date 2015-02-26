./runGen.sh task1.task1a.Task1a -c data/case_chr2_29504091_30044866_part1.txt  -t data/control_chr2_29504091_30044866_part1.txt $1 &

./runEva.sh task1.task1a.Task1a -c data/case_chr2_29504091_30044866_part2.txt -t data/control_chr2_29504091_30044866_part2.txt $1
