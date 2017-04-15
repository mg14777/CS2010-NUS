// Copy paste this Java Template and save it as "HospitalRenovation.java"
import java.util.*;
import java.io.*;

// write your matric number here:
// write your name here:
// write list of collaborators here:
// year 2017 hash code: AlaYnzmQ65P4x2Uc559u (do NOT delete this line)

class HospitalRenovation {
  private int V; // number of vertices in the graph (number of rooms in the hospital)
  private int[][] AdjMatrix; // the graph (the hospital)
  private int[] RatingScore; // the weight of each vertex (rating score of each room)
  private ArrayList<ArrayList<Integer>> adjList;
  private int[] visitedVertices;
  private static final int SENTINEL = 1000000;
  // if needed, declare a private data structure here that
  // is accessible to all methods in this class

  public HospitalRenovation() {
    // Write necessary code during construction
    //
    // write your answer here
  }
  /**
  To check for critical vertices perform DFS after removing that vertex from the graph 
  (simply mark that vertex as -1 in visitedVertices array) and check if the entire graph is still connected (1 connected component). 
  If yes then the vertex is NOT a critical vertex
  **/

  int DFS() {
    int connectedComponents = 0;
    for(int i=0; i < V; i++) {
      if(visitedVertices[i] == 0) {
        connectedComponents++;
        dfsVisit(i);
      }
    }
    return connectedComponents;
  }
  void dfsVisit(int vertex) {
    visitedVertices[vertex] = 1;
    for(int neighbour : adjList.get(vertex))
      if(visitedVertices[neighbour] == 0)
        dfsVisit(neighbour);
  } 
  ArrayList<Integer> findCriticalVertices() {
    ArrayList<Integer> criticalVertices = new ArrayList<>();
    for(int i=0; i < V; i++) {
      for(int x=0; x < V; x++)
        visitedVertices[x] = 0;
      visitedVertices[i] = -1; // mark the vertex that needs to be excluded
      int connectedComponents = DFS();
      if(connectedComponents > 1)
        criticalVertices.add(i);
    }
    return criticalVertices;
  }
  int Query() {
    int ans = 0;

    // You have to report the rating score of the critical room (vertex)
    // with the lowest rating score in this hospital
    //
    // or report -1 if that hospital has no critical room
    ArrayList<Integer> criticalVertices = new ArrayList<>();
    criticalVertices = findCriticalVertices();
    if(criticalVertices.isEmpty())
      ans = -1;
    else {
      int min = SENTINEL;
      for(int vertex : criticalVertices)
        if(min > RatingScore[vertex])
          min = RatingScore[vertex];
      ans = min;
    }
    return ans;
  }

  // You can add extra function if needed
  // --------------------------------------------



  // --------------------------------------------

  void run() throws Exception {
    // for this PS3, you can alter this method as you see fit

    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    PrintWriter pr = new PrintWriter(new BufferedWriter(new OutputStreamWriter(System.out)));
    int TC = Integer.parseInt(br.readLine()); // there will be several test cases
    while (TC-- > 0) {
      br.readLine(); // ignore dummy blank line
      V = Integer.parseInt(br.readLine());
      visitedVertices= new int[V];
      StringTokenizer st = new StringTokenizer(br.readLine());
      // read rating scores, A (index 0), B (index 1), C (index 2), ..., until the V-th index
      RatingScore = new int[V];
      for (int i = 0; i < V; i++)
        RatingScore[i] = Integer.parseInt(st.nextToken());

      // clear the graph and read in a new graph as Adjacency Matrix
      AdjMatrix = new int[V][V];
      adjList = new ArrayList<>();
      for(int i=0; i < V; i++)
        adjList.add(new ArrayList<Integer>());
      for (int i = 0; i < V; i++) {
        st = new StringTokenizer(br.readLine());
        int k = Integer.parseInt(st.nextToken());
        while (k-- > 0) {
          int j = Integer.parseInt(st.nextToken());
          AdjMatrix[i][j] = 1; // edge weight is always 1 (the weight is on vertices now)
          adjList.get(i).add(j);
        }
      }

      pr.println(Query());
    }
    pr.close();
  }

  public static void main(String[] args) throws Exception {
    // do not alter this method
    HospitalRenovation ps3 = new HospitalRenovation();
    ps3.run();
  }
}



class IntegerPair implements Comparable < IntegerPair > {
  Integer _first, _second;

  public IntegerPair(Integer f, Integer s) {
    _first = f;
    _second = s;
  }

  public int compareTo(IntegerPair o) {
    if (!this.first().equals(o.first()))
      return this.first() - o.first();
    else
      return this.second() - o.second();
  }

  Integer first() { return _first; }
  Integer second() { return _second; }
}