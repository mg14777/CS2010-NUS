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
  private int[][] queries;
  public static final int MAXWEIGHT = 1000000000;
  private int[] distance;
  private PriorityQueue<IntegerPair> ssspPQ;
  // --------------------------------------------

  public Bleeding() {
    // Write necessary code during construction
    //
    // write your answer here



  }
  void floydWarshall() {
    int sourceSize = Math.min(10,V);
    for(int k=0; k < V; k++)
      for(int i=0; i < sourceSize;i++)
        for(int j=0; j < V;j++) {
          if(queries[i][j] > queries[i][k] + queries[k][j])
            queries[i][j] = queries[i][k] + queries[k][j];
        }
  }
  void initSSSP(int s) {
    distance = new int[V];
    ssspPQ = new PriorityQueue<>();
    for(int i=0; i < V;i++) {
      distance[i] = MAXWEIGHT;
    }
    distance[s] = 0;
    ssspPQ.add(new IntegerPair(s,0));
  }
  boolean relax(int parent,int neighbour,int weight) {
    if(distance[neighbour] > distance[parent] + weight) {
      distance[neighbour] = distance[parent] + weight;
      return true;
    }
    return false;
  }
  void dijkstra(int s) {
    initSSSP(s);
    while(ssspPQ.size() != 0) {
      IntegerPair pair = ssspPQ.poll();
      int parent = pair.first();
      int dist = pair.second();
      if(dist == distance[parent]) {
        for(IntegerPair edge : AdjList.get(parent)) {
          int neighbour = edge.first();
          int weight = edge.second();
          boolean relaxed = relax(parent,neighbour,weight);
          if(relaxed)
            ssspPQ.add(new IntegerPair(neighbour,distance[neighbour]));
        }
      }
    }
  }
  void PreProcess() {
    int sourceSize = Math.min(10,V);
    queries = new int[sourceSize][V];
    for(int i=0; i < sourceSize; i++) {
      dijkstra(i);
      for(int j=0; j < V; j++) {
        queries[i][j] = distance[j];
      }
    }
  }
  int Query(int s, int t, int k) {
    int ans = -1;

    // You have to report the shortest path from Ket Fah's current position s
    // to reach the chosen hospital t, output -1 if t is not reachable from s
    // with one catch: this path cannot use more than k vertices   

    // Compute all queries via dijkstra
    ans = queries[s][t];
    if(ans == MAXWEIGHT)
      ans = -1;
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
      // initialise queries array to adjacency matrix
      for (int i = 0; i < V; i++) {
        AdjList.add(new ArrayList < IntegerPair >());

        int k = sc.nextInt();
        while (k-- > 0) {
          int j = sc.nextInt(), w = sc.nextInt();
          AdjList.get(i).add(new IntegerPair(j, w)); // edge (road) weight (in minutes) is stored here
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
    if (this.second() > o.second())
      return 1;
    else if(this.second() < o.second())
      return -1;
    else
      return 0;
  }

  Integer first() { return _first; }
  Integer second() { return _second; }
}