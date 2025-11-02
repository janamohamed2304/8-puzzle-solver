import java.util.*;

public class AStar {

    private int manhatten(String state, String goal) {
        int distance = 0;
        int size = 3; // since it's a 3x3 puzzle
    
        for (int i = 0; i < state.length(); i++) {
            char c = state.charAt(i);
            if (c != '0') {
                int currentRow = i / size;
                int currentCol = i % size;
    
                int goalIndex = goal.indexOf(c);
                int goalRow = goalIndex / size;
                int goalCol = goalIndex % size;
    
                distance += Math.abs(currentRow - goalRow) + Math.abs(currentCol - goalCol);
            }
        }
        return distance;
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

        Node startNode = new Node(start, null, null, 0, manhatten(start, goal));
        frontier.add(startNode);
        visitedGScore.put(start, 0);

        while (!frontier.isEmpty()) {
            Node current = frontier.poll();

            if (closed.contains(current.state))
                continue;
            closed.add(current.state);

//            System.out.println("Exploring state with f=" + current.f() +
//                    ", g=" + current.g + ", h=" + current.h + ": " + current.state);

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
                int h = manhatten(nextState, goal);
                Node nextNode = new Node(nextState, current, succ.getDirection(), succG, h);
                frontier.add(nextNode);

            }
        }
        return null;
    }

}
