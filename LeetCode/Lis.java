package LeetCode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Lis {
    public int lengthOfLIS(int[] nums) {
        int n = nums.length;
        List<Integer> dp = new ArrayList<>(Collections.nCopies(n, 0));
        for(int i = 0; i < n; i++) {
            for (int j = i; j < n; j++) {
                if (nums[i] <= nums[j]) {
                    dp.set(j, Math.max(dp.get(j), dp.get(i) + 1));
                }
            }
        }
        System.out.println(dp);
        return Collections.max(dp);
    }
}
