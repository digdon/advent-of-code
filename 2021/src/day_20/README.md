# Day 20: Trench Map

Day 20 was really unnecessarily sneaky and underhanded. "Here's the problem. Here's some example data to use to test your code. Hahaha, no matter what you've done, it won't work on the puzzle input."

The problem itself is pretty easy - there's a grid of pixels that, essentially, increases in size by one pixel in every direction every processing iteration. For each pixel, find all of the ones around it, use them to generate a number that maps into the enhancement algorithm, and use the mapped value to determine whether the target pixel should be lit or not in the current iteration.

Given the example data, this was very easy to implement. I just set up a hash set, using x,y as the key, to keep track of which pixels are lit. We know that any pixels outside of the input image area are unlit by default, so it was easy to include those 'infinite' pixels in the calculations.

However, it turns out the actual puzzle input had a nasty trick up its sleeve - enhancement value 0 is on and enhancement value 511 is off. What this means is that for one iteration, all of the infinitely unknown pixels are set to off. For the next iteration, they are all flipped to on, and back and forth. I should have expected something this ridiculous, but the thought just never occurred to me. I had to go to reddit (https://www.reddit.com/r/adventofcode/comments/rkf5ek/2021_day_20_solutions/) for help on this one.

In order to deal with this, I had to modify my solution to keep track of what iteration I'm on and what the infinity value is supposed to be for that iteration. As soon as I did that, everything came together.

Part 2 was basically take what you have and run it 48 more times. My solution is not optimal and runs longer than it should. I could work out some ways to make it run faster, but the whole premise of this puzzle annoyed the hell out of me and I can't be bothered to work on it more.

*Ok, I lied. I wasn't happy with the time, so I updated how I generate the key set. I switched from generating string values to using a Point object and the time reduction was significant, from several seconds to about 700ms.*