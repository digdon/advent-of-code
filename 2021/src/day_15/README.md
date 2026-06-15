# Day 15: Chiton

First time ever implementing Dijkstra. I've seen it used plenty of times, but I've never taken the time to implement it myself. [Dijkstra's algorithm](https://en.wikipedia.org/wiki/Dijkstra%27s_algorithm) gave me the lowdown. I opted for the version with a priority queue, which was ultimately a great choice, given what part 2 entailed. And, instead of filling up the queue with all of the nodes, I opted for adding only the source node to start and added every neighbour when the current cost to the neighbour is less than the recorded cost.
