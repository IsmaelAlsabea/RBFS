import java.io.File;
import java.util.Scanner;

public class driver {
    public static void main(String[] args) {

        File file = new File("/home/ismael/Desktop/Java VS Code/AI 4365/firstProg/data.txt");
        Scanner sc = null;
        try {
            sc = new Scanner(file);
        } catch (Throwable t) {
            t.printStackTrace();
        }
        int[][] container = new int[3][3];
        String str = "";
        char c = ' ';
        for (int i = 0; i < 3; i++) { // the i loop
            str = sc.nextLine();
            for (int j = 0; j < 3; j++) { // the J loop
                for (int r = 0; r < str.length(); r++) { // the R loop
                    c = str.charAt(r);

                    if (c == ' ')
                        continue;
                    else if (Character.isDigit(c))
                        break;
                }
                str = str.substring(str.indexOf(c) + 1, str.length());
                container[i][j] = Character.getNumericValue(c);
            }
        }
        tile s = new tile(-1, -1), g = new tile(-1, -1);
        puzzle is = new puzzle();
        node zero = new node(container[0][0], g, s);
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++) {
                s = new tile(-1, -1);
                g = new tile(-1, -1);
                if (container[i][j] == 0) {
                    g.r = 0;
                    g.c = 0;
                    s.r = i;
                    s.c = j;
                    zero = new node(0, g, s);
                } else if (container[i][j] == 1) {
                    g.r = 0;
                    g.c = 1;
                    s.r = i;
                    s.c = j;
                } else if (container[i][j] == 2) {
                    g.r = 0;
                    g.c = 2;
                    s.r = i;
                    s.c = j;
                } else if (container[i][j] == 3) {
                    g.r = 1;
                    g.c = 0;
                    s.r = i;
                    s.c = j;
                } else if (container[i][j] == 4) {
                    g.r = 1;
                    g.c = 1;
                    s.r = i;
                    s.c = j;
                } else if (container[i][j] == 5) {
                    g.r = 1;
                    g.c = 2;
                    s.r = i;
                    s.c = j;
                } else if (container[i][j] == 6) {
                    g.r = 2;
                    g.c = 0;
                    s.r = i;
                    s.c = j;
                } else if (container[i][j] == 7) {
                    g.r = 2;
                    g.c = 1;
                    s.r = i;
                    s.c = j;
                } else if (container[i][j] == 8) {
                    g.r = 2;
                    g.c = 2;
                    s.r = i;
                    s.c = j;
                } else
                    System.out.println("error");
                is.insert(new node(container[i][j], g, s));
            }
        rbfs tree = new rbfs(is, zero);
        branch j = tree.constructSolution();
        tree.solve(j);
    }
}