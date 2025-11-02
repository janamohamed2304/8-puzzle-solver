import java.util.List;
import java.util.Stack;

public class Main {

    static final String RESET = "\u001B[0m";
    static final String CYAN = "\u001B[36m";
    static final String WHITE = "\u001B[37m";
    static final String GREEN = "\u001B[32m";
    static final String BLACK = "\u001B[30m";

    public static void printBoard(String state, int step, String move) {

        System.out.println(GREEN + "Step " + step +
                (move != null ? " (Move: " + move + ")" : "") + RESET);

        System.out.println(WHITE + "+---+---+---+" + RESET);

        for (int i = 0; i < 9; i++) {

            char c = state.charAt(i);
            if (c == '0') {
                System.out.print("|" + BLACK + "   " + RESET);
            } else {
                System.out.print("| " + CYAN + c + RESET + " ");
            }

            if (i % 3 == 2) {
                System.out.println("|");
                System.out.println(WHITE + "+---+---+---+" + RESET);
            }
        }

        System.out.println();
    }

    public static void main(String[] args) {

        String initialState = "123405678";
        Node start = new Node(initialState, null, null, 0, 0);

        System.out.println("\n========= Optimal Solutions =========\n");

        System.out.println("\n========= BFS Solution Path =========\n");

        BFS solver = new BFS();

        long startTime = System.currentTimeMillis();
        List<Node> path = solver.bfs(start);
        long endTime = System.currentTimeMillis();

        if (path == null) {
            System.out.println("No solution found!");
            return;
        }

        for (int i = 0; i < path.size(); i++) {
            Node n = path.get(i);
            printBoard(n.state, i, n.action);
        }

        System.out.println("Path cost: " + path.size());
        System.out.println("Number of Explored Nodes : " + solver.visited.size());
        System.out.println("Search Depth: " + path.size());
        System.out.println("Time: " + (endTime-startTime) + " ms");

        System.out.println("\n========= AStar Solution Path =========\n");

        AStar solver2 = new AStar();
        List<String> solution = solver2.aStar(initialState, "012345678");
        if (solution != null) {
            System.out.println("Solution found in " + solution.size() + " moves:");
            for (String move : solution) {
                System.out.print(move + " ");
            }

            System.out.println("\n\nPath visualization:");
            String currentState = initialState;
            printBoard(currentState,0,null);

            int m = 0;
            for (String move : solution) {
                m++;
                for (Pair succ : Successors.getSuccessors(currentState)) {
                    if (succ.getDirection().equals(move)) {
                        currentState = succ.getState();
                        break;
                    }
                }
                printBoard(currentState, m, move);
            }

        } else {
            System.out.println("No solution found (unsolvable state).");
        }

        System.out.println("\n========== IDDFS Solution Path ==========\n");

        IDFS solver3 = new IDFS();
        List<Pair> res = solver3.iddfs(initialState);
        int m2 = 0;
        for(Pair item : res){
            m2++;
            printBoard(item.getState(), m2, item.getDirection());
        }

        System.out.println("\n========= Not Optimal Solutions =========\n");

        System.out.println("\n========= DFS Solution Path =========\n");

        DFS solver4 = new DFS();
        solver4.DFS(initialState);


    }
}