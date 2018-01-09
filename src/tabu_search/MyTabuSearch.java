package tabu_search;

import com.group24.*;

import java.util.Random;

public final class MyTabuSearch {

    private static TabuList tabuList;
    private static int[][]  collisionMatrix;
    private static int      counter;
    private static int      maxNumberOfIteration;
    private static int      neighborhoodSize;
    private static MyExam   examMoving;
    private static int      TabuListDimension;

    /** SETTER */
    public static void setMaxNumberOfIteration(int maxNumIter){
        maxNumberOfIteration = maxNumIter;
    }

    public static void setNeighborhoodSize(int NeigSize){
        neighborhoodSize = NeigSize;
    }

    public static void setTabuListDimension(int TLDim){
        TabuListDimension = TLDim;
    }

    /**METODI GESTIONE TABUSEARCH*/
    public static void bootstrap(int[][] matrix){
        tabuList = new TabuList(TabuListDimension);//(matrix.length/2);
        collisionMatrix = matrix;
    }

    public static MyScenario makeFeasible(MyScenario s){

        /*SCEGLIE LA MOSSA MIGLIORE DA FARE E LA PASSA AD UPDATE SCENARIO*/
        counter = 0;

        while(!s.isFeasible() && counter < maxNumberOfIteration){
            MyTimeslot bestTimeslot = new MyTimeslot(-1);
            MyTimeslot timeslotTo;
            MyTimeslot timeslotFrom;
            MyExam     examToMove;
            Random     r = new Random();
            int        iteration = 0;
            double     bestImprovement = -1000000; //inizializzo il miglioramento "migliore" alla penalty dello scenario
            int        numberConflictualExams = s.getConflictualExamsIDSize();
            int        idx;
            int initialConflicts = 0;
            int conflictsPerMove = 0;
            int improvement = 0;
            int bestInitConf = 0;
            int bestEndConf = 0;

            /*METODO RANDOM*/
            for (iteration = 0; iteration < neighborhoodSize; iteration++) {
                /*PRENDO ESAME SICURAMENTE CONFLITTUALE*/
                idx = r.nextInt(numberConflictualExams);
                examToMove = s.getExamFromConflictuals(idx);
                /*SELEZIONO TIMESLOT RANDOM DIVERSO DAL MIO*/
                do{
                    int toID = r.nextInt(s.getScenarioTimeslots().size());
                    timeslotTo = s.getScenarioTimeslots().get(toID);
                }while(timeslotTo.getTimesotID() == examToMove.getTimeslotID(s.getScenarioID()));

                timeslotFrom = s.getScenarioTimeslots().get(examToMove.getTimeslotID(s.getScenarioID()));

                /*VALUTO SE IL VICINO SIA BUONO OPPURE NO*/
                initialConflicts = s.conflictPerMove(timeslotFrom, examToMove);
                conflictsPerMove = s.conflictPerMove(timeslotTo, examToMove);
                improvement = initialConflicts - conflictsPerMove; // (imp<0) => no migliorameto; (imp>0) => miglioramento

                if (improvement > bestImprovement && (!tabuList.istabu(examToMove, timeslotFrom, timeslotTo))) {
                    examMoving = examToMove;
                    bestTimeslot = timeslotTo;
                    bestImprovement = improvement;
                    bestEndConf = conflictsPerMove;
                    bestInitConf = initialConflicts;
                }
            }

            s.updateScenario(examMoving, bestTimeslot, bestInitConf, bestEndConf);
            tabuList.addToTabu(examMoving,s.getScenarioTimeslots().get(examMoving.getTimeslotID(s.getScenarioID())), bestTimeslot);
            counter++;
        }



        return s;
    }



}
