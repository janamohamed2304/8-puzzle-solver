import java.util.*;

public class BFS {
    Set<String> visited = new HashSet<>();
    int searchdepth=0;

    public List<Node> bfs(Node start) {
        Queue<Node> queue = new LinkedList<>();
        queue.add(start);
        visited.add(start.state);
        while (!queue.isEmpty()) {
            Node curr = queue.poll();
            //check goal
            if (curr.state.equals("012345678")) {
                return buildPath(curr);
            }
            List<Pair> neighbors = Successors.getSuccessors(curr.state);
            //add its children in frounter
            for (Pair p : neighbors) {
                searchdepth++;
                if (!visited.contains(p.state)) {

                    visited.add(p.state);

                    Node child = new Node(p.state, curr, p.direction, 0, 0);

                    queue.add(child);
                }
            }
        }

        return null;
    }
    //Backtracking
    public static List<Node> buildPath(Node goal) {
        List<Node> path = new ArrayList<>();
        while (goal != null) {
            path.add(goal);
            goal = goal.parent;
        }
        Collections.reverse(path);
        return path;
    }



}
