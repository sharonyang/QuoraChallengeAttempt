import java.util.*;
import java.io.*;

// Sharon Yang on Mar 8, 2014
// This program reads from an input file to output
// the desired result shown on Quora website here:
// http://www.quora.com/challenges (Challenge 1)
// to generate 'Nearby' topics and questions based
// on input queries.

// Input: a text file containing topics, questions,
// and queries.
// Output: a printout to the terminal windows showing
// the result.

public class Solution{
  // here is a series of constraints from the Quora site:
  public static final int T_MAX = 10000;
  public static final int Q_MAX = 1000;
  public static final int N_MAX = 10000;
  public static final int MIN = 1;
  public static Scanner read = new Scanner(System.in);
  
  public static void main (String [] args) {
    Solution run = new Solution();
    BinaryTree tTree, qTree;
    String file_string = "";
    
    file_string = read.nextLine();
    int[] para = new int[3]; // this is where the parameters are stored
    
    para = run.findParameters(file_string); // this is the input file for parameters
    
    int t_lines = para[0];
    int q_lines = para[1];
    int n_lines = para[2];
    float[][] n_entries = new float[4][n_lines];
    System.out.println();
    //System.out.println(para[0] + " " + para[1] + " " + para[2] + " ");
    
    //run.tTree(t_lines);
    //run.qTree(q_lines);
    //float[][] n_entries = new float[4][n_lines];
    
    tTree = run.readT(t_lines);
    qTree = run.readQ(q_lines);
    n_entries = run.readN(n_lines);
    // for each query find distances and print out result
    //run.matchCal(n_entries, q_entries, t_entries);
    
    run.calN(n_entries, n_lines, tTree, qTree);
  }

