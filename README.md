# GlobalAlignment
Global alignment of two sequences DNA sequences using a given mismatch cost matrix.

Calling convention: GlobalAlignment sequences.fasta matrix.txt.

matrix.txt format:
- 1) all first lines starting with '#' are comments 
- 2) first non-comment line contains alphabet (letters are space separated)
- 3) |sigma| lines follow
- 3a) line i contains |sigma| integer values
- 3b) k-th value corresponds to the cost of replacing the i-th letter by the k-th
