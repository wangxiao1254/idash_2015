struct Task1aAutomated@m{};

void Task1aAutomated@m.funct(int@m[public 1] alice_data,
      int@m[public 1] bob_data,
      int@m[public 1] ret,
      public int@m total_instances,
      public int32 test_cases) {
   int@m total = total_instances;
   int@m half = total_instances/2;
   for(public int32 i = 0; i < test_cases; i = i + 1) {
      ret[i] = alice_data[i]+bob_data[i];
      if(ret[i] > half)
         ret[i] = total-ret[i];
   }
}
