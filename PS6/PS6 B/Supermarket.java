// Copy paste this Java Template and save it as "Supermarket.java"
import java.util.*;
import java.io.*;

// write your matric number here: A0153196B
// write your name here: Mudit Gupta
// write list of collaborators here:
// year 2017 hash code: mZGXoXQ2wBX0FK7WCZun (do NOT delete this line)

class Supermarket {
  private int N; // number of items in the supermarket. V = N+1 
  private int K; // the number of items that Ketfah has to buy
  private int[] shoppingList; // indices of items that Ketfah has to buy
  private int[][] T; // the complete weighted graph that measures the direct wheeling time to go from one point to another point in seconds
  private int[][] modified_distance_matrix; // Modified to store the shortest distance from an item in the shopping list to every other vertex
  // if needed, declare a private data structure here that
  // is accessible to all methods in this class
  // --------------------------------------------
  private PriorityQueue<IntegerPair> pqueue;
  private int[][] memo;


  public Supermarket() {
    // Write necessary code during construction
    //
    // write your answer here



  }
  private int[] initSSSP(int s) {
    int[] distance = new int[N+1];
    for(int i=0; i < N+1;i++)
      distance[i] = Integer.MAX_VALUE;
    distance[s] = 0;
    return distance;
    
  }
  private int[] dijkstra(int s) {
    int[] distance = initSSSP(s);
    PriorityQueue<IntegerPair> pqueue = new PriorityQueue<>();
    pqueue.offer(new IntegerPair(s,0));
    while(!pqueue.isEmpty()) {
      IntegerPair pair = pqueue.poll();
      int cur_vertex = pair.first();
      int cur_distance = pair.second();
      if(cur_distance == distance[cur_vertex]) {
        for(int neighbour=0; neighbour < N+1;neighbour++) {
          if(distance[neighbour] > distance[cur_vertex] + T[cur_vertex][neighbour]) {
            distance[neighbour] = distance[cur_vertex] + T[cur_vertex][neighbour];
            pqueue.offer(new IntegerPair(neighbour,distance[neighbour]));
          }
        }
      }
    }
    return distance;
  }
  private void debug() {
    for(int i=0; i < N+1; i++) {
      for(int j=0; j < N+1; j++) {
        System.out.print(modified_distance_matrix[i][j]+" ");
      }
      System.out.println();
    }
  }
  private void buildModifiedMatrix() {
    modified_distance_matrix = new int[N+1][N+1];

    // Copy T matrix first
    for(int i=0; i < N+1; i++)
      for(int j=0; j < N+1; j++) {
        modified_distance_matrix[i][j] = T[i][j];
      }
    for(int i=0; i < K;i++) {
      int[] distance = dijkstra(shoppingList[i]);
      for(int j=0; j < N+1; j++) {
        modified_distance_matrix[shoppingList[i]][j] = distance[j];
        modified_distance_matrix[j][shoppingList[i]] = distance[j];
      }
    }
  }
  private void initMemo() {
    memo = new int[N+1][1<<K];
    for(int i=0; i < memo.length; i++)
      for(int j=0; j < memo[i].length; j++) {
        memo[i][j] = -1;
      }
  }
  private int tsp(int pos,int mask) {

    if(((1 << K) - 1) == mask)
      return modified_distance_matrix[pos][0];

    if(memo[pos][mask] != -1)
      return memo[pos][mask];

    memo[pos][mask] = Integer.MAX_VALUE;
    for(int i=0; i < K;i++) {
      if((mask & (1 << i)) == 0)
        memo[pos][mask] = Math.min(memo[pos][mask],modified_distance_matrix[pos][shoppingList[i]] + tsp(shoppingList[i],mask | (1 << i)));
    }
    return memo[pos][mask];

  }
  
  int Query() {
    int ans = 0;

    // You have to report the quickest shopping time that is measured
    // since Ketfah enters the supermarket (vertex 0),
    // completes the task of buying K items in that supermarket,
    // then reaches the cashier of the supermarket (back to vertex 0).
    buildModifiedMatrix();
    initMemo();
    ans = tsp(0,0);

    return ans;
  }

  // You can add extra function if needed
  // --------------------------------------------



  void run() throws Exception {
    // do not alter this method to standardize the I/O speed (this is already very fast)
    IntegerScanner sc = new IntegerScanner(System.in);
    PrintWriter pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(System.out)));

    int TC = sc.nextInt(); // there will be several test cases
    while (TC-- > 0) {
      // read the information of the complete graph with N+1 vertices
      N = sc.nextInt(); K = sc.nextInt(); // K is the number of items to be bought

      shoppingList = new int[K];
      for (int i = 0; i < K; i++)
        shoppingList[i] = sc.nextInt();

      T = new int[N+1][N+1];
      for (int i = 0; i <= N; i++)
        for (int j = 0; j <= N; j++)
          T[i][j] = sc.nextInt();

      pw.println(Query());
    }

    pw.close();
  }

  public static void main(String[] args) throws Exception {
    // do not alter this method
    Supermarket ps6 = new Supermarket();
    ps6.run();
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