import sys

f = open(sys.argv[2])
f.readline()
f.readline()
f.readline()
f.readline()


error = list()
for l1,l2 in zip(open(sys.argv[1]).readlines(), f.readlines()):
   if len(l2.split()) > 1:
      f2 = float(l2.split()[1]) 
   else:
      f2 = (float)(l2)
   f1 = (float)(l1)
   error.append(abs(f1-f2))


print error
print max(error), sum(error)/len(error)
