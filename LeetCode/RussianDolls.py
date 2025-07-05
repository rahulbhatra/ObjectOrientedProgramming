import bisect
from typing import List
class Solution:
    def maxEnvelopes(self, envelopes: List[List[int]]) -> int:
        n = len(envelopes)
        
        lis = []
        for i in range(0, n):
            pos = bisect.bisect_left(lis, envelopes[i])

            if pos < len(lis):
                lis.insert(envelopes[i], pos)
            else:
                lis.append(envelopes[i])

            print(lis)

        return len(lis)



        # [[1, 1], [1, 10], [2, 5], [5, 4], [6, 4], [6, 7], [7, 11]]