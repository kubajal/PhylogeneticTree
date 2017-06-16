# PhylogeneticTree

Calling convention: PhylogeneticTree sequences.fasta matrix.txt.

matrix.txt format:
- i) all first lines starting with '#' are comments 
- ii) first non-comment line contains alphabet (letters are space-separated)
- iii) |sigma| lines follow
- iiia) line i contains |sigma| integer values
- iiib) k-th value corresponds to the cost of replacing the i-th letter by the k-th

sequences.fasta includes multiple sequences to cluster
