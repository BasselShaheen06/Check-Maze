import java.util.*;

public class MazeSolver {

    // initialize our grid, counter and 2 end points and our ui (to call updateUI)
    private Tile[][] maze;
    private Counter counter;
    private Tile start;
    private Tile end;
    private MazeUI ui; 

    public MazeSolver(Tile[][] maze) {
        this.maze = maze;
        this.counter = new Counter(0);
        locateStartAndEnd();
    }
    
    // method to set UI reference for updates
    public void setUI(MazeUI ui) {
        this.ui = ui;
    }

    // this method locates our start point and end point by iterating over each tile 
    private void locateStartAndEnd() {
        for (Tile[] row : maze) {
            for (Tile tile : row) {
                if (tile.isStart()) 
                    start = tile;
                
                if (tile.isEnd())
                    end = tile;
            }
        }
        if (start == null || end == null) {
            throw new IllegalStateException("Start or End tile not found.");
        }
    }
    
    // Depth-First search algorithm - True DFS Implementation
    public boolean DFS() {
        // Reset all tiles before starting
        resetMaze();
        
        // initially just mark the starting tile as visited
        start.setVisited(true);
        counter.value++;
        
        // update UI 
        if (ui != null) {
            ui.updateCounter(counter.value);
            ui.updateUI();
        }
        
        // then we call the recursive function 
        return dfs(start);
    }
    
    private boolean dfs(Tile current) {
    	
        // Check if we reached the end
        if (current.isEnd()) {
            System.out.println("Reached the end! Final counter: " + counter.value);
            if (ui != null) {
                ui.updateCounter(counter.value);
                ui.updateUI();
            }
            return true;
        }
        
        // Apply special effects
        Tile next = current.applySpecialEffect(counter, maze);
        
        // Handle teleportation. first we check that the current tile is not the previous tile before teleporting 
        if (next != current) {
        	// then we check if the current tile has been visited before (we already made sure to teleport to a non visited tile but this is just to make sure)
            if (!next.isVisited()) {
            	// if this is a new tile, set it to visited before, save parent for backtracking and update counter and UI
                next.setVisited(true);
                next.setParent(current);
                counter.value++;
                
                if (ui != null) {
                    ui.updateCounter(counter.value);
                    ui.updateUI();
                }
                
                // recursively explore from teleported location
                if (dfs(next)) {
                    return true;
                }
            }
            return false;
        }
        
        // Get all available neighbors 
        List<Tile> neighbors = current.getValidNeighbors(maze);
        
        // iterate over each neighbor and for each neighbor we will call the dfs function again to explore that entire neighbor and the full path before moving to the next neighbor 
        // for example if the first neighbor is left then we will take that left and explore all its neighbor and take the left's left and then the left's left's left etc until we finish the entire path for that left neighbor
        for (Tile neighbor : neighbors) {
        	// if they had not been visited before then 
            if (!neighbor.isVisited()) {
                neighbor.setVisited(true);
                neighbor.setParent(current);
                counter.value++;
                
                if (ui != null) {
                    ui.updateCounter(counter.value);
                    ui.updateUI();
                }
                
                // Recursively explore this neighbor completely before trying the next one
                if (dfs(neighbor)) {
                    return true;
                }
            }
        }
        
        return false; // if we reach this point then there is no path found 
    }

    
    // Breadth-First search algorithm
    public boolean BFS() {
    	// reset the maze before starting 
        resetMaze();
        
        // BFS implements a queue frontier to load the last element first to ensure we go through all neighboring tiles before going to a deeper level 
        Queue<Tile> queue = new LinkedList<>();
        // load the start tile to the Queue and mark it as visited
        queue.offer(start);
        start.setVisited(true);

        // loop over and over until the Queue is empty
        while (!queue.isEmpty()) {
        	
        	// first we pull the last element 
            Tile current = queue.poll();
            
            // update counter and UI
            counter.value++;
            if (ui != null) {
                ui.updateCounter(counter.value);
                ui.updateUI();
            }
            
            // Check if we reached the end
            if (current.isEnd()) {
                System.out.println("Reached the end! Final counter: " + counter.value);
                if (ui != null) {
                    ui.updateCounter(counter.value);
                    ui.updateUI();
                }
                return true;
            }
            
            // Apply special effects
            Tile next = current.applySpecialEffect(counter, maze);

            // teleportation logic, first we check that the tile we teleported to is not the same as what we teleported from 
            if (next != current) {
            	// then we check that the tile we teleported to was not visited before. 
            	// these checks are already implemented in the teleportation logic itself but we did it again to avoid any edge cases
                if (!next.isVisited()) {
                	//if everything is okay then we mark the current node as visited and load it to the Queue 
                    next.setVisited(true);
                    next.setParent(current);
                    queue.offer(next);
                }
                continue;
            }

            // add all unvisited neighbors to queue
            for (Tile neighbor : current.getValidNeighbors(maze)) {
                if (!neighbor.isVisited()) {
                    neighbor.setVisited(true);
                    neighbor.setParent(current);
                    queue.offer(neighbor);
                }
            }
        }

        System.out.println("No path found.");
        return false;
    }
    
