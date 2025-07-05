from queue import PriorityQueue
from threading import Lock
class MedianFinder:

    def __init__(self):
        self.maxHeap = PriorityQueue() # all small values compare to top of the tree
        self.minHeap = PriorityQueue() # all bigger values compare to top of the tree
        self.lock = Lock()

    def addNum(self, num: int) -> None:
        with self.lock:
            if not self.maxHeap.empty() and -self.maxHeap.queue[0] > num:
                self.maxHeap.put(-num)
            else:
                self.minHeap.put(num)

            # print(f"Add num: {num} Max Heap: {self.maxHeap.queue} Min Heap: {self.minHeap.queue}")

            if self.maxHeap.qsize() > self.minHeap.qsize():
                max_num = -self.maxHeap.get()
                self.minHeap.put(max_num)
            if self.maxHeap.qsize() < self.minHeap.qsize():
                min_num = self.minHeap.get()
                self.maxHeap.put(-min_num)

        # print(f"Add num: {num} Max Heap: {self.maxHeap.queue} Min Heap: {self.minHeap.queue}")

    def findMedian(self) -> float:
        # print("find median", self.nums)
        with self.lock:
            n = self.minHeap.qsize() + self.maxHeap.qsize()
            if n % 2 == 0: # even
                return (-self.maxHeap.queue[0] + self.minHeap.queue[0]) / 2
            else:
                return -self.maxHeap.queue[0]