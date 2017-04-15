// Copy paste this Java Template and save it as "Bleeding.java"
import java.util.*;
import java.io.*;

// write your matric number here: A0153196B
// write your name here: Mudit Gupta
// write list of collaborators here:
// year 2017 hash code: x8DYWsALaAzykZ8dYPZP (do NOT delete this line)

class Bleeding {
  private int V; // number of vertices in the graph (number of junctions in Singapore map)
  private int Q; // number of queries
  private ArrayList < ArrayList < IntegerPair > > AdjList; // the weighted graph (the Singapore map), the length of each edge (road) is stored here too, as the weight of edge

  // if needed, declare a private data structure here that
  // is accessible to all methods in this class
  // --------------------------------------------
  private int[][] AdjMatrix;
  private int[][] queries;
  public static final int MAXWEIGHT = 2000;
  private int[] dfsVisited;
  // --------------------------------------------

  public Bleeding() {
    // Write necessary code during construction
    //
    // write your answer here



  }
  // Find all pairs shortest paths using Floyd Warshall algorithm
  /*
  void floydWarshall() {

    for(int k=0; k < V; k++)
      for(int i=0; i < V;i++)
        for(int j=0; j < V;j++) {
          if(distMatrix[i][j] > distMatrix[i][k] + distMatrix[k][j])
            distMatrix[i][j] = distMatrix[i][k] + distMatrix[k][j];
        }
  }
  void BFS(int s,int t) {
    LinkedList<Integer> queue = new LinkedList<Integer>();
    int[] visited = new int[V];
    pred = new int[V];
    queue.addLast(s);
    visited[s] = 1;
    pred[s] = -1;
    while(queue.size() != 0) {
      int u = queue.removeFirst();
      for(IntegerPair edge : AdjList.get(u)) {
        int neighbour = edge.first();
        int weight = edge.second();
        if(neighbour == t) {
          pred[neighbour] = u;
          return;
        }
        if(visited[neighbour] == 0) {
          visited[neighbour] = 1;
          queue.addLast(neighbour);
          pred[neighbour] = u;
        }
      }
    }
  }
  */
  void PreProcess() {
    
    computeAllQueries();

  }
  void maxWeightOptimized(int org_source,int cur_source,int cur_path_weight) {
    dfsVisited[cur_source] = 1;
    for(IntegerPair edge: AdjList.get(cur_source)) {
      int cur_neighbour = edge.first();
      if(dfsVisited[cur_neighbour] == 0) {
        queries[org_source][cur_neighbour] = edge.second() + cur_path_weight;
        maxWeightOptimized(org_source,cur_neighbour,queries[org_source][cur_neighbour]);
      }
    }
  }
  void computeAllQueries() {
    int sourceSize = V;
    queries = new int[V][V];
    for(int s=0; s < sourceSize; s++) {
        dfsVisited = new int[V];
        maxWeightOptimized(s,s,0);
    }
  }
  int Query(int s, int t, int k) {
    int ans = -1;

    // You have to report the shortest path from Ket Fah's current position s
    // to reach the chosen hospital t, output -1 if t is not reachable from s
    // with one catch: this path cannot use more than k vertices      

    // BFS technique
    /*
    if(s == t)
      ans = 0;
    else {
      BFS(s,t);
      int cur = t;
      int cost = 0;
      while(pred[cur] != -1) {
        
        cost += AdjMatrix[pred[cur]][cur];
        cur = pred[cur];
      }
      ans = cost;
    }
    */
    // Compute all queries via DFS technique 
    ans = queries[s][t];
    return ans;
  }

  // You can add extra function if needed
  // --------------------------------------------



  // --------------------------------------------

  void run() throws Exception {
    // you can alter this method if you need to do so
    IntegerScanner sc = new IntegerScanner(System.in);
    PrintWriter pr = new PrintWriter(new BufferedWriter(new OutputStreamWriter(System.out)));

    int TC = sc.nextInt(); // there will be several test cases
    while (TC-- > 0) {
      V = sc.nextInt();

      // clear the graph and read in a new graph as Adjacency List
      AdjList = new ArrayList < ArrayList < IntegerPair > >();
      // construct an adjacency matrix as well
      //AdjMatrix = new int[V][V];
      //distMatrix = new int[V][V];
      /*
      for(int i=0; i < V;i++)
        for(int j=0;j<V;j++) {
          if(i == j)
            AdjMatrix[i][j] = 0;
          else
            AdjMatrix[i][j] = MAXWEIGHT;
          distMatrix[i][j] = AdjMatrix[i][j];;
        }
        */
      for (int i = 0; i < V; i++) {
        AdjList.add(new ArrayList < IntegerPair >());

        int k = sc.nextInt();
        while (k-- > 0) {
          int j = sc.nextInt(), w = sc.nextInt();
          AdjList.get(i).add(new IntegerPair(j, w)); // edge (road) weight (in minutes) is stored here
          //AdjMatrix[i][j] = w;
          //distMatrix[i][j] = w;
        }
      }

      PreProcess(); // optional

      Q = sc.nextInt();
      while (Q-- > 0)
        pr.println(Query(sc.nextInt(), sc.nextInt(), sc.nextInt()));

      if (TC > 0)
        pr.println();
    }

    pr.close();
  }

  public static void main(String[] args) throws Exception {
    // do not alter this method
    Bleeding ps5 = new Bleeding();
    ps5.run();
  }
}



class IntegerScanner { // coded by Ian Leow, using any other I/O method is not recommended
  BufferedInputStream bis;
  IntegerScanner(InputStream is) {
    bis = new BufferedInputStream(is, 1000000);
  }
  
  public int nextInt() {
    int result = 0;
    try {
      int cur = bis.read();
      if (cur == -1)
        return -1;

      while ((cur < 48 || cur > 57) && cur != 45) {
        cur = bis.read();
      }

      boolean negate = false;
      if (cur == 45) {
        negate = true;
        cur = bis.read();
      }

      while (cur >= 48 && cur <= 57) {
        result = result * 10 + (cur - 48);
        cur = bis.read();
      }

      if (negate) {
        return -result;
      }
      return result;
    }
    catch (IOException ioe) {
      return -1;
    }
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