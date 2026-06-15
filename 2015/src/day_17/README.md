# Day 17: No Such Thing as Too Much

I was stuck on this one for a long time, trying to figure out how to iterate through every combination. After working through 2025 day 10, I finally had an idea of how to tackle this. Of course, iterating through a lot of numbers is potentially a problem, but with the input at only 20 values, this meant a relatively small search space of 2^20 values, so the brute-force method seemed feasible. Indeed, it only takes about 70ms to run the whole thing (I solve part 1 and part 2 at the same time).

A workable solution and got me the stars, but the solution wouldn't really work for larger container counts, and that sort of irked me....

After spending a day thinking about it, I figured I could do it by maybe adding containers to a list of some sort and keeping track of things like running totals. I tried to do this without recursion, but that failed miserably. So, onto a recursion-based solution, which proved to be quite trivial. I started with a stack-based solution, but eventually realized I didn't need that sort of infrastructure. Now the whole thing runs in 1-2ms.
