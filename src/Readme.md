The Variable Size Bin Packing Problem (VSBPP) was first proposed by Friesen and Langston in 1986
as a generalization of the 1BPP. The aim is to minimize the cost of used bins of type k, to pack a set of items.
The used bins belongs to different sets of different sizes (k) but with the same cost.
For each set of bins there's an infinite number of bins that could be used, i.e.,
all items of size equal or less that the bin type k, could be packed in bins of bin type k.

The instance generator allow the generations of instances that can modify the problem complexity
and can be used to outperform the methods proposed so far. We consider the following papers to
parametrize the generator:

1. D. K. Friesen, M. A. Langston, Variable sized bin-packing, SIAM J. COMPUT 15 (1), 
iSSN (online): 1095-7111. doi:10.1137/0215016
2. V. Hemmelmayr, V. Schmid, C. Blum, Variable neighbourhood search for the variable sized bin packing problem, 
Computers & Operations Research 39 (2012) 10971108, iSSN: 0305-0548. doi:10.1016/j.cor.2011.07.003
3. M. Haouari, M. Serairi, Heuristics for the variable sized bin-packing problem, Computers & Operations Research 36 (2009) 
2877 – 2884, iSSN: 0305-0548. doi:10.1016/j.cor.2008.12.016.
4. I. Correia, L. Gouveia, F. S. da Gama, Solving the variable size bin packing problem with discretized formulations, 
Computers & Operations Research 35 (6) (2008) 2103 2113, iSSN: 0305-0548. doi:10.1016/j.cor.2006.10.014.
5. J. Bang-Jensen, R. Larsen, Efficient algorithms for real-life instances of the variable size bin packing problem,
Computers & Operations Research 39 (2012) 2848–2857
6. D. Pisinger, M. Sigurd, The two-dimensional bin packing problem with variable bin sizes and costs,
Discrete Optimization 2 (2005) 154–167

Some of the characteristics that we think still need attention for the VSBPP are:
- All items have the same weight, thing that might look like a trivial case but methods often fails in "easy" instances
- Bins capacities are somehow correlated with items weight so, selection method could be non-trivial
- There will be always wasted space in the bins since not always items weights are multiple of bins capacities  
- Cost of bins are not related linearly with the capacity, thus smaller bins might be expensive that the bigger ones.
- Different probability distributions for the items sizes that might increase the hardness of the packing methods

The generator for the Variable Size and Cost Bin Packing Problem has the following parameters:
- Items weights distribution:
  * [1] - Uniform (default)
  * [2] - Normal
      * a) Mean
      * b) Standard deviation
  * [3] - Gamma
      * a) Shape
      * b) Scale
  * [4] - Weibull
      * a) Alpha
      * b) Beta
  * [5] - Roulette (based on probability of occurrence, 1 - 100)
      * a) Probability for the heaviest part of the set
      * b) Probability for the medium part of the set
      * c) Probability for the smallest part of the set
- Bin types capacities distribution:
  * [1] - Uniform distribution
  * [2] - Normal distribution
    * a) Mean
    * b) Standard deviation
  * [3] - Gamma distribution
      * a) Shape
      * b) Scale
  * [4] - Weibull distribution
      * a) Alpha
      * b) Beta
  * [5] - Discrete with increment in range
      * a) Smallest bin type capacity
      * b) Increment of the capacity
  * [6] - Manual (default)
      * a) One line for each capacity
- Bin relation between cost and capacity
  * [1] - Linear
  * [2] - Concave
  * [3] - Convex
- Bin number by type bound:
  * [1] - Automatic (default)
  * [2] - Stricted
  * [3] - Fixed
The values of all the parameters should be set with those numbers in case of using an existing YML file.

Finally, this generator gives the instances in xls format, in order to be loaded with
any high level programming language with and with a higher level of understanding the data.
 
Also, and as a plus, we would liked to add the same instances in MathProg format,
ready to be loaded in any solver. We prefer GKPL, as it could be used in any OS, but
there are many others more powerful but with commercial licence as CPLEX, XPRESS and 
Gurobi. 