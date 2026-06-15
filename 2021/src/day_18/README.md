# Day 18: Snailfish

This was an interesting one. The puzzle absolutely screams "use trees for this!" and I was very tempted. Building the tree was probably going to be easy, but locating the previous and/or next number when dealing with explosions looked like it was going to be tricky. I elected to first try to solve this via basic string manipulation.

The string manipulation isn't all that tricky and, thankfully, the test data provided enough examples to work out all of the bugs. This one in particular - `[[[[4,3],4],4],[7,[[8,4],9]]] + [1,1]` - found all of the remaining issues in my code. The runtime for my solution is very short.

Part 2 was pretty easy - add each pair of snailfish, looking for the pair with the largest magnitude. Thankfully my reduction code runs fast. :)
