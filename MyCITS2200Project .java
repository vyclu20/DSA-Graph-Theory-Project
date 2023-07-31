import java.util.*;

public class MyCITS2200Project implements CITS2200Project {
    //use a HashMap to store the graph, where the key is a page URL and the value
    //is a Set of adjacent page URLs.
    private Map<String, Set<String>> graph;
    private Map<String, Set<String>> reversedGraph;

    public MyCITS2200Project() {
        graph = new HashMap<>();
        reversedGraph = new HashMap<>();
    }

    /**
     * A method that adds links to the Wikipedia graph
     * @param urlFrom the starting url of the link to be added
     * @param urlTo the ending url of the link to be added
     */
    public void addEdge(String urlFrom, String urlTo) {
        //if the URL is not already in the graph, add it with an empty adjacency set.
        graph.putIfAbsent(urlFrom, new HashSet<>());
        graph.putIfAbsent(urlTo, new HashSet<>());
        //add the directed edge from urlFrom to urlTo.
        graph.get(urlFrom).add(urlTo);

        //add the reverse edge for the reversed graph.
        reversedGraph.putIfAbsent(urlTo, new HashSet<>());
        reversedGraph.putIfAbsent(urlFrom, new HashSet<>());
        reversedGraph.get(urlTo).add(urlFrom);
    }

    /**
     * A method that finds the shortest path given two Wikipedia links, using BFS
     * This is a response to question 1
     * @param urlFrom Starting url 
     * @param urlTo Endpoint url
     * @return Minimum number of links to reach the end url from the start url, -1 if no path found
     */
    public int getShortestPath(String urlFrom, String urlTo) {
        if (!graph.containsKey(urlFrom) || !graph.containsKey(urlTo)) {
            // If either URL is not in the graph, return -1.
            return -1;
        }
    
        Queue<String> queue = new LinkedList<>();
        Map<String, Integer> distances = new HashMap<>();
        Set<String> visited = new HashSet<>();
    
        // Initialize the queue and distances map.
        queue.add(urlFrom);
        distances.put(urlFrom, 0);
        visited.add(urlFrom);
    
        while (!queue.isEmpty()) {
            String current = queue.poll();
    
            //if current page is the destination, return its distance.
            if (current.equals(urlTo)) {
                return distances.get(current);
            }
    
            //visit all adjacent pages.
            for (String adjacent : graph.get(current)) {
                if (!visited.contains(adjacent)) {
                //enqueue the adjacent page and mark it as visited.
                queue.add(adjacent);
                visited.add(adjacent);

                //set the distance of the adjacent page.
                distances.put(adjacent, distances.get(current) + 1);
                }
            }
        }

        //if no path found, return -1.
        return -1;
    }

    /**
     * A method that finds the center of the page of a Wikipedia graph, using BFS
     * This is a response to question 4
     * @return A string array of the centers of the Wikipedia graph
     */
    public String[] getCenters() {

        if (graph.isEmpty()) {
            // return an empty array if the graph is empty to show there are no centers in an empty graph
            return new String[0];
        }

        // set an infinite variable and a map to store the eccentricities
        int INF = Integer.MAX_VALUE;
        Map<String, Integer> eccentricityMap = new HashMap<>();

        for (String url : graph.keySet()) {
            Map<String, Integer> distances = new HashMap<>();
            // set the distances for each vertex as infinite
            for (String v : graph.keySet())
                distances.put(v, INF);
            distances.put(url, 0);

            //use a queue to implement the BFS.
            Queue<String> queue = new LinkedList<>();
            queue.add(url);

            // BFS
            while (!queue.isEmpty()) {
                String current = queue.poll();
                for (String neighbour : graph.get(current)) {
                    if (distances.get(neighbour) == INF) {
                        queue.add(neighbour);
                        distances.put(neighbour, distances.get(current) + 1);
                    }
                }
            }

            // Look for the max distance for each url in the graph 
            int maxDistance = 0;
            boolean connected = true;
            for (String v : graph.keySet()) {
                if (distances.get(v) == INF) {
                    connected = false;
                    break;
                }
                if (distances.get(v) > maxDistance) {
                    maxDistance = distances.get(v);
                }
            }
            if (connected) {
                eccentricityMap.put(url, maxDistance);
            }
        }

        // Look for the vertex with the smallest eccentricity
        int minEccentricity = INF;
        for (int ecc : eccentricityMap.values())
            if (ecc < minEccentricity)
                minEccentricity = ecc;


        // Make a list of the centers of the graph, given that the value of 
        // that vertex's eccentricity is the same as minEccentricity
        List<String> centers = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : eccentricityMap.entrySet())
            if (entry.getValue() == minEccentricity)
                centers.add(entry.getKey());

