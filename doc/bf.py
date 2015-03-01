import numpy as np
import math
import sys
import pickle 

def singletrial(N, M, k):
#	bf = np.array([0 for i in range(M)])
#	for trial in range(k):
#		for elem in range(N):
#			bf[np.random.randint(0,M)] = 1

	bits = np.random.randint(0, M, k * N)
#	bf[bits] = 1		

#	occ = np.sum(bf)
	occ = len(np.unique(bits))
	occrate = occ/(M + 0.0)

	est =  math.log(1-occrate)/math.log(1-1/(M+0.0)) / k
	return est


if __name__ == "__main__":

	if len(sys.argv) > 1:
		N = int(sys.argv[1])
		M = int(sys.argv[2])
	else:
		N = 1000
		M = N * 10 

#	k = int((M+0.0)/(N+0.0) * 0.30102999566)
#	k = max(k, 1)
	k = 1

	print "k is", k

#	k = math.ceil((M+0.0)/(N+0.0) * 0.30102999566)
#	k = int(k)
#	print k

	numtrials = 10000
	percentile = 0.95

	alltrials = np.array([ singletrial(N, M, k) for i in range(numtrials)])

	error = (alltrials-N)/N
	print "expectation of error", sum(error)/numtrials
#	print "expectation of error", sum(error)/len(alltrials)
	error =  np.sort(np.abs(error))
	
	pickle.dump(error, open('out.txt','w'))

	print "average error is", np.mean(error)
	print "with 95% probability, the relative error is", error[numtrials * percentile]	
	print "with 99% probability, the relative error is", error[numtrials * 0.99]	
	

