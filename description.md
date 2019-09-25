# Theoretical questions

##  Complexity of the original algorithm

For an hole with `m` boundary pixels and `n` pixels inside the hole,
the complexity of the naive algorithm is: `O(mn)`.

### Explanation:
	
* For each pixel inside the hole, the algorithm iterates over the `m` boundary pixels. 

* In the worst case, there are `O(n)` boundary pixels (diagonal hole). 
which results in runtime complexity  of `O(n^2)`

# Application:

In order to run the application do as follows:

1. Build `gradle build`
2. Extract distribution:
    1. `cd ./build/distributions`
    2.  `unzip lightricks.zip`
3. Run `./lightricks/bin/lightricks <path to img> <path to hole img> <connectivity> <Z> <Epsilon>`
    
* The hole is represented by the pixels `p`, s.t `I(p) != 255`.

Example:

    ./lightricks/bin/lightricks  ./srcImgExample.png ./holeShapeExample.png 8 10 0.01




	



  

