# DSA-Graph-Theory-Algorithm-Project

Project was last updated on: 22 May 2023

**This was a university project for a data structures and algorithm unit/module.**

Achieved 100% on this project. A report was also written detailing the algorithms used, sources used, complexity analysis of the algorithms, and performance studies on different-sized graphs to demonstrate our understanding of the algorithms and our ability to design and think of efficient solutions for graph-related problems.

## Project Overview

The main goal of the project was to create a Java class that implements a given interface, to provide methods to store and analyze various features of a Wikipedia page graph. 

> Wikipedia contains numerous pages on varied topics, where these pages may link to other pages inside Wikipedia. If we treat pages as vertices, and links as directed edges, then Wikipedia can be viewed as a graph.

### The interface defines four main questions to be addressed:

**Question 1: Finding the Shortest Path**
The first question requires implementing a method that can find the minimum number of links (edges) one must follow to get from a given Wikipedia page to another, which is essentially finding the shortest path between two vertices in the graph. Since this method needed a graph traversal algorithm to calculate the shortest path, we used BFS.

**Question 2: Finding Hamiltonian Path**
The second question involves finding a Hamiltonian path in the Wikipedia page graph. The method for this question must explore all possible paths in the graph to find a valid Hamiltonian path where we made use of the Held-Karp algorithm.

**Question 3: Finding Strongly Connected Components**
The third question requires writing a method that finds every strongly connected component of pages in the Wikipedia graph(a strongly connected component is a set of pages where there is a path between every ordered pair of vertices in that component). We used Kosaraju's algorithm to find the strongly connected components in the directed graph.

**Question 4: Finding Centers of the Graph**
The fourth question involves finding all the centers of the Wikipedia page graph. A center of a graph is a vertex (page) where the maximum shortest path from that vertex to any other vertex is the minimum possible. To solve this question, we made use of the BFS algorithm from question 1, then determined the centers based on the maximum shortest path values.
