from collections import Counter
from typing import List

# There are n amazon server with their powers.
# We can to select maximum server such that when we keep the in circle. The adjacent server have power
# difference of 1 or less.
def max_amazon_servers(powers: List) -> int:
    print(sorted(powers))
    count = Counter(powers)
    print(count)

    unique_powers = sorted(count.keys())
    max_servers = 1
    local_max_server = 0
    server_set = []
    whole_set = []
    for i in range(len(unique_powers)):
        power = unique_powers[i]
        max_servers = max(max_servers, count[power])

        prev_power = unique_powers[i - 1] if i - 1 >= 0 else -1
        next_power = unique_powers[i + 1] if i + 1 < len(unique_powers) else -1

        # current one is middle power need to appear at least twice
        if prev_power == power - 1 and power + 1 == next_power: 
            if count[power] >= 2: # add in the local_max_server
                local_max_server += count[power]
                server_set.append([power, count[power]])
            else: # only single server so can't be used as middle power server eg. 1, 2, 3 but 1, 2, 3, 2 will work
                server_set.append([power, count[power]])
                max_servers = max(max_servers, local_max_server + 1)
                whole_set.append(server_set)

                server_set = []
                server_set.append([power, count[power]])
                local_max_server = count[power]
        elif prev_power == power - 1: # last power in sequence
                local_max_server += count[power] # last power in sequence can work with being only one or more just add
                server_set.append([power, count[power]])
                whole_set.append(server_set)

                server_set = []
        elif power + 1 == next_power: # first power in sequence
             local_max_server = count[power]
             server_set.append([power, count[power]])
        else:
             local_max_server = count[power]
             server_set.append([power, count[power]])
             whole_set.append(server_set)

             server_set = []
             
             

        max_servers = max(max_servers, local_max_server)

        print(power, prev_power, next_power, max_servers, local_max_server)
    
    for i in range(len(whole_set)):
        print(f"Whole set number {str(i + 1)}: {whole_set[i]}")

    return max_servers



print(max_amazon_servers([2, 7, 1, 5, 2, 2, 3, 2, 2])) # expected 1, 2, 2, 3, 2, 2
print(max_amazon_servers([1, 2, 3, 4, 5, 4, 3, 2])) # expected 1, 2, 3, 4, 5, 4, 3, 2
print(max_amazon_servers([1, 2, 3, 4, 5, 4, 3])) # expected 2, 3, 4, 5, 4, 3
print(max_amazon_servers([1, 2, 3, 4, 5, 6, 5, 4, 3, 2])) # expected 2, 3, 4, 5, 4, 3
print(max_amazon_servers([1, 2, 3, 2, 1, 5, 6])) # expected 2, 3, 4, 5, 4, 3
print(max_amazon_servers([1, 2, 3, 10, 11, 12, 10, 11, 12])) # expected 2, 3, 4, 5, 4, 3
print(max_amazon_servers([15, 8, 4, 10, 7, 10, 13, 1, 13, 11, 11, 10, 1, 9, 9])) # expected 2, 3, 4, 5, 4, 3
print(max_amazon_servers([6, 5, 7, 6, 5, 13, 2, 4, 15, 12, 12, 13, 11, 10])) # expected 2, 3, 4, 5, 4, 3
print(max_amazon_servers([5, 6, 6, 8, 8, 4, 2, 1, 10, 5, 2, 7, 8, 1]))


# 1, 2, 3, 2

#                         1, 2, 2, 3, 3, 4, 5, 5, 5, 6, 7

# local max server        1,    3     5, 6, 4        5  2