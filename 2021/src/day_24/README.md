# Day 24: Arithmetic Logic Unit

Let me start by stating, again, that I hate, hate, hate these sorts of puzzles. They aren't coding puzzles, they're data analysis puzzles. In fact, no coding was required for this one at all.

So, we need to build a 'computer' and feed a bunch of 14 digit numbers to a particular 'program', looking for a specific end value. That, of course, isn't really feasible, so we need to see if we can trim the search space down some. For this, we need to examine the actual program itself. In fact, the puzzle pretty much tells us to do exactly this when it said, "MONAD imposes additional, mysterious restrictions on model numbers, and legend says the last copy of the MONAD documentation was eaten by a tanuki. You'll need to figure out what MONAD does some other way."

I started by implementing the ALU, generating some sets of digits, passing them in, and logging the register values after each step. A lot of the 'code' does practically nothing. However, 'z' tends to be multiplied by 26 on some occasions and divided by 26 on other occasions. Since we need 'z' to be 0 at the end, obviously this is important.

Grabbing some paper, I mapped out exactly what each set of instructions is doing and tried to reduce it. For my puzzle input, it amounts to something like this (where i is input digit 1-14):
1. z = i1 + 10 (this is actually z \* 26 + i1 + 10, but since z is initially 0, only i1 + 10 matters)
1. z = z * 26 + i2 + 5
1. z = z * 26 + i3 + 12
1. z = z / 26 (huh, interesting)
1. z = z * 26 + i5 + 6
1. z = z / 26 * 26 + i6 + 4 (huh, even more interesting)

Because I hate these types of puzzles, I gave up pretty much at this point and headed over to [reddit](https://www.reddit.com/r/adventofcode/comments/rnejv5/2021_day_24_solutions/) for some suggestions.

There was a lot of discussion about using stacks, but I couldn't find any explanation as to why. There was also a lot of discussion 3 particular instructions in every set that ultimately controlled everything:
* instruction 5, which is either div z 1 or div z 26
* instruction 6, which is add x {value}
* instruction 16, which is add y {value}

The div controls whether we are dividing by 26 or not. This is fine. The add x is used to control whether multiplying z by 26, at the end of the set, happens or not. The add y is used at the end to add to z. There was also some mention that of the 14 sets of instructions, 7 have div z 1 and 7 have div z 26. Obviously that's important. But still.... what's this about stacks?

I eventually stumbled onto this [page](https://github.com/wilkotom/AoC2021/blob/main/day24/onpaper.txt). It's got a bunch of errors, but the gist was clear. Here's a basic breakdown:
1. When div z 1, that means we're only going to be multiplying z by 26. In this case, the add x {value} is of no use and can be ignored. However, the add y {value} is important. We'd better save that and which digit slot/instruction set it comes from
2. When div z 26, we're going to divide by 26. This always happens. However, the trick is switching off the multiply by 26 at the end. To do this, we need to set x to 0. This is done by using a previous value of y, adding the two together. We use this value to then work out the input digit. When x is 0, the multiply by 26 doesn't happen. And neither does the add z y (ie, the add y {value} is ignored)

And this is where the stack comes into play. Digging into my ALU program, I have this:

```
 1 - div z 1
 2 - div z 1
 3 - div z 1
 4 - div z 26
 5 - div z 1
 6 - div z 26
 7 - div z 1
 8 - div z 26
 9 - div z 1
10 - div z 1
11 - div z 26
12 - div z 26
13 - div z 26
14 - div z 26
```

To undo the z * 26 in step 4, I need the add y value from step 3. For step 6, I need the value from 5. Moving on, step 11 needs the value from 10, step 12 needs the value from 9. To keep track of all all of this, we use a stack. When we have div z 1, we push the add y value onto the stack. When we have div z 26, we pop a value from the stack and use it in our calculations.

Based on that, I was able to, by hand, work out stuff like this:

```
3 add y 12 - push i3 and 12 to stack

4 add x -12
  pop from stack - i3 and 12
  i3 + 12 (popped from the stack) - 12 = w
  i3 = w
```
What this tells me is that any value from 1-9 is valid for both digit 3 and digit 4.

Let's look at 5 and 6:

```
5 add y 6 - push i5 and 6 to stack

6 add x -2
  pop from stack - i5 and 6
  i5 + 6 - 2 = w
  i5 + 4 = w
```
If i<sub>5</sub> = 1, then w = 5. These are both within the 1..9 range, so they're both valid. So are 2/6, 3/7, 4/8, 5/9. 6 is not a valid number for i<sub>5</sub> because then w would be 10, which is invalid.

From there, it was easy to work through the rest of the digits and come up with the valid ranges for each digit. For part 1, we use the maximum values, so for 5 and 6, that would be 5 and 9, as shown above. For part 2, it's the minimum values, so again for 5 and 6, it would be 1 and 5. An interesting tidbit.... for 10/11, the only acceptable values are 1/9.

So there we have it.... a solution worked out on paper that required no coding whatsoever.

Of course, the point is to write some code. Naturally I used the ALU I'd already implemented to test the numbers I came up with by hand. But surely I could write something to work all of this out.

Grabbing the important instructions, setting up the stack, and flipping between div 1 and div 26 was pretty easy. I started with a pretty basic brute-force approach to calculating w - basically a pair of nested for loops, 1..9. The first loop worked through values for the popped digit slot. Take the number, add x, add the popped y. The second loop then just compared the values to see if they equaled the previous equation. If they did, we set i<sub>n</sub> and the current digit slot accordingly.

Of course, 2 nested 1..9 loops is inefficient, so I started looking as ways to reduce the range I needed to check. The first obvious move is that I can eliminate any iteration where n + x + y is less than 1 or greater than 9. That reduces the number of times I hit the inner loop. Still going through all values for n, though. Surely I can do better....

From there, using the value of x + y, I worked out the actual start and stop ranges for n, so rather than running through it 9 times, if there are only 3 allowable values, I only run through with those 3 values.

But, if I know the min and max allowable values for n, that's the min and max values for i<sub>n</sub> directly, I can then just directly work out the matching w value for the current digit.

```
addValue = addX + popped y
start = Math.max(1, 1 - addValue)
end = Math.min(9,  9 - addValue)
maxModel[popped i] = end           // other w (digit i)
maxModel[curr] = end + addValue    // current w
minModel[popped i] = start         // other w (digit i)
minModel[curr] = start + addValue  // current w
```
Boom. Less than 50 lines of code. I have to say that while I loathe these types of puzzles, I'm very satisfied with my coded solution.
