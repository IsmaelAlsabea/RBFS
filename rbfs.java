import java.util.ArrayList;
import java.util.List;

class rbfs {

    node zero;
    puzzle is; // initial state

    rbfs(puzzle is, node zero) {
        this.is = is;
        this.zero = zero;
    }

    /**
     * instead of having 1 root for a tree, I am using 4
     * branches instead of a monolithic tree. because the state space will be altered by each
     * branch, so it is easier to write code that uses 4 branches (since the
     * maximum number of branches for a tree with this puzzle is 4 
     * each will have its own state space that can be altered without
     * affecting the state space of the other branches
     */
    private List<branch> initializeSolutionsArray() {
        List<branch> x = new ArrayList<>(); // max is 4
        branch j = new branch();
        // initial instantiation of tree branches.
        if (zero.ti.r + 1 < 3) {
            j = new branch(this.zero.deepCopy(), this.is.deepCopy());
            j.leafs.add(is.ss[zero.ti.r + 1][zero.ti.c].deepCopy());
            x.add(j);
        }
        if (zero.ti.r - 1 > -1) {
            j = new branch(this.zero.deepCopy(), this.is.deepCopy());
            j.leafs.add(is.ss[zero.ti.r - 1][zero.ti.c].deepCopy());
            x.add(j);
        }
        if (zero.ti.c + 1 < 3) {
            j = new branch(this.zero.deepCopy(), this.is.deepCopy());
            j.leafs.add(is.ss[zero.ti.r][zero.ti.c + 1].deepCopy());
            x.add(j);
        }
        if (zero.ti.c - 1 > -1) {
            j = new branch(this.zero.deepCopy(), this.is.deepCopy());
            j.leafs.add(is.ss[zero.ti.r][zero.ti.c - 1].deepCopy());
            x.add(j);
        }
        return x;
    }

    /**
     * the function below swap nodes with the zero node and calculate the move that
     * produces the smallest f, and then swaps back so it won't alter the state
     * space.
     */
    private int determineSmallestF(List<branch> x) {
        int sf = 1000; // smallest f
        int pc = 0; // pathCost
        for (int i = 0; i < x.size(); i++) {
            x.get(i).gx += 1;
            for (int w = 0; w < x.get(i).leafs.size(); w++) {
                x.get(i).puz.swap(x.get(i).zero, x.get(i).leafs.get(w)); // swap
                pc = x.get(i).pathCost(); // calc f
                if (pc < sf)
                    sf = pc;
                x.get(i).puz.swap(x.get(i).leafs.get(w), x.get(i).zero); // swap back
            }
            x.get(i).gx--;
        }
        return sf;
    }

    /**
     * the code below puts the next move in the mtg (moves to goal). this method will create a number of
     * branches based on the position of the zero tile, so if 0 was at
     * the right downward corner, there will be 2 branches one has the left node as
     * a root, and the other one has the upward node as the root node (of the branch), then it will
     * swap and check if the move produces the smallest f that was calculated
     * earlier. if the swap generates the smallest f then we make the move and put
     * the next batch of nodes in the leafs arraylist(expand the branch). Otherwise
     * we swap back and not expand the branch.
     */
    branch constructSolution() {
        List<branch> x = initializeSolutionsArray();
        int sf = 1000; // smallest f
        int pc = 0; // pathCost
        /*
         * flags to specify which nodes not to be taken into account since if we move
         * left, we don't consider right in the next move;
         */
        boolean right, left, down, up;
        tile til = null; // to determine the zero node next time
        while (true) {
            sf = determineSmallestF(x);
            right = down = left = up = true;

            for (int i = 0; i < x.size(); i++) {
                if (sf == 1200)
                    break;
                for (int w = 0; w < x.get(i).leafs.size(); w++) {
                    til = x.get(i).leafs.get(w).ti;
                    x.get(i).puz.swap(x.get(i).zero, x.get(i).leafs.get(w)); // swap
                    x.get(i).zero = x.get(i).puz.ss[til.r][til.c]; // keep track of where zero is.
                    x.get(i).gx++; // increment if (pc ==sf ) we will not decrement;
                    pc = x.get(i).pathCost(); // calc f
                    if (pc == sf) {
                        /*
                         * if leaf is below the zero node, then before the previous swap, zero was below
                         * leaf, and the swap made zero above this is the reason of the ! sign
                         */
                        if (x.get(i).leafs.get(w).ti.r == x.get(i).zero.ti.r) {
                            if (!(x.get(i).leafs.get(w).ti.c > x.get(i).zero.ti.c)) {
                                x.get(i).mtg.add(dir.r); // we move right
                                left = false; // we won't consider the left move in the next iteration.
                            } else if (!(x.get(i).leafs.get(w).ti.c < x.get(i).zero.ti.c)) {
                                x.get(i).mtg.add(dir.l);
                                right = false;
                            }
                        } else if (x.get(i).leafs.get(w).ti.c == x.get(i).zero.ti.c) {
                            if (!(x.get(i).leafs.get(w).ti.r > x.get(i).zero.ti.r)) {
                                x.get(i).mtg.add(dir.d);
                                up = false;
                            } else if (!(x.get(i).leafs.get(w).ti.r < x.get(i).zero.ti.r)) {
                                x.get(i).mtg.add(dir.u);
                                down = false;
                            }
                        } else {
                            System.out.println("error in constructSolution when swapping stuff");
                            System.exit(1);
                        }

                        if (isAtGoal(x.get(i)))
                            return x.get(i);
                        // expand the branch that has the leaf with the smallest f
                        putNextTilesInLeafsArrayList(x.get(i), down, up, right, left);
                        sf = 1200;
                        break; // we break since we got our smallest f move in our mtg array.
                    }
                    // swap back if solution was not considered //pruning action.
                    x.get(i).puz.swap(x.get(i).leafs.get(w), x.get(i).zero);
                    x.get(i).gx--;
                }
            }
        }
    }

    private void putNextTilesInLeafsArrayList(branch x, boolean down, boolean up, boolean right, boolean left) {
        x.leafs.clear();/*
                         * we clear because we already made the move and the leafs of the previous iteration should not
                         * be considered
                         */
        if (x.zero.ti.r + 1 < 3 && down) {
            x.leafs.add(x.puz.ss[x.zero.ti.r + 1][x.zero.ti.c]);
        }
        if (x.zero.ti.r - 1 > -1 && up) {
            x.leafs.add(x.puz.ss[x.zero.ti.r - 1][x.zero.ti.c]);
        }
        if (x.zero.ti.c + 1 < 3 && right) {
            x.leafs.add(x.puz.ss[x.zero.ti.r][x.zero.ti.c + 1]);
        }
        if (x.zero.ti.c - 1 > -1 && left) {
            x.leafs.add(x.puz.ss[x.zero.ti.r][x.zero.ti.c - 1]);
        }
    }

    private boolean isAtGoal(branch j) {
        if (j.calcMd() == 0)
            return true;
        else
            return false;
    }

    void solve(branch j) {
        is.display();// display initial state
        for (int i = 0; i < j.mtg.size(); i++) {
            is.movementController(zero, j.mtg.get(i));
            is.display();
        }
    }

}
