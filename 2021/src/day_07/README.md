# Day 7: The Treachery of Whales

Part 1 was pretty simple. I started by working out the mean and calculating distances based on that. That didn't work, so then I worked out the standard deviation, removed outliers, and tried to calculate the mean from the remaining values. Again, that didn't work. Turns out the answer is just to calculate the median as the target position.

Part 2 was much trickier. Working out the costs between the current position and the target position is easy; it's just basic Gauss - (n * (n + 1)) / 2. Unlike part 1, which used the median, part 2 is based on the mean. However, the mean doesn't necessary work out to be a whole number. For that reason, I:
1. calculate the mean value
2. work out the fuel cost based on the floor of the mean value
3. work out the fuel cost based on the ceiling of the mean value

Once the two fuel costs are calculated, the answer is the lower of the two.
