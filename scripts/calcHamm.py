#!/usr/bin/env python

# count the number of common SNPs in two files

import sys
import operator
import argparse


def calcHamm(args):
	mHamm = {}

	with open(args.input1, 'r') as f:
		for l in f:
			l = l.strip()
			if l.startswith('#'):
				continue

			r = l.split()
			k = (r[0], r[1])
			if 'DEL' in r[-1]:
				continue
			elif 'INS' in r[-1]:
				continue
			elif 'SNP' in r[-1]:
				v = (r[3], r[4], l)
			elif 'SUB' in r[-1]:
				v = (r[3], r[4], l)
			else:
				print 'wrong'
				sys.exit()

			if k not in mHamm:
				mHamm[k] = []
			mHamm[k].append(v)

	with open(args.input2, 'r') as f:
		for l in f:
			l = l.strip()
			if l.startswith('#'):
				continue

			r = l.split()
			k = (r[0], r[1]) # key is (chr,loc)
			if 'DEL' in r[-1]:
				continue
			elif 'INS' in r[-1]:
				continue
			elif 'SNP' in r[-1]:
				v = (r[3], r[4], l)
			elif 'SUB' in r[-1]:
				v = (r[3], r[4], l)
			else:
				print 'wrong'
				sys.exit()

			if k not in mHamm:
				mHamm[k] = []
			mHamm[k].append(v) # value is (ref,mut)

	nOne = 0
	nTwo = 0
	nHamm = 0
	for k in mHamm:
		if len(mHamm[k]) == 1:
			nOne += 1
			nHamm += 1
		elif len(mHamm[k]) == 2:
			nTwo += 1
			t0 = mHamm[k][0]
			t1 = mHamm[k][1]

			if t0[0] != t1[0]:
				if args.v:
					print 'incompatible records:', k, mHamm[k]
				continue
			else:
				if t0[1] != t1[1]:
					if args.v:
						print 'countable records:', k, mHamm[k]
					nHamm += 1
	
	print 'hamming distance is', nHamm


if __name__ == '__main__':
	parser = argparse.ArgumentParser(description='count # of common SNPs')

	parser.add_argument('-i1', '--input1', help='data 1')
	parser.add_argument('-i2', '--input2', help='data 2')
	parser.add_argument('--v', help='print records', action='store_true')
	parser.set_defaults(func=calcHamm)

	args = parser.parse_args()
	args.func(args)

