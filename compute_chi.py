#!/usr/bin/python
import sys
import numpy

f = open(sys.argv[1])

def tonums(s):
   ts = s.split();
   res = list()
   for item in ts:
      res.append((float)(item))
   return res

alicecase = tonums(f.readline())
alicecontrol = tonums(f.readline())
bobcase = tonums(f.readline())
bobcontrol = tonums(f.readline())

case = numpy.add(alicecase,bobcase)
control = numpy.add(alicecontrol,bobcontrol)

case2 = numpy.multiply(case, case)
control2 = numpy.multiply(control, control)
casecontrol = numpy.add(case, control)

#print case2, control2, casecontrol

case2cc = numpy.divide(case2, casecontrol)
control2cc = numpy.divide(control2, casecontrol)

print case2cc, control2cc;

print numpy.sum(numpy.add(case2cc, control2cc))/200
