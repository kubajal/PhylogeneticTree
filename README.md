# PhylogeneticTree

Calling convention: PhylogeneticTree sequences.fasta matrix.txt.

matrix.txt format:
- i) all first lines starting with '#' are comments 
- ii) first non-comment line contains alphabet (letters are space-separated)
- iii) |sigma| lines follow
- iiia) line i contains |sigma| integer values
- iiib) k-th value corresponds to the cost of replacing the i-th letter by the k-th

sequences.fasta includes multiple sequences to cluster

This program computes the phylogenetic tree of a given sequences set.
Algorithm:
initialization - global alignment of all given sequences is computed pairwise (using the given weight matrix). Scores of those global alignment are then into the pool.
1) the pair <a, b> with the biggest score is chosen from the pool and erased from it (all other pairs containing a or b are also erased)
2) the sequencesa a and b are merged into one virtual sequence s'. Scores of all pairs <s', c> are computed and inserted into the pool
3) if there is more than one pair in the pool go 1), else go 4)
4) merge the last pair into the a virtual sequence (root of the phylogenetic tree) 

docs: https://kubajal.github.io/PhylogeneticTree/
