# Day 19: Beacon Scanner

A bunch of what seems to be easy steps, but turned out to be perhaps one of the most difficult things ever. None of this should have been hard, and yet not only did I not find any really good solutions, everyone on Reddit was also complaining about how difficult this one was.

Finding scanners with overlaps should be easy. Calculate distances between pairs of beacons in each scanner, compare that to distances in another scanner. However, it was not. I tried using simple Manhattan distance, like so many others did, but it resulted in too many matches. As soon as I went to Euclidean distance, I was getting better results.

I was worried about rotations and all of the math involved, but because everything rotates 90 degrees, that makes things easier. I found this [lovely page](https://www.euclideanspace.com/maths/geometry/rotations/conversions/eulerToMatrix/examples/index.htm) that allowed me to generate a simple table.

So, we know how to find overlapping scanners (well, at least we think we do) and we know how to rotate them. Now the practically impossible part is checking to see if they're aligned. I tried about half a dozen different things, but nothing would bear fruit.

Used this as the main source of inspiration - https://github.com/zedrdave/advent_of_code/blob/master/2021/19/__main__.py
This one works by:
* looking at each pair of beacons from an anchor and the un-anchored scanner
* for each pair, look to see if there are more than 11 distance matches
* if there are, record the anchor and unanchored beacon pair
* if we record any pairs, rotate all of the unanchored beacons in that list, keeping track of the vector differences between the unanchored beacon and it's matching anchor beacon
* if we have more than 1 difference, we haven't yet found the correct rotation, so keep trying
* if we have a single diff, we've got the rotation
* rotate all of the unanchored beacons, translate to the proper position, and add this list to the list of anchored scanners
* if the unanchored scanner doesn't match any of the anchored ones, queue it up to try again

This is a weird one in that we cannot merge the beacons as we go. The gist of this solution is to align each scanner individually, and then combine all of the beacons together in a set once everything has been aligned. It works, but I'm not a fan.
