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
                int h = misplacedTiles(nextState, goal);
                Node nextNode = new Node(nextState, current, succ.getDirection(), succG, h);
                frontier.add(nextNode);

            }
        }
        return null;
    }

}
