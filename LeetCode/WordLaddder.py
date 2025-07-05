from typing import List

from queue import Queue, PriorityQueue
from itertools import permutations, combinations

class Solution:
    def ladderLength(self, beginWord: str, endWord: str, wordList: List[str]) -> int:
        queue = Queue()
        queue.put((0, beginWord))

        minCount = len(wordList) + 1
        visited = set()
        while not queue.empty():
            count, word = queue.get()

            if word == endWord:
                minCount = min(minCount, count)
            else:
                for word_l in wordList:
                    if word_l in visited:
                        continue

                    diff = 0
                    for i in range(len(word)):
                        if word[i] != word_l[i]:
                            diff += 1

                    if diff == 1: #convert to another word
                        queue.put((count + 1, word_l))
                        visited.add(word_l)

        return minCount if minCount != len(wordList) + 1 else 0

            