from typing import List
import bisect

class Solution:
    def lengthOfLIS(self, nums: List[int]) -> int:
        n = len(nums)
        lis = 0

        lis_seq = [None for _ in range(n)]
        for i in range(n):
            pos = bisect.bisect_left(lis_seq, nums[i])
            lis_seq[pos] = nums[i]
            lis = max(lis, pos)

        return lis


        