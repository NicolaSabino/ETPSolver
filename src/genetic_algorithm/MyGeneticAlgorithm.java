package genetic_algorithm;

import Interface.ProgressBarRotating;
import com.group24.MyExam;
import com.group24.MyScenario;
import com.group24.MyTimeslot;

import java.util.ArrayList;
import java.util.Random;

public class MyGeneticAlgorithm {

    private boolean iterate;
    private int[][] collisionMatrix;
    private int numberOfExams;
    private int numberOfStudents;
    private int numberOftimeSlots;
    private ArrayList<MyExam> generationExams;
    private int sizeGeneration;
    private int seconds;
    MyGeneration generation;

    public MyGeneticAlgorithm(int sizeGeneration, int[][] collisionMatrix, int numberOfExams, int numberOfStudents, int numberOftimeSlots, ArrayList<MyExam> generationExams, int seconds){
        this.iterate = true;
        this.sizeGeneration = sizeGeneration;
        this.collisionMatrix = collisionMatrix;
        this.numberOfExams = numberOfExams;
        this.numberOfStudents = numberOfStudents;
        this.numberOftimeSlots = numberOftimeSlots;
        this.generationExams = generationExams;
        this.seconds = seconds;

        //generation = new MyGeneration(sizeGeneration, collisionMatrix, numberOfExams, numberOfStudents, numberOftimeSlots, generationExams);
        this.mainLoop();

    }

    /** METODO PER PRENDERE SOLUZIONE FINALE DAL MAIN*/
    public MyScenario getBestScenario(){
        return generation.getListOfScenarios().get(0);
    }

    /** METODI PER TIMER */
    private void mainLoop(){
        int i = 0;
        int choice;
        Random r = new Random();
        ProgressBarRotating progressBarRotating = new ProgressBarRotating(seconds);
        progressBarRotating.start();


        long end = System.currentTimeMillis() + (seconds*1000);

        generation = new MyGeneration(sizeGeneration, collisionMatrix, numberOfExams, numberOfStudents, numberOftimeSlots, generationExams);


        while((end - System.currentTimeMillis()) > 0){
            /*CORPO GENETICO*/

            //faccio cose genetiche
            while(iterate && i < Math.round(sizeGeneration/2)){

                choice = r.nextInt(9);
                if(choice < 3){
                    generation.permTimeslot(i);

                }else if(choice < 6){
                    generation.mutation(i);
                    //LocalSearch(sizeGeneration-1-i);
                }else{
                    generation.tryToEmptyTimeslot(i);
                    //LocalSearch(sizeGeneration-1-i);
                }


                i++;
            }
            generation.orderByPenalty();

            i=0;

        }
        progressBarRotating.showProgress = false;

    }

    public void interrupt(){
        iterate = false;
    }

}
