# Day 19: Medicine for Rudolph

Part 1 was pretty easy, of course, and I solved it by simply performing the substitutions manually and putting the resulting molecules into a set so I could count the number of unique values.

For part 2, I sort of recognized that compiler grammar might be involved, but I haven't done anything with that in a very long time, so I couldn't really wrap my head around how to tackle this. I saw some suggestions about going backwards - starting with the molecule and substituting bigger sections with simpler elements, working back to a final 'e' (via BFS or DFS). I also saw some suggestions about being greedy while doing this. All seemed pretty reasonable.

Then I saw [this comment](https://www.reddit.com/r/adventofcode/comments/3xflz8/comment/cy4etju/) on Reddit. Wow....
