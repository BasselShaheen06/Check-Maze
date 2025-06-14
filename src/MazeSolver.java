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