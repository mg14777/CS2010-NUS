// Copy paste this Java Template and save it as "GettingFromHereToThere.java"
import java.util.*;
import java.io.*;

// write your matric number here: A0153196B
// write your name here: Mudit Gupta
// write list of collaborators here:
// year 2017 hash code: x4gxK7xzMSlNvFsMEUVn (do NOT delete this line)

class GettingFromHereToThere {
  private int V; // number of vertices in the graph (number of rooms in the building)
  private ArrayList < ArrayList < IntegerPair > > AdjList; // the weighted graph (the building), effort rating of each corridor is stored here too
  private ArrayList<IntegerTriple> edgeList;
  // if needed, declare a private data structure here that
  // is accessible to all methods in this class
  // --------------------------------------------
  private UFDS ufds;
  private int[][] queries;
  private int[] dfsVisited;
  private ArrayList<ArrayList<IntegerPair>> MST;
  // --------------------------------------------

  public GettingFromHereToThere() {
    // Write necessary codes during construction;
    //
    // write your answer here



  }
  /**
  Original technique of computing maximum weight for a single path from a given source vertex to a destination vertex in a single DFS search
  **/
  int weightViaDFS(int source,int destination) {
    dfsVisited[source] = 1;
    int max_weight = -1;
      for(IntegerPair edge : MST.get(source)) {
        if(edge.first() == destination)
          return Math.max(max_weight,edge.second());
        else if(dfsVisited[edge.first()] == 0) {
          int branch_max = edge.second();
          int subtree_max = weightViaDFS(edge.first(),destination);
          if(subtree_max == -1)
            branch_max = -1;
          else {
            branch_max = Math.max(branch_max,subtree_max);
          }
          max_weight = Math.max(max_weight,branch_max);
        }
      }
      return max_weight;
  }
  int maxWeight(int source,int destination) {
    dfsVisited = new int[V];
    int max_weight;
    if(source == destination)
      max_weight = 0;
    else
      max_weight = weightViaDFS(source,destination);
    return max_weight;
  }
  /**
  Optimized technique of computing maximum weight for all paths from a given source vertex in a single DFS search
  Reduces complexity by a factor of V
  **/
  void maxWeightOptimized(int org_source,int cur_source,int cur_max_weight) {
    dfsVisited[cur_source] = 1;
    for(IntegerPair edge: MST.get(cur_source)) {
      int cur_neighbour = edge.first();
      if(dfsVisited[cur_neighbour] == 0) {
        queries[org_source][cur_neighbour] = Math.max(edge.second(),cur_max_weight);
        maxWeightOptimized(org_source,cur_neighbour,queries[org_source][cur_neighbour]);
      }
    }
  }
  ArrayList<ArrayList<IntegerPair>> kruskalMST() {
    MST  = new ArrayList<ArrayList<IntegerPair>>(V);
    for(int i=0; i < V; i++)
      MST.add(new ArrayList<IntegerPair>());
    for(IntegerTriple edge: edgeList) {
      if(!ufds.isSameSet(edge.first(),edge.second())) {
        MST.get(edge.first()).add(new IntegerPair(edge.second(),edge.third()));
        MST.get(edge.second()).add(new IntegerPair(edge.first(),edge.third()));
        ufds.unionByRank(edge.first(),edge.second());
      }
    }
    return MST;
  }
  void computeAllQueries() {
    int sourceSize = Math.min(10,V);
    queries = new int[sourceSize][V];
    for(int s=0; s < sourceSize; s++) {
        dfsVisited = new int[V];
        maxWeightOptimized(s,s,0);
    }
  }
  void PreProcess() {
    int i=0;
    edgeList = new ArrayList<IntegerTriple>();
    for(ArrayList<IntegerPair> edges : AdjList) {
      for(IntegerPair edge : edges) {
        if(i < edge.first())
          edgeList.add(new IntegerTriple(i,edge.first(),edge.second()));
      }
      i++;
    }
    ufds = new UFDS(V);
    Collections.sort(edgeList,new IntegerTripleComparator());
    kruskalMST();
    computeAllQueries();

  }

  int Query(int source, int destination) {
    int ans = 0;
    // You have to report the weight of a corridor (an edge)
    // which has the highest effort rating in the easiest path from source to destination for the wheelchair bound
    ans = queries[source][destination];
    return ans;
  }


  void run() throws Exception {
    // do not alter this method
    IntegerScanner sc = new IntegerScanner(System.in);
    PrintWriter pr = new PrintWriter(new BufferedWriter(new OutputStreamWriter(System.out)));

    int TC = sc.nextInt(); // there will be several test cases
    while (TC-- > 0) {
      V = sc.nextInt();

      // clear the graph and read in a new graph as Adjacency List
      AdjList = new ArrayList < ArrayList < IntegerPair > >();
      for (int i = 0; i < V; i++) {
        AdjList.add(new ArrayList < IntegerPair >());

        int k = sc.nextInt();
        while (k-- > 0) {
          int j = sc.nextInt(), w = sc.nextInt();
          AdjList.get(i).add(new IntegerPair(j, w)); // edge (corridor) weight (effort rating) is stored here
        }
      }

      PreProcess(); // you may want to use this function or leave it empty if you do not need it

      int Q = sc.nextInt();
      while (Q-- > 0)
        pr.println(Query(sc.nextInt(), sc.nextInt()));
      pr.println(); // separate the answer between two different graphs
    }

    pr.close();
  }

  public static void main(String[] args) throws Exception {
    // do not alter this method
    GettingFromHereToThere ps4 = new GettingFromHereToThere();
    ps4.run();
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


class IntegerTripleComparator implements Comparator< IntegerTriple > {
  public int compare(IntegerTriple a,IntegerTriple b) throws ClassCastException {
    if(a.third().equals(b.third()))
      return 0;
    else if(a.third() > b.third()) 
      return 1;
    else
      return -1;
  }
}
class IntegerTriple implements Comparable < IntegerTriple > {
  Integer _first, _second, _third;

  public IntegerTriple(Integer f, Integer s, Integer t) {
    _first = f;
    _second = s;
    _third = t;
  }

  public int compareTo(IntegerTriple o) {
    if (!this.first().equals(o.first()))
      return this.first() - o.first();
    else if (!this.second().equals(o.second()))
      return this.second() - o.second();
    else
      return this.third() - o.third();
  }

  Integer first() { return _first; }
  Integer second() { return _second; }
  Integer third() { return _third; }
}
class UFDS {
  int[] parent,rank;
  UFDS(int size) {
    parent = new int[size];
    rank = new int[size];
    for(int i=0; i < parent.length; i++)
      parent[i] = i;
  }
  int findSet(int elem) {
    if(elem != parent[elem]) {
      parent[elem] = findSet(parent[elem]);   // Path Compression
      return parent[elem];
    }
    else
      return elem;
  }
  Boolean isSameSet(int elem1, int elem2) {
    if(findSet(elem1) == findSet(elem2))
      return true;
    else
      return false;
  }
  void unionByRank(int elem1,int elem2){
    int parent1 = findSet(elem1);
    int parent2 = findSet(elem2);
    if(parent1 != parent2) {
      if(rank[elem1] > rank[elem2]) {
        parent[parent2] = parent1; 
      }
      else if(rank[elem1] < rank[elem2]) {
        parent[parent1] = parent2;
      }
      else {
        parent[parent1] = parent2;
        rank[parent2] += 1;
      }
    }
  }
}
