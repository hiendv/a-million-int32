# a-million-int32
## Sorting a million of signed 32-bit integers using 3 MB of memory

### Introduction
> Write a program in any programming language of your preference to sort 1 million of signed 32-bit integers using 3 MB of memory.

### Problems & Solutions
- I consider it 3MB of heap
- A million of signed 32-bit integers costs:  
4 * 1.000.000 bytes = **4MB**. It's impossible to push all of the values into the heap and sort them.  

**External sorting to the rescue !**
- Let the GC take care of the hard part.
- Read the input dataset, chunk it into *n* chunks, each costs less than 3MB and sort them before write them to the disk.
- Create a heap containing *n* integers, a map to keep track of seeking position for every chunk and a `lastPick` value.
- Fill the heap with *n* integers, find the minimum value `i`, print `heap[i]`, let `lastPick = i` and `skips[i] = heap[i].length + 1` (1 is for the \n)
- If the input stream of a chunk reach the end, let `skips[i] = -1` to ignore
- Repeat until the number of finished input streams equals to *n*

### Running
- Download the latest release https://github.com/hiendv/a-million-int32/releases
- Build the project (optional) or use the attached distribution
- `java -jar "a-million-int32.jar" input output capacity`.  E.g. `java -jar "a-million-int32.jar" /tmp/dataset /tmp/sorted 20000`  
 
### Java & Golang
At the beginning, I tried Go.
I was doing just fine until I found it impossible to understand the garbage collector. Memory leaking !
I have to admit that I'm new to Go, there are a lot to learn so I switched back to Java.
JVM also seems to be more friendly, pass `-Xmx3m -Xms3m` to limit the memory and you're good to go.

### Results & Profiling
It took 1 minutes and 10 seconds to finish the sorting.
The input file is located in my SSD while the tmp and output file are in the HDD.
#### Profiling will be updated later
