import java.util.*;
import java.io.*;

// Sharon Yang on Mar 6, 2014
// This program reads from an input file to output
// the desired result shown on Quora website here:
// http://www.quora.com/challenges (Challenge 1)
// to generate 'Nearby' topics and questions based
// on input queries.

// Input: a text file containing topics, questions,
// and queries.
// Output: a printout to the terminal windows showing
// the result.

public class Solution_Array{
  // here is a series of constraints from the Quora site:
  public static final int T_MAX = 10000;
  public static final int Q_MAX = 1000;
  public static final int N_MAX = 10000;
  public static final int MIN = 1;
  public static Scanner read = new Scanner(System.in);
  
  public static void main (String [] args) {
    Solution_Array run = new Solution_Array();
    String file_string = "";
    
    file_string = read.nextLine();
    int[] para = new int[3]; // this is where the parameters are stored
    
    // get the input data file from user    
    
    // checks whether parameters make sense and then create arrays.
    para = run.findParameters(file_string); // this is the input file for parameters
    
    int t_lines = para[0];
    int q_lines = para[1];
    int n_lines = para[2];
    //System.out.println();
    //System.out.println(para[0] + " " + para[1] + " " + para[2] + " ");
    
    double[][] t_entries = new double[3][t_lines];
    int[][] q_entries = new int[5][q_lines];
    double[][] n_entries = new double[4][n_lines];
    
    t_entries = run.readT(t_lines);
    q_entries = run.readQ(q_lines);
    n_entries = run.readN(n_lines);
    
    System.out.println();
    // for each query find distances and print out result
    run.matchCal(n_entries, q_entries, t_entries);
  }
  
  // this method goes through the queries and distributes the input to
  // either T related or Q related.
  public void matchCal(double[][] n_arr, int[][] q_arr, double[][] t_arr) {
    for (int i = 0; i < n_arr[0].length; i++) {
      if (n_arr[0][i] == 0) { // then the query is about a topic
        findTDist(n_arr[2][i], n_arr[3][i], t_arr, (int)n_arr[1][i]);
      }
      else {
        findQDist(n_arr[2][i], n_arr[3][i], q_arr, (int)n_arr[1][i]);
      }
    }
  }
  
  // give a query that is related to a question, generate all distances based
  // on the questions listed and print out the closest ones within a given max.
  public void findQDist(double x, double y, int[][] entry, int max) {
    double[][] store_id = new double[entry[0].length][2];
    for (int i = 0; i < entry[0].length; i++) {
      if (entry[1][i] != 0) {
        store_id[i][0] = entry[0][i];
        store_id[i][1] = calDistance(x, y, (double)entry[1][i], (double)entry[2][i]);
      }
      else {
        store_id[i][0] = -1;
      }
    }
    
    java.util.Arrays.sort(store_id, new java.util.Comparator<double[]>() {
      public int compare(double[] a, double[] b) {
        if (Double.compare(a[1], b[1]) != 0)
          return Double.compare(a[1], b[1]);
        else {
          return Double.compare(-a[0], -b[0]);
        }
      }
    });
    /* print out each id with distance respect to a given query
    for (int i = 0; i < entry[0].length; i++) {
      System.out.print(store_id[i][0] + " " + store_id[i][1]);
      System.out.println();
    }
    */
    
    //System.out.println(entry[0].length);
    
    //System.out.println("output:");
    
    int pointer = 0;
    while (max != 0 && pointer != entry[0].length) {
      if (store_id[pointer][0] != -1) {
        System.out.print((int)store_id[pointer][0] + " ");
        max--;
      }
      pointer++;
    }
    System.out.println();
  }
  
  // given a query related to a topic and its location, list out all distances
  // select the closest IDs, and print out
  public void findTDist(double x, double y, double[][] entry, int max) {
    double[][] store_id = new double[entry[0].length][2];
    for (int i = 0; i < entry[0].length; i++) {
      store_id[i][0] = entry[0][i];
      store_id[i][1] = calDistance(x, y, entry[1][i], entry[2][i]);
    }
    
    java.util.Arrays.sort(store_id, new java.util.Comparator<double[]>() {
      public int compare(double[] a, double[] b) {
        if (Double.compare(a[1], b[1]) != 0)
          return Double.compare(a[1], b[1]);
        else {
          return Double.compare(-a[0], -b[0]);
        }
      }
    });
    
    /* print out sorted list of available T and distances
    for (int i = 0; i < entry[0].length; i++) {
      System.out.print(store_id[i][0] + " " + store_id[i][1]);
      System.out.println();
    }
    */
    
    //System.out.println("output:");
    
    int pointer = 0;
    while (max != 0 && pointer != entry[0].length) {
      System.out.print((int)store_id[pointer][0] + " ");
      max--;
      pointer++;
    }
    System.out.println();
  }
  
  // if there are repeated inputs that cause max to go over required max, there's a
  // block, and we need to generate a new max.
  public int newMax(double[][] store, int max, int len) {
    while (max < len && max > 0) {
      if (Math.abs(store[max - 1][1] - store[max][1]) < 0.001) {
        max--;
        //System.out.println("new max is " + max);
      }
      else {
        break;
      }
    }
    return max;
  }
  
