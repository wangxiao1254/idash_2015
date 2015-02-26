struct Task1bAutomated@n{};

float32[public n] Task1bAutomated@n.func(
      float32[public n][public 3] alice_case, float32[public n][public 3] alice_control,
      float32[public n][public 3] bob_case, float32[public n][public 3] bob_control) {
   float32[public n] ret;

   for(public int32 i = 0; i < n; i = i + 1) {
      float32 a = alice_case[i][0] + bob_case[i][0];
      float32 b = alice_case[i][1] + bob_case[i][1];

      float32 c = alice_control[i][0] + bob_control[i][0];
      float32 d = alice_control[i][1] + bob_control[i][1];

      float32 g = a + c, k = b + d;
      float32 tmp = a*d - b*c;
      tmp = tmp*tmp;
      ret[i] = tmp / (g * k);
   }
   return ret;
}