  // create a 2-D array storing the topic data.
  // start readT
  public BinaryTree readT(int t_lines) {
    BinaryTree tTree = new BinaryTree();
    int count = 0;
    int id;
    float x, y;
    while(true) {
        String temp = read.nextLine();
        id = -1;
        x = y = 0;
        if (count >= 0) {
          int curr = 0;
          int space = 0;
          for (int j = 0; j < temp.length(); j++) {
            if (j == temp.length() - 1) {
              y = Float.parseFloat(temp.substring(curr + 1));
            }
            else if (temp.charAt(j) == ' ' && space == 0) {
              space = 1;
              curr = j;
              id = Integer.parseInt(temp.substring(0, curr));
            }
            else if (temp.charAt(j) == ' '){
              x = Float.parseFloat(temp.substring(curr + 1, j));
              space++;
              curr = j;
            }
          }
        if (id != -1) 
          tTree.addNode(x, y, id);
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
        
    return tTree;
  }
  // end readT
  
  public BinaryTree readQ(int q_lines) {
    BinaryTree qTree = new BinaryTree();
    int count = 0;
    int id;
    float x, y;
      while(read.hasNext()) {
        String temp = read.nextLine();
        id = -1;
        x = y = 0;
        if (count >= 0) {
          if (temp.charAt(2) == '0') {
            id = -1;
          }
          else {
            int curr = 0;
            int space = 0;
            for (int j = 0; j < temp.length(); j++) {
              if (j == temp.length() - 1) {
                if (space == 2) {
                  break;
                }
                else if (space == 4) {
                  x = y;
                  y = Float.parseFloat(temp.substring(curr + 1));
                }
                else if (space == 3) {
                  y = Float.parseFloat(temp.substring(curr + 1));
                }
              }
              else if (temp.charAt(j) == ' ' && space == 0) {
                space = 1;
                curr = j;
                id = Integer.parseInt(temp.substring(0, curr));
              }
              else if (temp.charAt(j) == ' ' && space == 1){
                space++;
                curr = j;
              }
              else if (temp.charAt(j) == ' ' && space == 2){
                x = Float.parseFloat(temp.substring(curr + 1, j));
                space++;
                curr = j;
              }
              else if (temp.charAt(j) == ' ' && space == 3){
                y = Float.parseFloat(temp.substring(curr + 1, j));
                space++;
                curr = j;
              }
            }
          }
          if (id != -1)
            qTree.addNode(x, y, id);
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
    return qTree;
  }
  // end readQ
  
  // create a 2-D array storing the query data
  // start readN
  public float[][] readN(int n_lines) {
    float[][] data = new float[4][n_lines];
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
              data[space][count] = Float.parseFloat(temp.substring(curr + 1));
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
              data[space][count] = Float.parseFloat(temp.substring(curr + 1, j));
              space++;
              curr = j;
            }
            
          }
          //System.out.println("id: " +data[0][count] +"max: " +data[1][count] + " x: " +data[2][count] +" y: " + data[3][count]);
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
  
  public void calN(float[][] entries, int n_lines, BinaryTree tTree, BinaryTree qTree) {
    int count = 0;
    Node newNode = new Node();
    while (count != n_lines) {
      int times = (int)entries[1][count];
      float[][] store = new float[times][3];
      int curr = 0;
      if ((int)entries[0][count] == 0) {
        while( curr < times ) {
          float[] stat = new float[3];
          stat = tTree.findClosest(entries[2][count], entries[3][count]);
          store[curr][0] = stat[0];
          store[curr][1] = stat[1];
          store[curr][2] = stat[2];
          System.out.print((int)store[curr][0] + " ");
          curr = times; // this is to be changed when knn implemented
        }
      }
      else {
        while( curr < times ) {
          float[] stat = new float[3];
          stat = qTree.findClosest(entries[2][count], entries[3][count]);
          store[curr][0] = stat[0];
          store[curr][1] = stat[1];
          store[curr][2] = stat[2];
          System.out.print((int)store[curr][0] + " ");
          curr = times; // this is to be changed when knn implemented
        }
      }
      count++;
      System.out.println();
    }
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
}

class Node {
  float x;
  float y;
  int depth;
  int id;
  Node parent;
  Node left;
  Node right;
  
  Node() {
    this.x = 0;
    this.y = 0;
    this.depth = 1;
    this.parent = null;
    this.left = null;
    this.right = null;
    this.id = 0;
  }
  
  Node(float x, float y, int name) {
    this.x = x;
    this.y = y;
    this.depth = 1;
    this.id = name;
    this.parent = null;
    this.left = null;
    this.right = null;
  }
}

class BinaryTree {
  Node head;
  
  BinaryTree() {
    this.head = null;
  }
  
  public void addNode(float x, float y, int id) {
    Node newNode;
    newNode = new Node(x, y, id); // now you have the node
    //System.out.print("\nid: " + newNode.id + " x: " + newNode.x + " y: " + newNode.y);
    if (head == null) {
      head = newNode;
    }
    else {
      add(newNode, head);
      return;
    }
  }
  
  public void add(Node newNode, Node curr) {
    newNode.depth++;
    if (compareNode(newNode, curr) == 2 && curr.right == null) {
      curr.right = newNode;
      newNode.parent = curr.right;
      return;
    }
    else if (compareNode(newNode, curr) == 1 && curr.left == null) {
      curr.left = newNode;
      newNode.parent = curr;
      return;
    }
    else if (compareNode(newNode, curr) == 1 && curr.left != null) {
      add(newNode, curr.left);
    }
    else if (compareNode(newNode, curr) == 2 && curr.right != null) {
      add(newNode, curr.right);
    }
    
    return;
  }
  
  public int compareNode(Node curr, Node parent) {
    if (curr.depth % 2 == 0 && Math.abs(curr.x - parent.x) < 0.001) {
      if (curr.id > parent.id) {
        return 1;
      }
      else {
        return 2;
      }
    }
    else if (curr.depth % 2 != 0 && Math.abs(curr.y - parent.y) < 0.001) {
      if (curr.id > parent.id) {
        return 1;
      }
      else {
        return 2;
      }
    } 
    else if (curr.depth % 2 == 0) {
      if (curr.x > parent.x) {
        return 2;
      }
      else {
        return 1;
      }
    }
    else {
      if (curr.y > parent.y) {
        return 2;
      }
      else {
        return 1;
      }
    }
  }
  
  public float[] findClosest(float x, float y) {
    if (head == null) {
      return null;
    }

    else {
      float[] stat = new float[3];
      Node temp = find(head, x, y);
      stat[0] = temp.id;
      stat[1] = temp.x;
      stat[2] = temp.y;
      return stat;
    }
  }
  
  public Node find(Node curr_node, float x, float y) {
    if (curr_node.left == null && curr_node.right == null) {
      return curr_node;
    }
    
    else if (curr_node.left == null && curr_node.right != null) {
      if (Math.abs(minDistance(x, y, curr_node) - minDistance(x, y, curr_node.right)) < 0.001) {
        return curr_node;
      }
      else if (minDistance(x, y, curr_node) < minDistance(x, y, curr_node.right)) {
        return curr_node;
      }
      else {
        return find(curr_node.right, x, y);
      }
    }
    
    else if (curr_node.right == null && curr_node.left != null) {
      if (Math.abs(minDistance(x, y, curr_node) - minDistance(x, y, curr_node.left)) < 0.001) {
        return find(curr_node.left, x, y);
      }
      else if (minDistance(x, y, curr_node) < minDistance(x, y, curr_node.left)) {
        return curr_node;
      }
      else {
        return find(curr_node.left, x, y);
      }
    }
    
    else {
      if (Math.abs(minDistance(x, y, curr_node) - minDistance(x, y, curr_node.left)) < 0.001) {
        return find(curr_node.left, x, y);
      }
      else if (Math.abs(minDistance(x, y, curr_node) - minDistance(x, y, curr_node.right)) < 0.001) {
        return curr_node;
      }
      else if (minDistance(x, y, curr_node) > minDistance(x, y, curr_node.right) && minDistance(x, y, curr_node) > minDistance(x, y, curr_node.left) && Math.abs(minDistance(x, y, curr_node.left) - minDistance(x, y, curr_node.right)) < 0.001) {
        return find(curr_node.left, x, y);
      }
      else if (minDistance(x, y, curr_node) < minDistance(x, y, curr_node.right) && minDistance(x, y, curr_node) < minDistance(x, y, curr_node.left)) {
        return curr_node;
      }
      else if (minDistance(x, y, curr_node) > minDistance(x, y, curr_node.right) && minDistance(x, y, curr_node) < minDistance(x, y, curr_node.left)) {
        return find(curr_node.right, x, y);
      }
      else if (minDistance(x, y, curr_node) < minDistance(x, y, curr_node.right) && minDistance(x, y, curr_node) > minDistance(x, y, curr_node.left)) {
        return find(curr_node.left, x, y);
      }
      else {
        if (minDistance(x, y, curr_node.left) < minDistance(x, y, curr_node.right))
          return find(curr_node.left, x, y);
        else {
          return find(curr_node.right, x, y);
        }
      }
    }
  }
  /*
  public void printNeighbors(int dist, int times, Node curr) {
    if (curr == null || times == 0) {
      return;
    }
    System.out.print(curr.id + " ");
    if (curr.left == null && curr.right == null) {
      dist += minDistance(x, y, curr.parent);
      printNeighbors(dist, times - 1, curr.parent);
    }
    else if (dist - minDistance(x, y, curr_node.parent) && minDistance(x, y, curr_node.parent) < minDistance(x, y, curr_node.right)) {
      
    }
    else if (minDistance(x, y, curr_node.parent) < minDistance(x, y, curr_node.left) && minDistance(x, y, curr_node.parent) < minDistance(x, y, curr_node.right)) {
      dist += minDistance(x, y, curr.parent);
      printNeighbors(dist, times - 1, curr.parent);
    }
    else if (minDistance(x, y, curr_node.parent) < minDistance(x, y, curr_node.left) && minDistance(x, y, curr_node.parent) < minDistance(x, y, curr_node.right)) {
      
    }
  }
  */
  public float minDistance(float x, float y, Node curr) {
    float x_dist;
    float y_dist;
    float dist;
    x_dist = y_dist = dist = 0;
    
    float x1 = curr.x;
    float y1 = curr.y;
    
    x_dist = Math.abs(x - x1);
    y_dist = Math.abs(y - y1);
    dist = x_dist * x_dist + y_dist * y_dist;
    dist = (float)Math.sqrt(dist);
    
    return dist;
  }
    

}