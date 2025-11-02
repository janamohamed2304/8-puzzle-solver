import java.util.*;

public class DFS {

    static class Node {
        Pair state;
        Node parent;
        int depth;

        Node(Pair state, Node parent, int depth) {
            this.state = state;
            this.parent = parent;
            this.depth = depth;
        }
    }

    private int nodesExpanded = 0;
    private int maxDepth = 0;
    private long time = 0;


    public void DFS(String initialState) {

        long startTime = System.currentTimeMillis();

        Stack<Node> frontier = new Stack<>();
        Set<String> frontierSearch = new HashSet<>();
        Set<String> explored = new HashSet<>();


        frontier.push(new Node(new Pair("-", initialState), null, 0));

        while (!frontier.isEmpty()) {

            Node current = frontier.pop();
            explored.add(current.state.getState());
            nodesExpanded++;
            maxDepth = Math.max(maxDepth, current.depth);

            if (current.state.getState().equals("012345678")) {
                long endTime = System.currentTimeMillis();
                time = endTime - startTime;
                printResults(current);
                return;
            }

            for (Pair neighbor : Successors.getSuccessors(current.state.getState())) {
                if (!explored.contains(neighbor.getState()) && !frontierSearch.contains(neighbor.getState())) {
                    frontier.push(new Node(neighbor, current, current.depth + 1));
                    frontierSearch.add(neighbor.getState());

                }
            }
        }

    }


    public void printResults(Node finalnode){
        List<Pair> path = new ArrayList<>();
        Node current = finalnode;

        while (current != null) {
            path.add(current.state);
            current = current.parent;
        }
        Collections.reverse(path);

        if(path.size() < 100) {
            for (Pair i : path) {
                System.out.println(i.getDirection());
                System.out.println(i.getState());
            }
        }

        System.out.println("Goal found at depth: " + (path.size() - 1));
        System.out.println("Nodes expanded: " + nodesExpanded);
        System.out.println("Max depth reached: " + maxDepth);
        System.out.println("Running time (ms): " + time);
    }

}
