java -cp bin:lib/*:../../idash_competition/bin util.GenRunnable task1.task1b.std.Task1b -h -s ../../idash_competition/data/case_chr2_29504091_30044866_part1.txt -t ../../idash_competition/data/control_chr2_29504091_30044866_part1.txt &


java -cp bin:lib/*:../../idash_competition/bin util.EvaRunnable task1.task1b.std.Task1b -h -s ../../idash_competition/data/case_chr2_29504091_30044866_part2.txt -t ../../idash_competition/data/control_chr2_29504091_30044866_part2.txt
