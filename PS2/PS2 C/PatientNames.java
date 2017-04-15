// Copy paste this Java Template and save it as "PatientNames.java"
import java.util.*;
import java.io.*;

// write your matric number here: A0153196B
// write your name here: Mudit Gupta
// write list of collaborators here:
// year 2017 hash code: DZAjKugdE9QiOQKGFbut (do NOT delete this line)

class PatientNames {
  // if needed, declare a private data structure here that
  // is accessible to all methods in this class
  AVLTree malePatients;
  AVLTree femalePatients;

  class Patient {
    String name;
    int gender;
    Patient left,right,parent;
    int height,size;
    Patient(String name,int gender) {
      this.name = name;
      this.gender = gender;
      left = right = parent = null;
      height = 0;
      size = 1;
    }
  }

  public PatientNames() {
    // Write necessary code during construction;
    malePatients = new AVLTree();
    femalePatients = new AVLTree();   
  }
  public class AVLTree {
    Patient root;
    AVLTree() {
      root = null;
    }
    int compare(String key1, String key2) {
      return key1.compareTo(key2);
    }
    int getHeight(Patient T) {
      if(T == null)
        return -1;
      return T.height;
    }
    int getSize(Patient T) {
      if(T == null)
        return 0;
      return T.size;
    }
    int recomputeHeight(Patient T) {
      int leftHeight = getHeight(T.left);
      int rightHeight = getHeight(T.right);
      if(leftHeight > rightHeight)
        return leftHeight+1;
      else
        return rightHeight+1;
    }
    int recomputeSize(Patient T) {
      int leftTreeSize = getSize(T.left);
      int rightTreeSize = getSize(T.right);
      return leftTreeSize + rightTreeSize + 1;
    }
    int calculateBalanceFactor(Patient T) {
      return getHeight(T.left) - getHeight(T.right);
    }
    Patient rotateLeft(Patient T) {
      Patient w = T.right;
      w.parent = T.parent;
      T.parent = w;
      T.right = w.left;
      if(w.left != null)
        w.left.parent = T;
      w.left = T;
      T.height = recomputeHeight(T);
      T.size = recomputeSize(T);
      w.height = recomputeHeight(w);
      w.size = recomputeSize(w);
      return w;
    }
    Patient rotateRight(Patient T) {
      Patient w = T.left;
      w.parent = T.parent;
      T.parent = w;
      T.left = w.right;
      if(w.right != null)
        w.right.parent = T;
      w.right = T;
      T.height = recomputeHeight(T);
      T.size = recomputeSize(T);
      w.height = recomputeHeight(w);
      w.size = recomputeSize(w);
      return w;
    }
    int rank(String elem) {
      return rank(root,elem);
    }
    int rank(Patient T,String elem) {
      if(T == null)
        return 1; // if element doesn't exist then rank of ceiling of elem is given
      else if(compare(elem,T.name) == 0)
        return getSize(T.left) + 1;
      else if(compare(elem,T.name) < 0)
        return rank(T.left,elem);
      else
        return getSize(T.left) + 1 + rank(T.right,elem);
    }
    int rangeCount(String from,String to) {
      return rank(to) - rank(from);
    }
    Patient balanceSubtree(Patient T) {
      int balanceFactor = calculateBalanceFactor(T);
      if(Math.abs(balanceFactor) < 2)
        return T;
      if(balanceFactor == 2) {    // BF of 2 implies left subtree is bigger than right
        int leftChildBF = calculateBalanceFactor(T.left);
        if(leftChildBF <= 1 && leftChildBF >= 0)
          T = rotateRight(T);
        else if(leftChildBF == -1) {
          T.left = rotateLeft(T.left);
          T = rotateRight(T);
        }
      }
      else if(balanceFactor == -2) {
        int rightChildBF = calculateBalanceFactor(T.right);
        if(rightChildBF == 1) {
          T.right = rotateRight(T.right);
          T = rotateLeft(T);
        }
        else if(rightChildBF >= -1 && rightChildBF <= 0) {
          T = rotateLeft(T);
        }
      }
      return T;
    }
  Patient findMin() {
    return findMin(root);
  }
  Patient findMin(Patient T) {
    if(T == null) {
      return T;
    }
    while(T.left != null)
      T = T.left;
    return T;
  }
  Patient findSuccessor(Patient T) {
    if(T.right != null){
      Patient successor = findMin(T.right);
      return successor;
    }
    else {
      Patient cur = T;
      Patient par = cur.parent;
      while((par != null) && (par.right == cur)) {
        cur = par;
        par = cur.parent;
      }
      if(par == null) {
        return null;
      }
      return par;
    }
  }
  void delete(String key) {
    root = delete(root,key);
  }
  Patient delete(Patient T,String key) {
    if(T == null) {
      return T;
    }
    else if(compare(key,T.name) < 0) {
      T.left = delete(T.left,key);
    }
    else if(compare(key,T.name) > 0) {
      T.right = delete(T.right,key);
    }
    else {
      if(T.left == null && T.right == null)
        T = null;
      else if(T.left != null && T.right == null) {
        T.left.parent = T.parent;
        T = T.left;
      }
      else if(T.left == null && T.right != null) {
        T.right.parent = T.parent;
        T = T.right;
      }
      else {
        Patient successor = findSuccessor(T);
        T.name = successor.name;
        T.right = delete(T.right,successor.name);
      }
    }
    if(T != null) {
      T = balanceSubtree(T);
      T.height = recomputeHeight(T);
      T.size = recomputeSize(T);
    }
    return T;
  }
    void preorder() {
      preorderTraversal(root);
      System.out.println();
    }
    void preorderTraversal(Patient T) {
      if(T == null)
        return;
      System.out.print(T.name+" ");
      preorderTraversal(T.left);
      preorderTraversal(T.right);
    }
    public void insert(String key,int value) {
     root = insert(root,key,value);
    }
    Patient insert(Patient T,String key,int value) {
      if(T == null)
        return new Patient(key,value);
      if(compare(key,T.name) < 0) {
        T.left = insert(T.left,key,value);
        T.left.parent = T;
      }
      else {
        T.right = insert(T.right,key,value);
        T.right.parent = T;
      }
      T = balanceSubtree(T);
      T.height = recomputeHeight(T);
      T.size = recomputeSize(T);
      return T;
    }
  }
  void AddPatient(String patientName, int gender) {
    // You have to insert the information (patientName, gender)
    // into your chosen data structure
    if(gender == 1)
      malePatients.insert(patientName,gender);
    if(gender == 2) {
      femalePatients.insert(patientName,gender);
    }
    //System.out.println(malePatients.calculateBalanceFactor(malePatients.root));
  }

