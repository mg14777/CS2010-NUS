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
  private int[] hops;
  private PriorityQueue<Node> ssspPQ;
  private int[] shortest_path;
  private int[] visited;
  private int[][] path_table;
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
    hops = new int[V];
    ssspPQ = new PriorityQueue<>();
    for(int i=0; i < V;i++) {
      distance[i] = MAXWEIGHT;
      hops[i] = MAXWEIGHT;
    }
    distance[s] = 0;
    hops[s] = 1;
    ssspPQ.add(new Node(s,0,1));
  }
  boolean relax(int parent,int neighbour,int weight,int k,int t) {
    if(hops[parent] + 1 <= k) {
      if(distance[t] == MAXWEIGHT) {
        distance[neighbour] = distance[parent] + weight;
        hops[neighbour] = hops[parent] + 1;
        return true;
      }
      if(distance[neighbour] > distance[parent] + weight) {
        distance[neighbour] = distance[parent] + weight;
        hops[neighbour] = hops[parent] + 1;
        return true;
      }
      if((distance[neighbour] == distance[parent] + weight) && (hops[neighbour] > hops[parent] + 1)) {
        hops[neighbour] = hops[parent] + 1;
        return true;
      }
    }
    return false;
  }
  void augmentedDijkstra(int s,int t,int k) {
    initSSSP(s);
    while(ssspPQ.size() != 0) {
      Node node = ssspPQ.poll();
      int parent = node.getVertex();
      //if(parent == t)
       // return;
      if(node.compareTo(new Node(parent,distance[parent],hops[parent])) == 0 || distance[t] == MAXWEIGHT) {
        for(IntegerPair edge : AdjList.get(parent)) {
          int neighbour = edge.first();
          int weight = edge.second();
          boolean relaxed = relax(parent,neighbour,weight,k,t);
          if(relaxed)
            ssspPQ.add(new Node(neighbour,distance[neighbour],hops[neighbour]));
        }
      }
    }
  }
  void PreProcess() {

  }
  /*
  void initDP(int t) {
    shortest_path = new int[V];
    hops = new int[V];
    visited = new int[V];
    for(int i=0; i < V;i++) {
      shortest_path[i] = -1;
      hops[i] = MAXWEIGHT;
      visited[i] = 0;
    }
    hops[t] = 1;
  }
  */
  void initDP(int t,int k) {
    path_table = new int[V][k];
    for(int i=0; i < V;i++)
      for(int j=0; j < k; j++) {
        if(i==t)
          path_table[i][j] = 0;
        else if(j==0)
          path_table[i][j] = MAXWEIGHT;
        else
          path_table[i][j] = -1;
    }
  }
  int DPShortestPaths(int current,int remaining_hops) {
    int ans = MAXWEIGHT;
    if(remaining_hops == 0)
      return path_table[current][remaining_hops];
    if(path_table[current][remaining_hops] != -1)
      return path_table[current][remaining_hops];
    for(IntegerPair edge: AdjList.get(current)) {
      int neighbour = edge.first();
      int weight = edge.second();
      int path_weight = DPShortestPaths(neighbour,remaining_hops-1) + weight;
      ans = Math.min(ans,path_weight);
    }
    path_table[current][remaining_hops] = ans;
    return ans;
  }
  /*
  int DPShortestPaths(int current,int remaining_hops,int t,int k) {
    System.out.println("Current: "+current+"   Remaining Hops: "+ remaining_hops);
    int ans = MAXWEIGHT;
    int hops_ans = MAXWEIGHT;
    visited[current] = 1;
    
    if(remaining_hops == 0)
      return MAXWEIGHT;
    
    if(current == t)
      return 0;
    if(shortest_path[current] != -1)
      return shortest_path[current];
    for(IntegerPair edge : AdjList.get(current)) {
      int neighbour = edge.first();
      int weight = edge.second();
      if(visited[neighbour] == 0) {
        int path_weight = DPShortestPaths(neighbour,remaining_hops-1,t,k)+weight;
        if(!(hops[neighbour] + 1 > k)) {
          ans = Math.min(ans,path_weight);
          hops_ans = hops[neighbour] + 1;
        }
      }
      else if(shortest_path[neighbour] != -1) {
        int path_weight = shortest_path[neighbour]+weight;
        if(!(hops[neighbour] + 1 > k)) {
          ans = Math.min(ans,path_weight);
          hops_ans = hops[neighbour] + 1;
        }
      }
    }
    shortest_path[current] = ans;
    hops[current] = hops_ans;
    System.out.println("Current: "+current+"   Hops: "+hops[current] +"   Distance: "+ans);
    return ans;
  }
  */
  int Query(int s, int t, int k) {
    int ans = -1;
    // You have to report the shortest path from Ket Fah's current position s
    // to reach the chosen hospital t, output -1 if t is not reachable from s
    // with one catch: this path cannot use more than k vertices   
    initDP(t,k);
    //System.out.println(k);
    ans = DPShortestPaths(s,k-1);
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

class Node implements Comparable<Node> {
  Integer vertex, distance, hops;
  public Node(int vertex,int distance,int hops) {
    this.vertex = vertex;
    this.distance = distance;
    this.hops = hops;
  }
  public int compareTo(Node n) {
    if(this.getDistance() > n.getDistance())
      return 1;
    else if(this.getDistance() < n.getDistance())
      return -1;
    else {
      if(this.getHops() > n.getHops())
        return 1;
      else if(this.getHops() < n.getHops())
        return -1;
      else
        return 0;
    }
  }
  public int getVertex() {
    return vertex;
  }
  public int getDistance() {
    return distance;
  }
  public int getHops() {
    return hops;
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