from typing import List
from queue import PriorityQueue

class Solution:
    def kSmallestPairs(self, nums1: List[int], nums2: List[int], k: int) -> List[List[int]]:
        pq = PriorityQueue()

        for i in range(len(nums1)):
            for j in range(len(nums2)):
                pq.put((nums1[i] + nums2[j], [nums1[i], nums2[j]]))

        result  = []
        for i in range(k):
            result.append(pq.get())

        return result