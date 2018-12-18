
class puzzle {

    node[][] ss; // state space

    puzzle() {
        ss = new node[3][3];
    }

    void insert(node n) {
        ss[n.ti.r][n.ti.c] = n;
    }

    void movementController(node n, dir direction) {
        switch (direction) {
        case r:
            swap(n, ss[n.ti.r][n.ti.c + 1]);
            break;
        case l:
            swap(n, ss[n.ti.r][n.ti.c - 1]);
            break;
        case u:
            swap(n, ss[n.ti.r - 1][n.ti.c]);
            break;
        case d:
            swap(n, ss[n.ti.r + 1][n.ti.c]);
            break;
        default:
            System.out.println("error in movement Controller, it is taking input that shouldn't be there");
        }
    }

    void display() {
        System.out.println();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                System.out.print(ss[i][j].val);
                System.out.print((char) 9);
            }
            System.out.println();
        }
    }

    void swap(node n, node an) {
        tile temp = n.ti;
        n.ti = an.ti;
        n.dfg = n.calcDfg();
        an.ti = temp;
        an.dfg = an.calcDfg();
        insert(n);
        insert(an);
    }

    puzzle deepCopy() {
        puzzle k = new puzzle();
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                k.insert(ss[i][j].deepCopy());
        return k;
    }

}