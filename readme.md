# 🧩 Maze Solver - Interactive Pathfinding Visualization

A comprehensive Java application that implements and visualizes multiple pathfinding algorithms on custom mazes. This project features a user-friendly GUI that allows you to load maze files and watch different algorithms solve them in real-time.

![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=java&logoColor=white)
![Swing](https://img.shields.io/badge/Swing-GUI-blue?style=for-the-badge)
![License](https://img.shields.io/badge/License-MIT-green?style=for-the-badge)

## ✨ Features

### 🎯 Multiple Pathfinding Algorithms
- **Depth-First Search (DFS)** - Explores as far as possible along each branch
- **Breadth-First Search (BFS)** - Explores all neighbors before moving to the next level
- **A * Search** - Uses heuristics to find the optimal path efficiently
- **Dijkstra's Algorithm** - Finds the shortest path with weighted edges
- **Greedy Best-First Search** - Uses heuristics but doesn't guarantee optimal path
- **Dead End Fill** - Eliminates dead ends before pathfinding

### 🎮 Interactive GUI
- Real-time visualization of algorithm execution
- Step counter to track algorithm performance
- Color-coded maze elements for easy understanding
- File browser for loading custom maze files
- Algorithm comparison capabilities

### 🏗️ Special Maze Elements
- **Start (A)** and **End (B)** points
- **Walls (#)** that block movement
- **Teleportation tiles (T)** for instant transport
- **Counter modifiers (C/c)** that increase/decrease step count
- **Empty spaces ( )** for free movement

## 🚀 Getting Started

### Prerequisites
- Java Development Kit (JDK) 8 or higher
- Any Java IDE (IntelliJ IDEA, Eclipse, VS Code, etc.) or command line

### Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/BasselShaheen06/Check-Maze.git
   cd Check-Maze
   ```

2. **Compile the Java files**
   ```bash
   javac *.java
   ```

3. **Run the application**
   ```bash
   java MazeUI
   ```

## 📝 Creating Maze Files

Create a text file with the following characters:

| Character | Meaning |
|-----------|---------|
| `#` | Wall (impassable) |
| ` ` | Empty space (walkable) |
| `A` | Start point (exactly one required) |
| `B` | End point (exactly one required) |
| `T` | Teleportation tile |
| `C` | Counter increase (+50) |
| `c` | Counter decrease (-50) |

### Example Maze File
```
#########
#A  #   #
# # # # #
#   T   #
####### #
#     # #
# ### # #
#   #  B#
#########
```

### Maze Requirements
- All rows must have the same length
- Exactly one start tile (A) and one end tile (B)
- Rectangular shape (no jagged edges)
- File should be saved as a `.txt` file

## 🎨 Visual Guide

The application uses color coding to help visualize the pathfinding process:

- 🟢 **Green**: Start point
- 🔴 **Red**: End point  
- ⚫ **Black**: Walls
- ⚪ **White**: Empty spaces
- 🔵 **Blue**: Teleportation tiles
- 🟤 **Dark Red**: Counter increase tiles
- 🟫 **Dark Green**: Counter decrease tiles
- 🟡 **Yellow**: Explored tiles
- 🟢 **Bright Green**: Final path
- 🩷 **Pink**: Dead end tiles (Dead End Fill algorithm)

## 🔧 Usage

1. **Launch the application** by running `java MazeUI`
2. **Load a maze** by clicking "Load Maze" and selecting your `.txt` file
3. **Choose an algorithm** from the dropdown menu
4. **Click "Start Algorithm"** to begin visualization
5. **Watch** as the algorithm explores the maze in real-time
6. **View results** including steps taken and path length

## 🏗️ Project Structure

```
Mazes/
├──MAZE2 SPECIAL TILES.txt
├──Maze1.txt
├──Maze2.txt
├──Maze3.txt
├──maze.txt
├──maze2.txt
├──maze5.txt
├──maze6.txt
├──maze8.txt
└──maze_.txt
src/
├── MazeLoader.java      # Handles maze file loading and validation
├── Tile.java            # Represents individual maze tiles and conversions
├── MazeSolver.java      # Implements all pathfinding algorithms
├── MazeUI.java          # Swing-based graphical user interface
└── Counter.java         # Wrapper class for step counting
.gitignore
LICENSE
README.md                # This file
```

## 🧠 Algorithm Details

### Depth-First Search (DFS)
- **Time Complexity**: O(V + E)
- **Space Complexity**: O(V)
- **Characteristics**: May not find shortest path, good for maze-like problems

### Breadth-First Search (BFS)
- **Time Complexity**: O(V + E)
- **Space Complexity**: O(V)
- **Characteristics**: Guarantees shortest path in unweighted graphs

### A* Search
- **Time Complexity**: O(b^d) where b is branching factor, d is depth
- **Space Complexity**: O(b^d)
- **Characteristics**: Optimal if heuristic is admissible, very efficient

### Dijkstra's Algorithm
- **Time Complexity**: O((V + E) log V)
- **Space Complexity**: O(V)
- **Characteristics**: Finds shortest path in weighted graphs

### Greedy Best-First Search
- **Time Complexity**: O(b^m) where m is maximum depth
- **Space Complexity**: O(b^m)
- **Characteristics**: Fast but not guaranteed to find optimal path

### Dead End Fill
- **Time Complexity**: O(V²) for dead end detection + O(V + E) for BFS
- **Space Complexity**: O(V)
- **Characteristics**: Preprocesses maze to eliminate dead ends, then uses BFS

## 🎯 Performance Comparison

The application tracks and displays:
- **Steps taken**: Number of tiles explored
- **Path length**: Length of the final solution path

## 🤝 Contributing

Contributions are welcome! Here are some ways you can help:

1. **Fork** the repository
2. **Create** a feature branch (`git checkout -b feature/amazing-feature`)
3. **Commit** your changes (`git commit -m 'Add amazing feature'`)
4. **Push** to the branch (`git push origin feature/amazing-feature`)
5. **Open** a Pull Request

### Ideas for Contributions
- Add more pathfinding algorithms (Jump Point Search, Theta*, etc.)
- Implement maze generation algorithms
- Add maze editing capabilities
- Improve UI/UX design
- Add maze file format validation
- Create more example maze files
- Add unit tests

## 🐛 Known Issues

- Very large mazes may cause performance issues
- UI may become unresponsive during algorithm execution on complex mazes
- Teleportation tiles may create infinite loops in some edge cases

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 👨‍💻 Creators

[Bassel Shaheen](https://github.com/BasselShaheen06)

[Amatalrahman](https://github.com/Amatalrahman)

[Alaa Essam](https://github.com/Alaa-Essam5)

[Ganna Ahmed](https://github.com/gannaahmed200)

[Kareem Taha](https://github.com/Kareem-Taha-05)

## 🙏 Acknowledgments

- [Inspired by CS50's introduction to AI](https://youtube.com/playlist?list=PLhQjrBD2T382Nz7z1AEXmioc27axa19Kv&si=agXRByYPWsO2i3hI)

---

**Happy Pathfinding!** 🗺️✨
