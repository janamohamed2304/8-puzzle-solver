import java.util.List;

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

        String initialState = "876543210";

        Node start = new Node(initialState, null, null, 0, 0);


        BFS solver = new BFS();

        long startTime = System.currentTimeMillis();
        List<Node> path = solver.bfs(start);
        long endTime = System.currentTimeMillis();

        if (path == null) {
            System.out.println("No solution found!");
            return;
        }

        System.out.println("\n==== BFS Solution Path ====\n");

        for (int i = 0; i < path.size(); i++) {
            Node n = path.get(i);
            printBoard(n.state, i, n.action);
        }

        System.out.println("Path cost: " + path.size());
        System.out.println("Number of Explored Nodes : " + solver.visited.size());
        System.out.println("Search Depth: " + path.size());
        System.out.println("Time: " + (endTime-startTime) + " ms");


    }
}