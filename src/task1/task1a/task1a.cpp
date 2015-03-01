struct Task1aAutomated@m@n{};

void Task1aAutomated@m@n.funct(int@m[public n] alice_data, int@m[public n] bob_data,
      int@m[public n] ret, public int@m total_instances) {
   int@m total = total_instances;
   int@m half = total_instances / 2;
   for(public int32 i = 0; i < n; i = i + 1) {
      ret[i] = alice_data[i] + bob_data[i];
      if(ret[i] > half)
         ret[i] = total - ret[i];
   }
}
