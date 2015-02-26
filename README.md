Summary
================
Step 1: Config.conf and replace localhost to the other party's IP
Step 2: run the following command on both machine, one with [party]=gen, one with [party]=eva on same tasks:

./run.py [party] [tasks] [more options]

where 
[party] can be gen or eva
[tasks] can be task1a, task1b, task2astd, task2abf, task2bstd, task2bbf
[more options] is used to specify datafiles and more.


Detail
====================
**task1a: Computing MAF.**
---------------------
      [more options]: -c casefile -t controlfile [ -a ]
      -c and -t are used to specify case and control file, -a is used to specify if we will run the circuit constructed manually or generated automatically by compiler With -a, it will use automatically generated circuit.

**task1b: Computing Chi Square.**
---------------------
      [more options]: -c casefile -t controlfile [ -a | -h ]
      meaning is similar to the above
      when -a is not used, -h can be used to have higher precision.

**task2astd: Computing Hamming Distance Standard**
---------------------
      [more options]: -f data [-a]
      -f is used to specify the input file, -a is used similarly as before

**task2abf: Computing Hamming Distance BloomFilter**
---------------------
      [more options]: -f file -p NUMBER
      -f is used to specify the input file, -p is used to specify approximation quality followed with an integer from 10 to 120. Default value is 20 is no value is provide. the larger the NUMBER used, the better.

**task2bstd: Computing Edit Distance Standard**
---------------------
      [more options]: -f data [-a]
      -f is used to specify the input file, -a is used similarly as before

**task2bbf: Computing Edit Distance BloomFilter**
---------------------
      [more options]: -f file -p NUMBER
      -f is used to specify the input file, -p is used to specify approximation quality followed with an integer from 10 to 120. Default value is 20 is no value is provide. the larger the NUMBER used, the better.


Usage example
=======================
task1a:
---------------------
  - Run manually created circuit:
      - Generator: ./run.py gen task1a -c path_case1 -t path_control1
      - Evaluator: ./run.py eva task1a -c path_case2 -t path_control2
  - Run automatically created circuit:
      - Generator: ./run.py gen task1a -c path_case1 -t path_control1 -a
      - Evaluator: ./run.py eva task1a -c path_case2 -t path_control2 -a

task1b:
---------------------
  - Run manually created circuit:
      Generator: ./run.py gen task1b -c path_case1 -t path_control1
      Evaluator: ./run.py eva task1b -c path_case2 -t path_control2
 -  Run automatically created circuit:
      Generator: ./run.py gen task1b -c path_case1 -t path_control1 -a
      Evaluator: ./run.py eva task1b -c path_case2 -t path_control2 -a
- Run manually created circuit with higher precision:
      Generator: ./run.py gen task1b -c path_case1 -t path_control1 -h
      Evaluator: ./run.py eva task1b -c path_case2 -t path_control2 -h

task2a:
---------------------
 -  Run manually created circuit:
      Generator: ./run.py gen task2astd -f path_file
      Evaluator: ./run.py eva task2astd -f path_file
 -  Run automatically created circuit:
      Generator: ./run.py gen task2astd -f path_file -a
      Evaluator: ./run.py eva task2astd -f path_file -a
  - Run manually created circuit with bloomfilter: (add -p NUMBER for a trade off between precision and speed)
      Generator: ./run.py gen task2abf -f path_file 
      Evaluator: ./run.py eva task2abf -f path_file


task2b:
---------------------
  - Run manually created circuit:
      Generator: ./run.py gen task2bstd -f path_file
      Evaluator: ./run.py eva task2bstd -f path_file
  - Run automatically created circuit:
      Generator: ./run.py gen task2bstd -f path_file -a
      Evaluator: ./run.py eva task2bstd -f path_file -a
  - Run manually created circuit with bloomfilter: (add -p NUMBER for a trade off between precision and speed)
      Generator: ./run.py gen task2bbf -f path_file 
      Evaluator: ./run.py eva task2bbf -f path_file

