#!/usr/bin/env python

# calculate chi2 statistics
# input: two pairs of case & control from different instituions


import sys
import argparse


def read(sFile):
	lRes = []
	with open(sFile, 'r') as f:
		nNum = len(f.next().strip().split())
		for l in f:
			sSnpId = l.strip()
			m = {}
			for x in f.next().strip().split():
				if x[0] not in m:
					m[x[0]] = 0
				m[x[0]] += 1

				if x[1] not in m:
					m[x[1]] = 0
				m[x[1]] += 1

			lRes.append((sSnpId, sum(m.values()), m[min(m.keys())]))
			print min(m.keys()), m[min(m.keys())]
			print m

	return lRes


def chi2(args):
	lCs1 = read(args.case1)
	lCt1 = read(args.control1)
	lCs2 = read(args.case2)
	lCt2 = read(args.control2)

	for cs1,ct1,cs2,ct2 in zip(lCs1,lCt1,lCs2,lCt2):
		assert(cs1[0]==ct1[0]==cs2[0]==ct2[0])
		a = cs1[2] + cs2[2]
		b = cs1[1]-cs1[2] + (cs2[1]-cs2[2])
		c = ct1[2] + ct2[2]
		d = ct1[1]-ct1[2] + (ct2[1]-ct2[2])

		r = a + b
		s = c + d
		g = a + c
		k = b + d
		n = r + s

		chi2 = 1.0 * n * (a*d-b*c)**2 / (r*s*g*k)
		print cs1[0], ':\t',
		if args.v:
			print 'a', a, 'b', b, 'c', c, 'd', d, 'r', r, 's', s, 'g', g, 'k', k, 'n', n
		print chi2
	

if __name__ == '__main__':
	parser = argparse.ArgumentParser(description='calculate chi2 statistics')

	parser.add_argument('-cs1', '--case1', help='case file from institutions 1')
	parser.add_argument('-ct1', '--control1', help='control file from institutions 1')
	parser.add_argument('-cs2', '--case2', help='case file from institutions 2')
	parser.add_argument('-ct2', '--control2', help='case file from institutions 2')
	parser.add_argument('--v', action='store_true')
	parser.set_defaults(func=chi2)

	args = parser.parse_args()
	print args
	args.func(args)
