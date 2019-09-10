The Decision Based Tree solver generates row specs by:
 1. choosing and removing a decision from the tree
 2. selecting an option from that decision
 3. adding the constraints from the chosen option to the root of the tree
    - adding the sub decisions from the chosen option to the root of the tree
 4. "pruning" the tree by removing any options from the tree that contradict with the new root node
    - any decisions that only have 1 remaining option will have that option also moved up the tree, and pruned again.
 5. restarting from 1, until there are no decision left
 6. creating a rowspec from the constraints in the remaining root node.