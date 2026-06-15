# Day 22: Reactor Reboot

Part 1 was pretty simple and I originally took the obvious (and soon to be dumb) way to solve it. The cuboids are small enough that I just created a map for every point in each cuboid, adding and removing from the map as required. Count up the number of items in the map and presto.

Part 2, of course, was never going to be that simple (which part 1 alluded to). I thought about it off and on for a while and eventually abandoned it. While working on a puzzle in 2023 ([day 5](https://adventofcode.com/2023/day/5)) it occurred to me that I need to just keep track of cuboid definitions. The problem, of course, is what happens when one cuboid intersects with another cuboid. The obvious solution there (again, based on stuff I did in 2023 day 5) was to slice up a cuboid based on the intersection point of the new cuboid.

Let's say we have the following two steps:
1. on x=0..9,y=0..9,z=0..9
1. off x=-1..3,y=0..3,z=3..9

The first step is easy - we build a cuboid with x=0..9,y=0..9,z=0..9.

The second step is trickier. We're turning off a cuboid and the target intersects with the first one.

First, we work out the intersection. In this case, the intersecting cuboid is x=0..3,y=0..3,z=3..9. From there, we then split the first cuboid along the faces of the intersecting cube. That gives us:
* x=4..9,y=0..9,z=0..9
* x=0..3,y=4..9,z=0..9
* x=0..3,y=0..3,z=0..2

The resultant cuboid set contains all of the cubes of the original cuboid, minus the intersecting cubes. If the next cuboid is turning stuff off (as in the above step 2), we don't need to do anything else. If we're turning on the cuboid, we've cleared out space for it, so we just add it, as is, to the cuboid list. Of course, things get a little more complicated as we go because we keep breaking cuboids into smaller ones, so rather than just checking 1 or 2 cuboids for intersections, we check perhaps dozens, breaking up each one as we go.

It took me a long time to work out how to slice up a cuboid, but it turns out to be pretty simple.

Overall, while it took a lot of time and effort to work out the solution, I'm super pleased with how it turned out. Takes less than 30 ms to run.
