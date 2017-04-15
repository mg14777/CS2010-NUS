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
  List<Patient> malePatients;
  List<Patient> femalePatients;

  class Patient {
    String name;
    int gender;
    Patient(String name,int gender) {
      this.name = name;
      this.gender = gender;
    }
  }

  public PatientNames() {
    // Write necessary code during construction;
    malePatients = new ArrayList<Patient>();
    femalePatients = new ArrayList<Patient>();   
  }
  boolean naiveSearch(String name,String start,String end) {
    List<String> prefixList = new ArrayList<String>();
    for(int endIndex=0; endIndex < name.length(); endIndex++) {
      prefixList.add(name.substring(0,endIndex));
    }
    for(String prefix:prefixList)
      if(prefix.compareTo(start) >= 0 && prefix.compareTo(end) < 0)
        return true;

    return false;
  }
  void AddPatient(String patientName, int gender) {
    // You have to insert the information (patientName, gender)
    // into your chosen data structure
    if(gender == 1)
      malePatients.add(new Patient(patientName,gender));
    if(gender == 2) {
      femalePatients.add(new Patient(patientName,gender));
    }
  }

  void RemovePatient(String patientName) {
    // You have to remove the patientName from your chosen data structure
    int index;
    for(index=0; index<malePatients.size();index++) {
      String name = malePatients.get(index).name;
      if(name.equals(patientName)) {
        malePatients.remove(index);
        return;
     }
   }
    for(index=0; index<femalePatients.size();index++) {
      String name = femalePatients.get(index).name;
      if(name.equals(patientName)) {
        femalePatients.remove(index);
        return;
     }
   }
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
      for(Patient malePatient: malePatients)
        if(naiveSearch(malePatient.name,START,END))
          ans++;
    }
    if(gender == 2 || gender == 0) {
      for(Patient femalePatient: femalePatients)
        if(naiveSearch(femalePatient.name,START,END))
          ans++;
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