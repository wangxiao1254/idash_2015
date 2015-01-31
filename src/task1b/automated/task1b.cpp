struct Task1b{};

float32[public 1] Task1b.func(
//      float32[public 200][public 3]alice_case,
//      float32[public 200][public 3]alice_control,
      float32[public 3] alice_case,
      float32[public 3] alice_control,
      float32[public 200][public 3]bob_case,
      float32[public 3] bob_control,
      public int32 testcases ) {
   float32[public 1] ret;
   float32 resCase;
   float32 resControl;
   float32 tmp;

   for(public int32 i = 0; i < testcases; i = i + 1) {
      float32[public 3] control_freq;
      float32[public 3] case_freq;
      for(public int32 j = 0; j < 3; j = j + 1) {
         tmp  = alice_control[j] + bob_control[j];
//         case_freq[j] = alicecase[i][j] + bobcase[i][j];
      }

      resCase = 0.0;
      resControl = 0.0;
      for(public int32 k = 0; k < 3; k = k + 1) {
         tmp = case_freq[k] + control_freq[k];
         resControl = resControl +  (control_freq[k]*control_freq[k])/tmp;
         resCase = resCase +  (case_freq[k]*case_freq[k])/tmp;
      }
      ret[i] =( resCase + resControl ) / 200;
   }
   //   return ret;
}
