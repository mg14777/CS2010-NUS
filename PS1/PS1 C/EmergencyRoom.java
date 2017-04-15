import java.util.*;
import java.io.*;

// write your matric number here: A0153196B
// write your name here: Mudit Gupta
// write list of collaborators here:
// year 2017 hash code: oIxT79fEI2IQdQqvg1rx (do NOT delete this line)

class EmergencyRoom {
  MaxHeap queue;
  HashMap<String,Integer> indexes = new HashMap<>();
  static final int SENTINEL = 200;
  int orderCounter = 0;
  class Patient {
    int key;
    String patientName;
    int order;
    Patient(int emergencyLv, String patientName,int order) {
      this.key = emergencyLv;
      this.patientName = patientName;
      this.order = order;
    }
  }
  class PQComparator implements Comparator<Patient> {
    public int compare(Patient entry1, Patient entry2) {
      if(entry1.key < entry2.key)
        return 1;
      else if(entry1.key > entry2.key)
        return -1;
      else {
        if(entry1.order > entry2.order)
          return 1;
        else if(entry1.order < entry2.order)
          return -1;
        else
          return 0;
      }
    }
  }
  class MaxHeap  {
    List<Patient> maxHeap = new ArrayList<>();
    Comparator<Patient> comp;

    MaxHeap(Comparator<Patient> c) {
      comp = c;
    }
    int compare(Patient p1,Patient p2) {
      return comp.compare(p1,p2);
    }
    int parent(int i) {
      return (i-1)/2;
    }
    int left(int i) {
      return 2*i + 1;
    }
    int right(int i) {
      return 2*i + 2;
    }
    boolean isEmpty() {
      return (maxHeap.size() == 0);
    }
    int size() {
      return maxHeap.size();
    }
    boolean hasLeft(int i) {
      return (2*i + 1) < maxHeap.size();
    }
    boolean hasRight(int i) {
      return (2*i + 2) < maxHeap.size();
    }
    void swap(int i1, int i2) {
      if(i1 == i2)
        return;
      Patient temp = maxHeap.get(i1);

      indexes.put(temp.patientName,i2);
      indexes.put(maxHeap.get(i2).patientName,i1);

      maxHeap.set(i1,maxHeap.get(i2));
      maxHeap.set(i2,temp);
    }
    void heapUp(int index) {
      int i = index;
      while(i > 0) {
        if(compare(maxHeap.get(parent(i)),maxHeap.get(i)) > 0)
          swap(i,parent(i));
        else
          break;
        i = parent(i);
      }
    }
    void heapDown(int index) {
      int i = index;
      int max_id;
      while(hasLeft(i)) {
        max_id = i;
        Patient currentNode = maxHeap.get(i);
        Patient leftChild = maxHeap.get(left(i));
        Patient maxNode = currentNode;
        if(compare(currentNode,leftChild) > 0) {
          max_id = left(i);
          maxNode = leftChild; 
        }
        if(hasRight(i)) {
          Patient rightChild = maxHeap.get(right(i));
          if(compare(maxNode,rightChild) > 0) {
            max_id = right(i);
            maxNode = rightChild;
          }
        }
        if(max_id == i)
          break;
        swap(i,max_id);
        i = max_id;
      }
    }
    void insert(int key, String value) {
      Patient newEntry = new Patient(key,value,orderCounter);
      orderCounter++;
      maxHeap.add(newEntry);
      indexes.put(newEntry.patientName,maxHeap.size()-1);
      heapUp(maxHeap.size()-1);
    }
    Patient removeMax() {
      if(maxHeap.size() == 0)
        return null;
      Patient maxElem = maxHeap.get(0);
      indexes.put(maxHeap.get(maxHeap.size()-1).patientName,0);
      maxHeap.set(0,maxHeap.get(maxHeap.size()-1));
      maxHeap.remove(maxHeap.size()-1);
      heapDown(0);
      return maxElem;
    }
    Patient peek() {
      if(isEmpty())
        return null;
      return maxHeap.get(0);
    }
    int searchIndex(String value) {
      for(int i=0; i < maxHeap.size();i++)
        if(maxHeap.get(i).patientName.equals(value))
          return i;
      return -1;
    }
  }
  public EmergencyRoom() {
    queue = new MaxHeap(new PQComparator());
  }
  void ArriveAtHospital(String patientName, int emergencyLvl) {
    // You have to insert the information (patientName, emergencyLvl)
    // into your chosen data structure
    queue.insert(emergencyLvl,patientName);
  }
  void UpdateEmergencyLvl(String patientName, int incEmergencyLvl) {
    // You have to update the emergencyLvl of patientName to
    // emergencyLvl += incEmergencyLvl
    // and modify your chosen data structure (if needed)
    int index = indexes.get(patientName);
    queue.maxHeap.get(index).key += incEmergencyLvl;
    queue.heapUp(index);
  }
  void Treat(String patientName) {
    // remove him/her from your chosen data structure
    UpdateEmergencyLvl(patientName,SENTINEL);
    queue.removeMax();
  }

  String Query() {
    String ans = "The emergency room is empty";
    if(queue.isEmpty())
      return ans;
    else
      ans = queue.peek().patientName;
    return ans;
  }

  void run() throws Exception {
    // do not alter this method

    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    PrintWriter pr = new PrintWriter(new BufferedWriter(new OutputStreamWriter(System.out)));
    int numCMD = Integer.parseInt(br.readLine()); // note that numCMD is >= N
    while (numCMD-- > 0) {
      StringTokenizer st = new StringTokenizer(br.readLine());
      int command = Integer.parseInt(st.nextToken());
      switch (command) {
        case 0: ArriveAtHospital(st.nextToken(), Integer.parseInt(st.nextToken())); break;
        case 1: UpdateEmergencyLvl(st.nextToken(), Integer.parseInt(st.nextToken())); break;
        case 2: Treat(st.nextToken()); break;
        case 3: pr.println(Query()); break;
      }
    }
    pr.close();
  }

  public static void main(String[] args) throws Exception {
    // do not alter this method
    EmergencyRoom ps1 = new EmergencyRoom();
    ps1.run();
  }
}