from queue import Queue
from typing import List

class Solution:
    def minMutation(self, startGene: str, endGene: str, bank: List[str]) -> int:

        if endGene not in bank:
            return -1

        mutations = Queue()
        mutations.put((0, startGene))


        min_mutation = len(startGene) + 1
        while not mutations.empty():
            mutation_count, gene = mutations.get()
            
            print(mutation_count, gene)

            if gene == endGene:
                min_mutation = min(min_mutation, mutation_count)

            for i in range(len(gene)):
                if gene[i] != endGene[i]: #char is different
                    mutatedGene = gene[0:i] + endGene[i] + gene[i + 1:]

                    if mutatedGene in bank:
                        mutations.put((mutation_count + 1, mutatedGene))
                        print(mutation_count, mutatedGene)

        return -1 if min_mutation == len(startGene) + 1 else min_mutation
    


solution = Solution()
# solution.minMutation(startGene = "AACCGGTT", endGene = "AACCGGTA", bank = ["AACCGGTA"])
# solution.minMutation(startGene = "AACCGGTT", endGene = "AAACGGTA", bank = ["AACCGGTA","AACCGCTA","AAACGGTA"])
minMutation = solution.minMutation(startGene= "AACCGGTT", endGene= "AAACGGTA", bank= ["AACCGATT","AACCGATA","AAACGATA","AAACGGTA"])
print(minMutation)




