// m : bit length of every entry
// n : bit length of the result
struct Task2Automated@m@n{};

int@n Task2Automated@m@n.funct(int@m[public 1] key, public int32 length) {
   this.obliviousMerge(key, 0, length);
   int@n ret = 1;
   for(public int32 i = 1; i < length; i = i + 1) {
      if(key[i-1] != key[i])
         ret = ret + 1;
   }
   return ret;
}

void Task2Automated@m@n.obliviousMerge(int@m[public 1] key, public int32 lo, public int32 l) {
   if (l > 1) {
      public int32 k = 1;
      while (k < l) k = k << 1;
      k = k >> 1;
      for (public int32 i = lo; i < lo + l - k; i = i + 1)
         this.compare(key, i, i + k);
      this.obliviousMerge(key, lo, k);
      this.obliviousMerge(key, lo + k, l - k);
   }
}

void Task2Automated@m@n.compare(int@m[public 1] key, public int32 i, public int32 j) {
   int@m tmp = key[j];
   int@m tmp2 = key[i];
   if( key[i] < key[j] )
      tmp = key[i];
   tmp = tmp ^ key[i];
   key[i] = tmp ^ key[j];
   key[j] = tmp ^ tmp2;
}
