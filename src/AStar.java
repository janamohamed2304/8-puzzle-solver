import java.util.*;

public class AStar {

    private int misplacedTiles(String state, String goal) {
        int count = 0;
        for (int i = 0; i < state.length(); i++) {
            if (state.charAt(i) != '0' && state.charAt(i) != goal.charAt(i)) {
                count++;
            }
        }
        return count;
    }

    private List<String> reconstructPath(Node node) {
        List<String> optimalPath = new ArrayList<>();
        while (node != null) {
            if (node.action != null) {
                optimalPath.add(node.action);
            }
            node = node.parent;
        }
        Collections.reverse(optimalPath);
        return optimalPath;
    }

    public List<String> aStar(String start, String goal) {

        PriorityQueue<Node> frontier = new PriorityQueue<>(Comparator.comparingInt(Node::f));
        Map<String, Integer> visitedGScore = new HashMap<>();
        Set<String> closed = new HashSet<>();

        Node startNode = new Node(start, null, null, 0, misplacedTiles(start, goal));
        frontier.add(startNode);
        visitedGScore.put(start, 0);

        while (!frontier.isEmpty()) {
            Node current = frontier.poll();

            if (closed.contains(current.state))
                continue;
            closed.add(current.state);

            System.out.println("Exploring state with f=" + current.f() +
                    ", g=" + current.g + ", h=" + current.h + ": " + current.state);

            if (current.state.equals(goal)) {
                return reconstructPath(current);
            }

            for (Pair succ : Successors.getSuccessors(current.state)) {
                String nextState = succ.getState();
                int succG = current.g + 1;

                if (succG >= visitedGScore.getOrDefault(nextState, Integer.MAX_VALUE)) {
                    continue;
                }

                visitedGScore.put(nextState, succG);
                int h = misplacedTiles(nextState, goal);
                Node nextNode = new Node(nextState, current, succ.getDirection(), succG, h);
                frontier.add(nextNode);
                
            }
        }
        return null;
    }

    private void printState(String state) {
        for (int i = 0; i < state.length(); i++) {
            if (i % 3 == 0) System.out.println();
            System.out.print(state.charAt(i) + " ");
        }
        System.out.println("\n------");
    }

    public static void main(String[] args) {
        String start = "724506831";
        String goal = "123456780";

        AStar solver = new AStar();
        List<String> solution = solver.aStar(start, goal);

        if (solution != null) {
            System.out.println("Solution found in " + solution.size() + " moves:");
            for (String move : solution) {
                System.out.print(move + " ");
            }

            System.out.println("\n\nPath visualization:");
            String currentState = start;
            solver.printState(currentState);

            for (String move : solution) {
                for (Pair succ : Successors.getSuccessors(currentState)) {
                    if (succ.getDirection().equals(move)) {
                        currentState = succ.getState();
                        break;
                    }
                }
                System.out.println(move + ":");
                solver.printState(currentState);
            }

        } else {
            System.out.println("No solution found (unsolvable state).");
        }
    }
}
