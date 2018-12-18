
class node {
    int val;
    tile g, ti;
    int dfg; // distance from goal;

    node(int val, tile g, tile ti) {
        this.val = val;
        this.g = g;
        this.ti = ti;
        dfg = calcDfg();
    }

    node() { // for instantiation purposes
        val = 0;
        g = null;
        ti = null;
        dfg = 0;
    }

    node deepCopy() {
        node x = new node(this.val, new tile(this.g.r, this.g.c), new tile(this.ti.r, this.ti.c));
        return x;
    }

    int calcDfg() {
        int xd = 0, yd = 0, td = 0; // x distance, y distance, total distance
        xd = ti.r - g.r;
        yd = ti.c - g.c;
        if (xd < 0)
            xd *= -1;
        if (yd < 0)
            yd *= -1;
        td = xd + yd;
        return td;
    }

}