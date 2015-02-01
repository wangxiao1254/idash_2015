import sys

for l1,l2 in zip(open(sys.argv[1]).readlines(),open( sys.argv[2]).readlines()):
   f1 = (float)(l1)
   f2 = (float)(l2)
   print abs(f1-f2);
   if abs(f1-f2) > 1e-3:
      print "!!!"