    // A* search algorithm 
    public boolean AStar() {
        // Before we start, we reset the maze to clear visited flags, parent references, and counters
        resetMaze();

        // Creates a priority queue that sorts tiles by their total estimated cost (fScore),
        // ensuring that the most promising (cheapest) path is explored first
        PriorityQueue<AStarNode> openSet = new PriorityQueue<>((a, b) -> Double.compare(a.fScore, b.fScore));

        // The closedSet keeps track of tiles that have already been fully processed
        Set<Tile> closedSet = new HashSet<>();

        // gScore stores the cheapest known cost from the start tile to each tile
        Map<Tile, Double> gScore = new HashMap<>();

        // fScore stores the estimated total cost from start to goal through each tile
        // (fScore = gScore + heuristic estimate to the goal)
        Map<Tile, Double> fScore = new HashMap<>();

        // Initialize the starting tile with gScore = 0 and fScore = heuristic to goal
        AStarNode startNode = new AStarNode(start, 0, heuristic(start, end));

        // Add the start tile to the priority queue and initialize its scores
        openSet.offer(startNode);
        gScore.put(start, 0.0);
        fScore.put(start, heuristic(start, end));
        start.setVisited(true);

        // Main loop: continue exploring while there are still nodes in the open set
        while (!openSet.isEmpty()) {

            // Remove the tile with the lowest estimated cost from the queue
            AStarNode currentNode = openSet.poll();
            Tile current = currentNode.tile;

            // Skip this tile if we've already processed it
            if (closedSet.contains(current)) continue;

            // Mark the current tile as processed
            closedSet.add(current);

            // Update step counter and UI (if available)
            counter.value++;
            if (ui != null) {
                ui.updateCounter(counter.value);
                ui.updateUI();
            }

            // Check if we have reached the goal tile
            if (current.isEnd()) {
                System.out.println("Reached the end! Final counter: " + counter.value);
                if (ui != null) {
                    ui.updateCounter(counter.value);
                    ui.updateUI();
                }
                return true;
            }

            // Apply special effects like teleportation if the tile supports it
            Tile next = current.applySpecialEffect(counter, maze);

            // Handle teleportation: if we teleported to a new tile, process it
            if (next != current) {
                if (!closedSet.contains(next)) {

                    // Calculate the new cost to reach the teleported tile
                    double tentativeGScore = gScore.get(current) + 1;

                    // If this path is better than any previous path to the tile (or first time visiting it)
                    if (!gScore.containsKey(next) || tentativeGScore < gScore.get(next)) {
                        // Update the tile's parent to allow path reconstruction later
                        next.setParent(current);

                        // Store the new gScore and calculate fScore
                        gScore.put(next, tentativeGScore);
                        double fScoreValue = tentativeGScore + heuristic(next, end);
                        fScore.put(next, fScoreValue);

                        // Mark the tile as visited and add it to the open set
                        next.setVisited(true);
                        openSet.offer(new AStarNode(next, tentativeGScore, fScoreValue));
                    }
                }
                // After teleportation, we skip neighbor exploration of the original tile
                continue;
            }

            // Explore all valid neighbors of the current tile
            for (Tile neighbor : current.getValidNeighbors(maze)) {
                // Skip if the neighbor has already been processed
                if (closedSet.contains(neighbor)) continue;

                // Calculate the tentative gScore from start to this neighbor via the current tile
                double tentativeGScore = gScore.get(current) + 1;

                // If this path to the neighbor is better than any previous path
                if (!gScore.containsKey(neighbor) || tentativeGScore < gScore.get(neighbor)) {
                    // Update the parent to enable path reconstruction
                    neighbor.setParent(current);

                    // Store the gScore and fScore for the neighbor
                    gScore.put(neighbor, tentativeGScore);
                    double fScoreValue = tentativeGScore + heuristic(neighbor, end);
                    fScore.put(neighbor, fScoreValue);

                    // Mark the neighbor as visited and add it to the priority queue
                    neighbor.setVisited(true);
                    openSet.offer(new AStarNode(neighbor, tentativeGScore, fScoreValue));
                }
            }
        }

        // If we exit the loop, it means no path to the goal was found
        System.out.println("No path found.");
        return false;
    }

    
    // Helper class to store data used in A* comparison and sorting
    private static class AStarNode {
        Tile tile;        // The tile represented by this node
        double gScore;    // Cost from the start to this tile
        double fScore;    // Estimated total cost from start to goal through this tile

