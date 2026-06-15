# Day 9: Smoke Basin

Part 1 was pretty easy. For every grid point, I look to see if there's anything around it that's of a lower value. If the current point is smaller than everything around it, it's obvious the low point in that particular area.

Part 2 involves calculating the size of each drainage basin. To work that out, for every low point found, I generate a flood fill of the surrounding area, using values of 9 as the edge limits.
