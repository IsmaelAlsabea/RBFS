import java.util.ArrayList;
import java.util.List;

class branch {
    int gx; // pathcost
    puzzle puz; /*
                 * each branch has its own copy of the state space because each branch will
                 * change the state space
                 */
    ArrayList<node> leafs; // will be updated after every move
    node zero; // zero is always node zero;
    List<dir> mtg; // move to goal state

    branch() {
        leafs = null;
        zero = null;
    }

    branch(node zero, puzzle puz) {
        this.zero = zero;
        this.leafs = new ArrayList<>();
        this.puz = puz;
        this.mtg = new ArrayList<>();
    }

    int pathCost() {
        return gx + calcMd();
    }

    int calcMd() {
        int md = 0;
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                md += puz.ss[i][j].dfg;
        return md;
    }

}