        AStarNode(Tile tile, double gScore, double fScore) {
            this.tile = tile;
            this.gScore = gScore;
            this.fScore = fScore;
        }
    }

    
    // Heuristic function for A* (Manhattan distance)
    private double heuristic(Tile a, Tile b) {
        return Math.abs(a.getRow() - b.getRow()) + Math.abs(a.getCol() - b.getCol());
    }
    
    
    // Greedy Best-First Search algorithm
 // Greedy Best-First Search algorithm
    public boolean greedyBestFirst() {
        // Reset the maze before starting the search (clears visited flags, parents, and counter)
        resetMaze();

        // Priority queue that always picks the tile with the lowest heuristic (hScore)
        // Greedy Best-First Search only considers how close the tile is to the goal (not total path cost)
        PriorityQueue<GreedyNode> openSet = new PriorityQueue<>((a, b) -> Double.compare(a.hScore, b.hScore));

        // Closed set to keep track of visited/processed tiles
        Set<Tile> closedSet = new HashSet<>();

        // Create the start node with its heuristic value (distance to goal)
        GreedyNode startNode = new GreedyNode(start, heuristic(start, end));

        // Add the start node to the open set and mark it as visited
        openSet.offer(startNode);
        start.setVisited(true);

        // Continue searching while there are nodes in the open set
        while (!openSet.isEmpty()) {

            // Remove the tile with the lowest heuristic value (closest to goal)
            GreedyNode currentNode = openSet.poll();
            Tile current = currentNode.tile;

            // Skip this tile if already processed
            if (closedSet.contains(current)) continue;

            // Mark the tile as processed
            closedSet.add(current);

            // Update step counter and UI (if exists)
            counter.value++;
            if (ui != null) {
                ui.updateCounter(counter.value);
                ui.updateUI();
            }

            // Check if we have reached the goal tile
            if (current.isEnd()) {
                System.out.println("Reached the end! Final counter: " + counter.value);
                if (ui != null) {
                    ui.updateCounter(counter.value);
                    ui.updateUI();
                }
                return true;
            }

            // Apply special tile effects (e.g., teleportation)
            Tile next = current.applySpecialEffect(counter, maze);

            // If teleportation happens (next != current), process the teleported tile
            if (next != current) {
                if (!closedSet.contains(next)) {
                    // Link the teleported tile to the current tile for path reconstruction
                    next.setParent(current);
                    next.setVisited(true);

                    // Add the teleported tile to the open set based on its heuristic
                    openSet.offer(new GreedyNode(next, heuristic(next, end)));
                }
                // Skip normal neighbor exploration when teleporting
                continue;
            }

            // Explore all valid neighbors of the current tile
            for (Tile neighbor : current.getValidNeighbors(maze)) {
                // Skip already processed tiles
                if (closedSet.contains(neighbor)) continue;

                // If the neighbor hasn’t been visited yet
                if (!neighbor.isVisited()) {
                    // Set parent for path reconstruction
                    neighbor.setParent(current);
                    neighbor.setVisited(true);

                    // Add to open set with heuristic value (h(n))
                    openSet.offer(new GreedyNode(neighbor, heuristic(neighbor, end)));
                }
            }
        }

        // If the open set is empty and goal wasn’t reached, no path was found
        System.out.println("No path found.");
        return false;
    }


 // Helper class to store tile and its heuristic score
    private static class GreedyNode {
        Tile tile;      // The current tile
        double hScore;  // Heuristic value: estimated distance to the goal (h(n))

        GreedyNode(Tile tile, double hScore) {
            this.tile = tile;
            this.hScore = hScore;
        }
    }


    
    // Helper method to reset maze state
    private void resetMaze() {
        for (Tile[] row : maze) {
            for (Tile tile : row) {
                tile.setVisited(false);
                tile.setParent(null);
            }
        }
        counter.value = 0;
    }
    
    
    // this method backtracks from end to finish using the parent tiles to return the shortest path we found
    public List<Tile> reconstructPath(Tile end) {
        List<Tile> path = new ArrayList<>();
        Tile current = end;
        while (current != null) {
            path.add(current);
            current = current.getParent();
        }
        Collections.reverse(path);
        return path;
    }
    
    // Getters
    public Counter getCounter() {
        return counter;
    }
    
    public Tile getStart() {
        return start;
    }
    
    public Tile getEnd() {
        return end;
    }
}