struct Pair<T1, T2> {
   T1 left;
   T2 right;
};

struct bit {
   int1 v;
};

struct Int@n {
   int@n v;
};

struct BF_circuit{};

Pair<bit, Int@n> BF_circuit.add@n(int@n x, int@n y) {
   bit cin;
   Int@n ret;
   bit t1, t2;
   for(public int32 i=0; i<n; i = i+1) {
      t1.v = x$i$ ^ cin.v;
      t2.v = y$i$ ^ cin.v;
      ret.v$i$ = x$i$ ^ t2.v;
      t1.v = t1.v & t2.v;
      cin.v = cin.v ^ t1.v;
   }
   return Pair{bit, Int@n}(cin, ret);
}

int@log(n+1) BF_circuit.countOnes@n(int@n x) {
   if(n==1) return x;
   int@log(n-n/2+1) first = this.countOnes@(n/2)(x$0~n/2$);
   int@log(n-n/2+1) second = this.countOnes@(n-n/2)(x$n/2~n$);
   Pair<bit, Int@log(n-n/2)> ret = this.add@log(n-n/2+1)(first, second);

   int@log(n+1) r = ret.right.v;
   r$log(n+1)-1$ = ret.left.v;
   return r;
}

int@log(n+1) BF_circuit.merge@n(int@n x, int@n y) {
   int@n tmp;
   for(public int32 i = 0; i < n; i = i +1 ) {
      tmp$i$ = x$i$ | y$i$;
   }
   return this.countOnes@n(tmp);
}