        return centers.toArray(new String[0]);
    }

    /**
     * A method that finds the strongly connected components of a Wikipedia graph, using the Kosaraju-Shamir algorithm
     * This is a response to question 3.
     * @return An array of arrays of strings that contain the different strongly connected components in the graph
     */
    public String[][] getStronglyConnectedComponents() {

        // Return empty array of arrays of strings if the graph is empty to show there are no SCC in an empty graph
        if (graph.isEmpty()) {
            return new String[0][0];
        }

        //step 1: run DFS on the original graph to get the vertices in post-order.
        Stack<String> postOrderStack = new Stack<>();
        Set<String> visited = new HashSet<>();
        for (String url : graph.keySet()) {
            if (!visited.contains(url)) {
                dfs(url, visited, postOrderStack);
            }
        }

        //step 2: run DFS on the reversed graph using vertices in reverse post-order.
        visited.clear();
        List<List<String>> sccList = new ArrayList<>();
        while (!postOrderStack.isEmpty()) {
            String url = postOrderStack.pop();
            if (!visited.contains(url)) {
                List<String> scc = new ArrayList<>();
                dfs(url, visited, scc, reversedGraph);
                sccList.add(scc);
            }
        }

        //step 3: convert the list of SCCs to the required format.
        String[][] sccArray = new String[sccList.size()][];
        for (int i = 0; i < sccList.size(); i++) {
            sccArray[i] = sccList.get(i).toArray(new String[0]);
        }

        return sccArray;
    }

    //DFS helper method for step 1.
    private void dfs(String url, Set<String> visited, Stack<String> stack) {
        visited.add(url);
        for (String adjUrl : graph.get(url)) {
            if (!visited.contains(adjUrl)) {
                dfs(adjUrl, visited, stack);
            }
        }
        stack.push(url);
    }

    //DFS helper method for step 2.
    private void dfs(String url, Set<String> visited, List<String> list, Map<String, Set<String>> graph) {
        visited.add(url);
        list.add(url);
        for (String adjUrl : graph.get(url)) {
            if (!visited.contains(adjUrl)) {
                dfs(adjUrl, visited, list, graph);
            }
        }
    }

    /**
     * A method that finds the hamiltonian path in the Wikipedia graph, ie finds if there is a path that 
     * visits all the nodes in the graph ONCE, using the Held-Karp algorithm
     * This is a response to question 2
     * @return An array of the order of the visited urls(vertices) of the path, -1 if hamiltonian path is not found
     */
    public String[] getHamiltonianPath() {

        // Return empty output if graph is empty
        if (graph.isEmpty()) {
            return new String[0];
        }

        // Find size of graph
        int n = graph.size();
	    // if the size of the graph is 0 return empty string
        if (n == 0)
            return new String[0];

        //initialization 
        // array of the urls
        String[] vertices = graph.keySet().toArray(new String[0]);
        // 2d array to store subsets for the dynamic programming
        int[][] dp = new int[n][1 << n];

	    // for all the rows in dp fill up w max value
        for (int[] row : dp)
            Arrays.fill(row, Integer.MAX_VALUE / 2);
        // for every subset initialize to 0
        for (int i = 0; i < n; i++)
            dp[i][1 << i] = 0;

        //dynamic Programming stuff
        // for each subset
        for (int mask = 1; mask < (1 << n); mask++) {
            // for each node
            for (int v = 0; v < n; v++) {
                // if the vertex is in the subset
                if ((mask & (1 << v)) != 0) {
                    // for all vertices (as ending nodes)
                    for (int u = 0; u < n; u++) {
                        // if the current vertex is also in the mask and the current vertex is adjacent to the node
                        if ((mask & (1 << u)) != 0 && graph.get(vertices[u]).contains(vertices[v])) {
                            // update min distance of current vertex and node depending whichever is smaller
                            dp[v][mask] = Math.min(dp[v][mask], dp[u][mask ^ (1 << v)] + 1);
                        }
                    }
                }
            }
        }

        //reconstruct path
        int[] parent = new int[n];
        Arrays.fill(parent, -1);
        int minDist = Integer.MAX_VALUE / 2, end = -1;
        // last subset
        int endMask = (1 << n) - 1;
        // for all vertices
        for (int i = 0; i < n; i++) {
            // the minDist is greater than the distance of the end subset, update
            if (minDist > dp[i][endMask]) {
                minDist = dp[i][endMask];
                end = i;
            }
        }
        if (end == -1)
            return new String[0]; //no ham path

        String[] path = new String[n];
        int curMask = endMask;
        // traversing the subsets in reverse
        for (int i = n - 1; i >= 0; i--) {
            // building hamiltonian path from the end 
            path[i] = vertices[end];
            int prevMask = curMask ^ (1 << end);
            for (int j = 0; j < n; j++) {
                // go through all the vertices and check if in the prev node AND 
                // has the same distance to prev node as the last node to current node AND
                // has the last node
                if ((prevMask & (1 << j)) != 0 && dp[end][curMask] == dp[j][prevMask] + 1 &&
                        graph.get(vertices[j]).contains(vertices[end])) {
                    parent[end] = j;
                    break;
                }
            }
            curMask = prevMask;
            end = parent[end];
        }
        return path;
    }
}