  void RemovePatient(String patientName) {
    // You have to remove the patientName from your chosen data structure
      malePatients.delete(patientName);
      femalePatients.delete(patientName);
  }

  int Query(String START, String END, int gender) {
    int ans = 0;
    // You have to answer how many patient name starts
    // with prefix that is inside query interval [START..END)
    /**
    IMP NOTE: for any 2 strings x and s:
    if s < x (lexicographically) => s < prefix(x) for any prefix of x
    */
    if(gender == 1 || gender == 0) {
      ans += malePatients.rangeCount(START,END);
    }
    if(gender == 2 || gender == 0) {
      ans += femalePatients.rangeCount(START,END);
    }
    return ans;
  }

  void run() throws Exception {
    // do not alter this method to avoid unnecessary errors with the automated judging
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    PrintWriter pr = new PrintWriter(new BufferedWriter(new OutputStreamWriter(System.out)));
    while (true) {
      StringTokenizer st = new StringTokenizer(br.readLine());
      int command = Integer.parseInt(st.nextToken());
      if (command == 0) // end of input
        break;
      else if (command == 1) // AddPatient
        AddPatient(st.nextToken(), Integer.parseInt(st.nextToken()));
      else if (command == 2) // RemovePatient
        RemovePatient(st.nextToken());
      else // if (command == 3) // Query
        pr.println(Query(st.nextToken(), // START
                         st.nextToken(), // END
                         Integer.parseInt(st.nextToken()))); // GENDER
    }
    pr.close();
  }

  public static void main(String[] args) throws Exception {
    // do not alter this method to avoid unnecessary errors with the automated judging
    PatientNames ps2 = new PatientNames();
    ps2.run();
  }
}