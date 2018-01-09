package Interface;

import com.group24.MyExam;
import com.group24.MyScenario;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Scanner;

import static java.awt.SystemColor.text;

public final class FileManager {

    /**
     * Method that read an istance file and populate the collision matrix
     * @param matrix
     * @param path
     * @return
     */
    public static int[][] makeCollisions(int[][] matrix,String path, ArrayList<MyExam> exams)throws FileNotFoundException{

        File file = new File(path + ".stu");
        ArrayList<Integer> list = new ArrayList<Integer>();

        Scanner scanner = new Scanner(file);
        String preiousStudentID = "prev";

        while (scanner.hasNext()) {

            String      currentStudentID   = scanner.next();
            int         currentExamID      = scanner.nextInt();

            if(currentStudentID.equals(preiousStudentID)){
                list.add(currentExamID);
            }else{
                matrix = updateColisionMatrix(matrix, list, exams);
                list.clear();
                list.add(currentExamID);
                preiousStudentID = currentStudentID; //update the previpous String for the next iteration
            }
        }
        //make the last collision ( we do not have a next BUT  the list is still populated)
        matrix = updateColisionMatrix(matrix, list, exams);
        scanner.close();


        return matrix;
    }

    /**
     * increment the collision value of each couple of exams passed using the list variable
     * @param matrix
     * @param list
     * @return
     */
    private static int[][] updateColisionMatrix(int[][] matrix,ArrayList<Integer> list, ArrayList<MyExam> exams){

        //  if we are at the first item list.size() is equal to 0 and we do not perform
        // any instruction
        if(list.size() > 0) {
            for (int i = 0; i < list.size(); i++)
                for (int j = i + 1; j < list.size(); j++) {

                    // !WARNING! we cannot use the examID as an index because
                    // exams are enumerate from 1 to n,
                    // collision matrix goes from 0 to n-1

                    int i_index = list.get(i)-1;
                    int j_index = list.get(j)-1;
//                    if (matrix[i_index][j_index] == 0){
//                        exams.get(i_index).increasePossibleConflicts();
//                        exams.get(j_index).increasePossibleConflicts();
//                    }
                    matrix[i_index][j_index]++;

                }
        }
        return matrix;
    }


    /**
     * It calculate the number of exams for a given instace
     * @param path
     * @return
     */
    public static int getExamsNumber(String path) throws FileNotFoundException{

        File file = new File(path + ".exm");
        int lineCounter = 0;


        Scanner scanner = new Scanner(file);
        while (scanner.hasNextInt()) {
            lineCounter++;

            //i discard two items
            int foo = scanner.nextInt();
            foo = scanner.nextInt();
        }
        scanner.close();



        return lineCounter;

    }

    public static int getNumberOfStudents(String pathFile) throws FileNotFoundException {
        File file = new File(pathFile + ".stu");
        int studentsCounter = 0;


        Scanner scanner = new Scanner(file);
        String preious = "prev";
        while (scanner.hasNext()) {

            String      studentID   = scanner.next();
            int         foo      = scanner.nextInt();

            if(studentID.equals(preious)){
                //do nothing
            }else{
                studentsCounter++;
                preious = studentID; //update the previpous String for the next iteration
            }
        }
        scanner.close();

        return studentsCounter;
    }

    public static ArrayList<MyExam> getExams(String path, int numOfScenarios) throws FileNotFoundException{

        File file = new File(path + ".exm");
        ArrayList<MyExam> list = new ArrayList<>();

        Scanner scanner = new Scanner(file);
        while (scanner.hasNextInt()) {

            int examID  = scanner.nextInt();
            int foo     = scanner.nextInt();

            MyExam exam = new MyExam(examID, numOfScenarios);
            list.add(exam);

        }
        scanner.close();

        return list;

    }

    public static int loadSlots(String pathFile)throws FileNotFoundException{
        File file = new File(pathFile + ".slo");
        int tMax                      = 0;

        Scanner scanner = new Scanner(file);
        while (scanner.hasNext()) {
            tMax  = scanner.nextInt();
        }
        scanner.close();

        return tMax;
    }

    public static void solWriter(MyScenario best,String path){
        BufferedWriter writer = null;
        String instancename = (path.length() > 10)? path.substring(path.length() - 10) : path;
        String newPath = (path.length() > 10)? path.substring(0,path.length()-10) : "";
        String fullpath = newPath + instancename;

        try {
            //create a temporary file
            File solFile = new File( fullpath + "_OMAAL_group24.sol");

            // This will output the full path where the file will be written to...
            System.out.println("\nSolution file is located in " + solFile.getCanonicalPath() + "\n");

            writer = new BufferedWriter(new FileWriter(solFile));
            StringBuilder solution = new StringBuilder();

            for (int i = 0; i < best.getScenarioExams().size(); i++) {
                solution.append((Integer) best.getScenarioExams().get(i).getExamID());
                solution.append("\t");
                solution.append((Integer) best.getScenarioExams().get(i).getTimeslotID(best.getScenarioID()) + 1);
                solution.append("\n");
                //System.out.print(best.getScenarioExams().get(i).getTimeslotID(best.getScenarioID()) + " ");

            }
            writer.write(solution.toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                // Close the writer regardless of what happens...
                writer.close();
            } catch (Exception e) {
            }
        }
    }


}