# Day 20: Infinite Elves and Infinite Houses

A surprisingly easy day....

For part 1, I looked at several different approaches. My first attempt involved brute-force calculating the number of presents each house would get by checking to see which elves would visit (house % elf). As soon as I had a sum that meant the target, I was done. Worked fine, but was very slow, of course. My house value was in the mid 700,000s, so it took a long time to get there. I was able to shorten this by calculating a higher starting house number. For me, `target / 10 / 4` resulted in too large a value (although it found it quickly), whereas `target / 10 / 5` took longer, but gave the correct value.

I had originally thought of using the Sieve of Eratosthenes method (typically used for finding primes), but opted for the first solution because I didn't want to use up a lot of memory. Curiosity got the better of me, though, and I decided to give it a go. Again, I didn't want to have to calculate values I knew I likely wouldn't need, so I went with `target / 10 / 3` as the maximum possible house number. Results in an array of about 1.1 million entries, which I was hoping to avoid, but it runs pretty quickly - about 12-14ms.

For part 2, I just used the same algorithm, but added in a house count check. It ran even faster.

_Some time later_

I wasn't happy with all of the memory usage, so I fiddled to see if I could work out a better starting house instead of starting with the 'elf' value. So, I figured out some math to work out a starting house closer to the answer, so rather than having to work through 1.1 million values, I narrow it down to about 450,000. This obviously uses a lot less memory and results in a faster execution time (reduced by about 50%). Good enough for now.

The lower and upper bounds I calculate happen to work for my puzzle input, but may not work for other values and may need to be adjusted.
