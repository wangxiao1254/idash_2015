please contact wangxiao@cs.umd.edu for any difficulties when running the program.

Summary
================
Step 1: open Config.conf and replace localhost to the other party's IP or Domain name
Step 2: run the following command on both machine, one with [party]=gen, one with [party]=eva on the same task as follows:

`./run.py [party] [tasks] [more options]`

where 
`[party]` can be gen or eva
`[tasks]` can be task1a, task1b, task2astd, task2abf, task2bstd, task2bbf
`[more options]` is used to specify datafiles and more.  -c is used for specify case data, -t is used to specify control
data, -f is used to specify data when there is only one input(task2), -h is used to switch to high precision(task1b),
-a is used for automatically generated circuits, -p NUMBER is used for a trade off between precision and speed (task2a/b controled by NUMBER);


Recommaend solution:
=======================
task1a(MAF):
---------------------
      Generator: `./run.py gen task1a -c path_case1 -t path_control1`

      Evaluator: `./run.py eva task1a -c path_case2 -t path_control2`

task1b(Chi^2):
---------------------
      Generator: `./run.py gen task1b -c path_case1 -t path_control1 -h`

      Evaluator: `./run.py eva task1b -c path_case2 -t path_control2 -h`

task2a(Hamming Distance) 
---------------------
add -p NUMBER to trade off speed for better approximation. e.g. -p 2 for large VCF

      Generator: `./run.py gen task2abf -f path_file`

      Evaluator: `./run.py eva task2abf -f path_file`

task2b(Edit Distance):
---------------------
add -p NUMBER to trade off speed for better approximation. e.g. -p 2 for large VCF

      Generator: `./run.py gen task2bbf -f path_file` 

      Evaluator: `./run.py eva task2bbf -f path_file`


Complete Usage example
=======================
task1a:
---------------------
  -  Exact solution (add -a for automatically generated circuits):

      Generator: `./run.py gen task1a -c path_case1 -t path_control1`

      Evaluator: `./run.py eva task1a -c path_case2 -t path_control2`

task1b:
---------------------
  - Run manually created circuit:

      Generator: `./run.py gen task1b -c path_case1 -t path_control1`

      Evaluator: `./run.py eva task1b -c path_case2 -t path_control2`

 -  Run automatically created circuit:

      Generator: `./run.py gen task1b -c path_case1 -t path_control1 -a`

      Evaluator: `./run.py eva task1b -c path_case2 -t path_control2 -a`

- Run manually created circuit with higher precision:

      Generator: `./run.py gen task1b -c path_case1 -t path_control1 -h`

      Evaluator: `./run.py eva task1b -c path_case2 -t path_control2 -h`

task2a:
---------------------
 -  Solution using oblivious merge (add -a for automatically generated circuits, use -p NUMBER for a trade off between precision and speed):

      Generator: `./run.py gen task2astd -f path_file`

      Evaluator: `./run.py eva task2astd -f path_file`


 -  Solution using bloom filter (add -a for automatically generated circuits, -p NUMBER for higher precision):

      Generator: `./run.py gen task2abf -f path_file`

      Evaluator: `./run.py eva task2abf -f path_file`


task2b:
---------------------
 -  Solution using oblivious merge (add -a for automatically generated circuits, use -p for a trade off between precision and speed):

      Generator: `./run.py gen task2bstd -f path_file`

      Evaluator: `./run.py eva task2bstd -f path_file`


 -  Solution using bloom filter (add -a for automatically generated circuits, -p NUMBER for higher precision):

      Generator: `./run.py gen task2bbf -f path_file`

      Evaluator: `./run.py eva task2bbf -f path_file`

Others
============================================
Compile the circuits:
All the circuits are precompiled and ready to run. If you would like to recompile
the circuits, please run:
java -cp ~/SCVM2/bin/ backend.flexsc.CommandTool [pathToSourceCode] compiledlib
