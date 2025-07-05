import sys
from collections import deque
class Solution:
    def position(self, n: int, number: int) -> List[int]: #[x, y]
        # number 6 where n is 6 then row is first from last
        # row = 5 - 0 = 5
        irow = (number - 1) // n
        arow = (n - 1) - irow

        # col will be (number - 1) % n when direction left -> right
        # else call will be (n - 1) - (number -1) % n

        # left -> right
        icol = (number - 1) % n
        acol = icol
        if irow % 2 == 1:
            acol = (n - 1) - icol

        return [arow, acol]


    def snakesAndLadders(self, board: List[List[int]]) -> int:
        n = len(board) # n * n matrix board
        # assert self.position(6, 1) == [5, 0]
        # assert self.position(6, 2) == [5, 1]
        # assert self.position(6, 12) == [4, 0]
        # assert self.position(6, 36) == [0, 0]
        # assert self.position(6, 21) == [2, 3]

        visited = [False for _ in range(n ** 2 + 1)]
        visited[1] = True
        queue = deque()
        queue.append([1, 0])

        while len(queue) != 0:
            pos, moves = queue.pop()

            if pos == n ** 2:
                return moves

            for nextPos in range(pos + 1, min(pos + 6, n ** 2) + 1):
                finalPos = nextPos
                x, y = self.position(n, nextPos)
                skip_to = board[x][y]
                if skip_to != -1:
                    finalPos = skip_to

                if not visited[finalPos]:
                    queue.append([finalPos, moves + 1])
                    visited[finalPos] = True

        return -1
        