# Day 8: Seven Segment Search

I found day 6 part 2 tricky at first, until I thought about the data in a different way. That was nothing compared to day 8 part 2. :)

Part 1 was very easy - go through the output value, count up the instances of 1, 4, 7, and 8. We know which ones these are because they are of unique lengths.

Part 2 was a whole other animal.

I started off looking at the data and trying to figure out what information I could deduce. I had actually worked out what I thought was a solution, working out each wire assignment. Of course when I ran it, it failed miserably. I racked my brain for hours trying to figure it out and couldn't....

Like day 6, day 8 was an exercise in the description of the problem throwing in a little misdirection. I thought I was going to have to work out the actual wire assignments, but it turns out that that's not the case....

I followed a hint that talked about using sets to work it out. 1, 4, 7, and 8 were easy, of course, because they are each of a unique length. Then we have a group of 5-letter signals, and a group of 6-letter signals. Those were trickier. I grabbed some paper and manually worked out the distinguishing characteristics. For example, for the 5-letter signals:
* '3' contains c & f (which we will work out from '1')
* '5' contains signals b & d (which we will work out from '4', which consists of c & f and b & d)
* '2' contains neither of those full sets

Essentially, working out the b/d and c/f sets was ultimately enough to figure out everything that wasn't 1, 4, 7, or 8.

Of course, the final part is that the signals and the output values do not always match. To fix that, I had to sort all of those string values to have all of the letters in alphabetical order.

Glad to be done this one.... oi.