  // create a 2-D array storing the topic data.
  // start readT
  public double[][] readT(int t_lines) {
    double[][] data = new double[3][t_lines];
    for (int i = 0; i < 3; i++) {
      for (int j = 0; j < t_lines; j++) {
        data[i][j] = 0; // all default to 0
      }
    }
    
    int count = 0;
    while(true) {
        String temp = read.nextLine();
        if (count >= 0) {
          //System.out.println(temp);
          int curr = 0;
          int space = 0;
          for (int j = 0; j < temp.length(); j++) {
            if (j == temp.length() - 1) {
              data[space][count] = Double.parseDouble(temp.substring(curr + 1));
            }
            else if (temp.charAt(j) == ' ' && space == 0) {
              space = 1;
              curr = j;
              data[0][count] = Double.parseDouble(temp.substring(0, curr));
            }
            else if (temp.charAt(j) == ' '){
              data[space][count] = Double.parseDouble(temp.substring(curr + 1, j));
              space++;
              curr = j;
            }
            
          }
        count++;
      }
      if (count == t_lines) break;
    }
    
    /* print out the T data 
    for (int i = 0; i < t_lines; i++){
      for (int j = 0; j < 3; j++) 
        System.out.print(data[j][i] + " ");
      System.out.println();
    }
    */
        
    return data;
  }
  // end readT
  
  // create a 2-D array storing the question data.
  // start readQ
  public int[][] readQ(int q_lines) {
    int[][] data = new int[5][q_lines];
    for (int i = 0; i < 5; i++) {
      for (int j = 0; j < q_lines; j++) {
        data[i][j] = 0; // all default to 0
      }
    }
    
      int count = 0;
      while(read.hasNext()) {
        String temp = read.nextLine();
        if (count >= 0) {
          int curr = 0;
          int space = 0;
          for (int j = 0; j < temp.length(); j++) {
            if (j == temp.length() - 1) {
              data[space][count] = Integer.parseInt(temp.substring(curr + 1));
            }
            else if (temp.charAt(j) == ' ' && space == 0) {
              space = 1;
              curr = j;
              data[0][count] = Integer.parseInt(temp.substring(0, curr));
            }
            else if (temp.charAt(j) == ' '){
              data[space][count] = Integer.parseInt(temp.substring(curr + 1, j));
              space++;
              curr = j;
            }
            
          }
          count++;
        }
        if (count == q_lines) break;
      }
    /* print out Q data
    for (int i = 0; i < q_lines; i++) {
      for (int j = 0; j < 5; j++)
        System.out.print(data[j][i] + " ");
      System.out.println();
    }
    */ 
    return data;
  }
  // end readQ
  
  // create a 2-D array storing the query data
  // start readN
  public double[][] readN(int n_lines) {
    double[][] data = new double[4][n_lines];
    for (int i = 0; i < 4; i++) {
      for (int j = 0; j < n_lines; j++) {
        data[i][j] = 0; // all default to 0
      }
    }
    
      int count = 0;
      while(read.hasNext()) {
        String temp = read.nextLine();
        if (count >= 0) {
          int curr = 0;
          int space = 0;
          for (int j = 0; j < temp.length(); j++) {
            if (j == temp.length() - 1) {
              data[space][count] = Double.parseDouble(temp.substring(curr + 1));
            }
            else if (temp.charAt(j) == ' ' && space == 0) {
              space = 1;
              curr = j;
              if (temp.charAt(0) == 't') { // 't' is a 0
                data[0][count] = 0;
              }
              else if (temp.charAt(0) == 'q') { // 'q' is a 1
                data[0][count] = 1;
              }
            }
            else if (temp.charAt(j) == ' '){
              data[space][count] = Double.parseDouble(temp.substring(curr + 1, j));
              space++;
              curr = j;
            }
            
          }
          count++;
        }
        if (count == n_lines) break;
      }

    
    /* print out N data
    for (int i = 0; i < n_lines; i++){
      for (int j = 0; j < 4; j++) 
        System.out.print(data[j][i] + " ");
      System.out.println();
    }
    */
       
    return data;
  }
  
  // end readN
  
  // find parameters based on the input files to verify whether the data makes sense.
  public int[] findParameters(String file) {
    int[] para = new int[3];
    int space = 0;
    for (int i = 0; i < file.length(); i++) {
      if (file.charAt(i) == ' ' && space == 0) {
        para[0] = Integer.parseInt(file.substring(0, i));
        space = i;
      }
      else if (file.charAt(i) == ' ') {
        para[1] = Integer.parseInt(file.substring(space + 1, i));
        space = i;
      }
      
      else if (i == file.length() - 1) {
        para[2] = Integer.parseInt(file.substring(space + 1));
      }
    }
      
      if (para[0] < MIN || para[0] > T_MAX) {
        System.out.println("Topic parameter is incorrect! ( > 0 and <= 10000 required.)");
        System.exit(1);
      }
      else if (para[1] < MIN || para[1] > Q_MAX) {
        System.out.println("Topic parameter is incorrect! ( > 0 and <= 10000 required.)");
        System.exit(1);
      }
      if (para[2] < MIN || para[2] > N_MAX) {
        System.out.println("Topic parameter is incorrect! ( > 0 and <= 10000 required.)");
        System.exit(1);
      }
      /*
      System.out.println(para[0]);
      System.out.println(para[1]);
      System.out.println(para[2]);
      */
      
    
    return para;
  }
  
  // calculate the distance given two points on the xy plane
  public double calDistance(double x, double y, double x1, double y1) {
    double x_dist;
    double y_dist;
    double dist;
    x_dist = y_dist = dist = 0;
    
    x_dist = Math.abs(x - x1);
    y_dist = Math.abs(y - y1);
    dist = Math.pow(x_dist, 2) + Math.pow(y_dist, 2);
    dist = Math.sqrt(dist);
    
    return dist;
  }
}