#!/usr/bin/env python

# count the number of common SNPs in two files

import sys
import operator
import argparse


def calcEdit(args):

	mEdit = {}

	with open(args.input1, 'r') as f:
		for l in f:
			l = l.strip()
			if l.startswith('#'):
				continue

			r = l.split()
			k = (r[0], r[1])
			if 'DEL' in r[-1]:
				v = (r[3], '')
			elif 'INS' in r[-1]:
				v = ('', r[3])
			elif 'SNP' in r[-1]:
				v = (r[3], r[4])
			elif 'SUB' in r[-1]:
				v = (r[3], r[4])
			else:
				print 'wrong'
				sys.exit()

			if k not in mEdit:
				mEdit[k] = []
			mEdit[k].append(v)

	with open(args.input2, 'r') as f:
		for l in f:
			l = l.strip()
			if l.startswith('#'):
				continue

			r = l.split()
			k = (r[0], r[1])
			if 'DEL' in r[-1]:
				v = (r[3], '')
			elif 'INS' in r[-1]:
				v = ('', r[3])
			elif 'SNP' in r[-1]:
				v = (r[3], r[4])
			elif 'SUB' in r[-1]:
				v = (r[3], r[4])
			else:
				print 'wrong'
				sys.exit()

			if k not in mEdit:
				mEdit[k] = []
			mEdit[k].append(v)

	nOne = 0
	nTwo = 0
	nWrong = 0
	nEdit = 0
	for k in mEdit:
		if len(mEdit[k]) == 1:
			nOne += 1
			t = mEdit[k][0]
			if t[0] != '' and t[1] != '':
				nEdit += len(t[1])
			elif t[0] == '':
				nEdit += 0 + len(t[1])
			elif t[1] == '':
				nEdit += 0 + len(t[0])
			else:
				print 'wrong'
				sys.exit()
		elif len(mEdit[k]) == 2:
			nTwo += 1

			n0 = 0
			t0 = mEdit[k][0]
			if t0[0] != '' and t0[1] != '':
				n0 += len(t0[1])
			elif t0[0] == '':
				n0 += 0 + len(t0[1])
			elif t0[1] == '':
				n0 += 0 + len(t0[0])
			else:
				print 'wrong'
				sys.exit()

			t1 = mEdit[k][1]
			n1 = 0
			if t1[0] != '' and t1[1] != '':
				n1 += len(t1[1])
			elif t1[0] == '':
				n1 += 0 + len(t1[1])
			elif t1[1] == '':
				n1 += 0 + len(t1[0])
			else:
				print 'wrong'
				sys.exit()

			if t0[0] != t1[0] or t0[1] != t1[1]:
				nWrong += 1

			if t0[0] == t1[0] and t0[1] == t1[1]:
				continue

			nEdit += max(n0, n1)
	
	print nOne
	print nTwo
	print nOne + nTwo*2
	print nWrong
	print nEdit


if __name__ == '__main__':
	parser = argparse.ArgumentParser(description='count # of common SNPs')

	parser.add_argument('-i1', '--input1', help='data 1')
	parser.add_argument('-i2', '--input2', help='data 2')
	parser.set_defaults(func=calcEdit)

	args = parser.parse_args()
	args.func(args)

