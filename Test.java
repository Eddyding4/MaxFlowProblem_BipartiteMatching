import java.util.*;
import java.io.File;  // Import the File class
import java.io.FileNotFoundException;  // Import this class to handle errors
import java.util.Scanner;

public class Test {

    private double score1, score2;
    private ArrayList<Integer> solutions1, solutions2;

    public Test() {
        score1 = 0;
        score2 = 0;
        solutions1 = new ArrayList<>();
        solutions2 = new ArrayList<>();
    }

    private void loadSolutions() {

        try{
            File myObj = new File("C:\\Users\\Eddy\\IdeaProjects\\CS 251 Project 3\\bipartite_matching_testcases\\results.txt");
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                solutions1.add(Integer.parseInt(data));
            }
        }  catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        try{
            File myObj = new File("C:\\Users\\Eddy\\IdeaProjects\\CS 251 Project 3\\max_flow_testcases\\results.txt");
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                solutions2.add(Integer.parseInt(data));
            }
        }  catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

    }

    private boolean testBipartiteMatching(String file_name, int result){
        try {
            BipartiteMatching model = null;
            File myObj = new File(file_name);
            Scanner myReader = new Scanner(myObj);
            int line = 0;
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                if(line == 0)
                {
                    String []str = data.split(" ");
                    int M = Integer.parseInt(str[0]);
                    int N = Integer.parseInt(str[1]);
                    model = new BipartiteMatching(M, N);
                }
                else
                {
                    String []str = data.split(" ");
                    int [] input = new int[str.length];
                    for (int i=0; i<str.length; i++)
                        input[i] = Integer.parseInt(str[i]);

                    model.insList(line-1, input);
                }
                line += 1;
            }
            myReader.close();
            int out = model.bipartiteMatching();
            System.out.println("mines: " + out);
            System.out.println("theirs: " + result);
            return (out==result);


        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");

            e.printStackTrace();
            return false;
        }
    }

    private boolean testMaxFlow(String file_name, int result){
        try {
            MaxFlow model = null;
            File myObj = new File(file_name);
            Scanner myReader = new Scanner(myObj);
            String data;
            int line=0;
            int n;

            while (myReader.hasNextLine()) {
                data = myReader.nextLine();
                //System.out.println(data);
                if (line == 0) {
                    n = Integer.parseInt(data);
                    //System.out.println(tot);
                    model = new MaxFlow(n);
                } else {

                    String[] comp = data.split(" ");
                    int s = Integer.parseInt(comp[0]);
                    int d = Integer.parseInt(comp[1]);
                    int f = Integer.parseInt(comp[2]);

                    model.insEdge(s, d, f);
                }
                line += 1;
            }

            myReader.close();

            int out = model.pathAugmentation();
            System.out.println("mines: " + out);
            System.out.println("theirs: " + result);

            return out==result;

        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
            return false;
        }
    }
    public static void main(String[] args) {


        Test test = new Test();
        test.loadSolutions();

        System.out.println("Testing Bipartite Matching");

        for (int i = 1; i <= 10; i++) {
            try{
                if(test.testBipartiteMatching("C:\\Users\\Eddy\\IdeaProjects\\CS 251 Project 3\\bipartite_matching_testcases\\"+i+".txt", test.solutions1.get(i-1))){
                    test.score1 += 5;
                    System.out.println("Test "+i+" passed");
                }
                else{
                    System.out.println("Test "+i+" failed");
                }
            } catch (Exception e){
                System.out.println("Test "+ i+" : An error occurred.");
                e.printStackTrace();
            }
        }

        System.out.println("Testing Max Flow");

        for (int i = 1; i <= 10; i++) {
            try {
                if (test.testMaxFlow("C:\\Users\\Eddy\\IdeaProjects\\CS 251 Project 3\\max_flow_testcases\\" + i + ".txt", test.solutions2.get(i - 1))) {
                    test.score2 += 5;
                    System.out.println("Test " + i + " passed");
                } else {
                    System.out.println("Test " + i + " failed");
                }
            } catch (Exception e){
                System.out.println("Test "+ i+" : An error occurred.");
                e.printStackTrace();
            }
        }

        System.out.println("Score of Bipartite Matching : "+test.score1+" out of 50");
        System.out.println("Score of Max Flow : "+test.score2+" out of 50");
    }
}

