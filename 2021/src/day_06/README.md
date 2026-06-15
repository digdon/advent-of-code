# Day 6: Lanternfish

Part 1 was pretty easy and I took a fairly basic approach. Essentially, I just created an ever-expanding list of values, one for each fish. For each day, I decrement the value for each fish. If the fish is at 0, I reset that fish to 6, than add a new fish with a value of 8. Lather, rinse, repeat.

That worked great for the test data (3,4,3,1,2), but it rapidly became a problem with the full puzzle input. Keeping track of and iterating through a list with hundreds of thousands of entries became rather unwieldy. To fix this, I opted for using an array of ints. I create the array large and keep track of the number of fish in the array. When I reach the end of the existing array, I create a new one that is increased in size by 10240 items. Easy peasy lemon squeezy.

As is so often the case, part 2 made that solution for part 1 completely untenable - we'd be dealing with billions (and, in fact, trillions) of entries. I needed something else.

While venting about the problem, a colleague said, "you need to structure the data differently than how they give it to you."

CLICK

The number of values that each fish can currently have is limited - 0 to 8. That is, fish can only have values in that range. Rather than keeping track of what day each fish is on, what if we keep track of how many fish are on a particular day? Instead of a huge array, with lots of repeated values, I created a single array that is used to keep count of how many fish are at that particular day. On each day, I record how many are at 0, then move each day from day 1 to 8 down a position (ie, day 1 count moves to day 0, day 2 count moves to day 1, etc). For the number of day 0 entries, I then add those to day 6 and also set day 8 to that value.

Processing a list of 9 entries is much more efficient. :)
