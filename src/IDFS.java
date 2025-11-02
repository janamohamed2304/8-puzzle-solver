import java.util.*;

public class IDFS {


    private int nodesExpanded = 0;
    private int maxDepth = 0;
    private long runTime = 0;

    // Returns path as list of strings (states)
    public List<Pair> iddfs(String start) {

        long startTime = System.currentTimeMillis();

        for(int d=1 ; d<=31 ; d++){

            Set<String> branch = new HashSet<>();
            branch.add(start);

            List<Pair> path = new ArrayList<>();
            path.add(new Pair(null, start));

            List<Pair> result = dls(start, d, branch, path);
            if (result != null) {
                long endTime = System.currentTimeMillis();
                runTime = endTime - startTime;
                System.out.println("Goal found at depth: " + d);
                System.out.println("Nodes Expanded: " + nodesExpanded);
                System.out.println("Max depth reached: " + maxDepth);
                System.out.println("Running Time (ms): " + runTime);
                return result;
            }
        }
        System.out.println("Can't found :')");
        return null;
    }

    // Depth-Limited Search (recursive)
    private List<Pair> dls(String node, int depth, Set<String> branch, List<Pair> path) {

        nodesExpanded++;
        maxDepth = Math.max(maxDepth, path.size() - 1);

        if (node.equals("012345678")) return path;
        if (depth == 0) return null;

        for (Pair next : Successors.getSuccessors(node)) {
            if (!branch.contains(next.getState())) {

                branch.add(next.getState());
                path.add(next);

                List<Pair> result = dls(next.getState(), depth - 1, branch, path);
                if (result != null) return result;

                path.removeLast();
                branch.remove(next.getState());
            }
        }
        return null;
    }

}
