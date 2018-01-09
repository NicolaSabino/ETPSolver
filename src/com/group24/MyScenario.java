package com.group24;

import tabu_search.MyTabuSearch;

import java.util.*;

public class MyScenario implements Comparable<MyScenario>{

    private ArrayList<MyExam> scenarioExams;
    private ArrayList<MyTimeslot> scenarioTimeslots;
    private int[][] collisionMatrix;
    private int numOfExams;
    private int numOfStudents;
    private double penalty;
    private int scenarioConflicts;
    private int tMax;
    private int scenarioID;
    private ArrayList<Integer> conflictualExamsID;

    public MyScenario(int scenarioID, ArrayList<MyExam> scenarioExams, int tMax, int numOfExams, int[][] collisionMatrix, int numOfStudents){
        this.scenarioID =scenarioID;
        this.scenarioTimeslots = new ArrayList<>();
        this.scenarioExams = scenarioExams;
        this.tMax = tMax;
        this.numOfExams = numOfExams;
        for (int i = 0; i < tMax; i++) {
            MyTimeslot t = new MyTimeslot(i);
            scenarioTimeslots.add(t);
        }
        this.collisionMatrix = collisionMatrix;
        this.setPenalty(0);
        this.numOfStudents = numOfStudents;

    }

    public MyScenario(int scenarioID, int[][] collisionMatrix, int numOfExams, int  numOfStudents, int tMax, ArrayList<MyExam> scenarioExams){
        this.collisionMatrix = collisionMatrix;
        this.numOfExams = numOfExams;
        this.numOfStudents = numOfStudents;
        this.penalty = 0;
        this.scenarioConflicts = 0;
        this.tMax = tMax;
        this.scenarioTimeslots = new ArrayList<>();
        this.scenarioExams = scenarioExams;
        this.scenarioID = scenarioID;
        this.conflictualExamsID = new ArrayList<>();
        this.conflictualExamsID.clear();

        //Creo i nuovi timeslot dello scenario
        for (int i = 0; i < tMax; i++) {
            MyTimeslot t = new MyTimeslot(i);
            scenarioTimeslots.add(t);
        }


        //Popolo lo scenario


        MyTabuSearch.bootstrap(collisionMatrix);

        do {
            scenarioGeneratorGreedy();

            if (scenarioConflicts > 0) {
                MyTabuSearch.makeFeasible(this);
            }
        }while(scenarioConflicts>0);
        //todo rimuovere
        /*System.out.println();
        for (int i = 0; i < numOfExams; i++) {
            System.out.print(this.scenarioExams.get(i).getTimeslotID(scenarioID) + " ");

        }*/
        //Calcolo la penalty


        calculatePenalty();
    }



    /** GETTER */
    public int getNumOfExams() {
        return numOfExams;
    }

    public double getPenalty() {
        return penalty;
    }

    public int getScenarioConflicts() {
        return scenarioConflicts;
    }

    public int gettMax() {
        return tMax;
    }

    public ArrayList<MyTimeslot> getScenarioTimeslots() {
        return scenarioTimeslots;
    }

    public ArrayList<MyExam> getScenarioExams(){
        return scenarioExams;
    }

    public int getScenarioID() {
        return scenarioID;
    }

    /** SETTER */
    public void setPenalty(double penalty) {
        this.penalty = penalty;
    }

    public void setScenarioConflicts(int scenarioConflicts) {
        this.scenarioConflicts = scenarioConflicts;
    }

    /** METODI PER GESTIRE ARRAY DI ESAMI CONFLITTUALI */
    //todo
    public int getConflictualExamsIDSize(){
        return conflictualExamsID.size();
    }

    public MyExam getExamFromConflictuals(int i){
        return scenarioExams.get(conflictualExamsID.get(i)-1);
    }

    /** METODI PER POPOLARE E GESTIRE LO SCENARIO */
    public void scenarioGeneratorGreedy(){
        Random r = new Random();
        int timeslotID = -1;
        int choosenExamID;
        int choosenExamIDX;
        int minNumberOfPossibleTimeslots;
        int possibleTS;
        ArrayList<Integer> notAssignedExams =  new ArrayList<>();

        //all'inizio tutti gli esami possono stare in tutti i timeslot
        for (int i = 0; i < scenarioExams.size(); i++) {
            scenarioExams.get(i).clearPossibleTSList();
            for (int k = 0; k < tMax; k++) {
                scenarioExams.get(i).addPossibleTimeslot(k);
            }
        }

        //inizializzo il vettore di esami non assegnati
        for (int i = 0; i < numOfExams; i++) {
            notAssignedExams.add(i);
        }

        //scelgo l'esame da cui partire in modo casuale
        choosenExamIDX = r.nextInt(notAssignedExams.size());
        choosenExamID = notAssignedExams.get(choosenExamIDX);

        //lo tolgo dalla lista degli esami ancora da assegnare
        notAssignedExams.remove((Integer) choosenExamID);
        ArrayList<Integer> currentExamsWithLeastPossibility = new ArrayList<>();

        for (int i = 0; i < numOfExams; i++) {

            if (i != 0){
                minNumberOfPossibleTimeslots = tMax ;
                //Cerco l'esame con il minor numero di TS possibili
                currentExamsWithLeastPossibility.clear();
                for (int k = 0; k < notAssignedExams.size(); k++) {
                    possibleTS = scenarioExams.get(notAssignedExams.get(k)).getPossibleTimeslotSize();
                    if (possibleTS <  minNumberOfPossibleTimeslots){
                        minNumberOfPossibleTimeslots = possibleTS;
                        currentExamsWithLeastPossibility.clear();
                        currentExamsWithLeastPossibility.add(notAssignedExams.get(k));
                    }else if(possibleTS ==  minNumberOfPossibleTimeslots){
                        currentExamsWithLeastPossibility.add(notAssignedExams.get(k));
                    }
                }
                choosenExamIDX = r.nextInt(currentExamsWithLeastPossibility.size());
                choosenExamID = currentExamsWithLeastPossibility.get(choosenExamIDX);
                notAssignedExams.remove((Integer)choosenExamID);
            }

            //corpo del greedy
            if(scenarioExams.get(choosenExamID).getPossibleTimeslotSize() > 0){
                timeslotID = scenarioExams.get(choosenExamID).getFirstPossibleTimeslot(); //prendo ts in cui ficcarlo
                scenarioTimeslots.get(timeslotID).addExam(scenarioExams.get(choosenExamID));
                //todo rimuovere
                if (timeslotID ==-1){
                    int foo = 1;
                }
                scenarioExams.get(choosenExamID).addTimeslot(scenarioID, timeslotID);
            }else{
                timeslotID = r.nextInt(scenarioTimeslots.size());
                scenarioTimeslots.get(timeslotID).addExam(scenarioExams.get(choosenExamID));
                //todo rimuovere
                if (timeslotID ==-1){
                    int foo = 1;
                }

                scenarioExams.get(choosenExamID).addTimeslot(scenarioID, timeslotID);
                //Se entro qui vuol dire che creo uno o più conflitti quindi aggiorno array di esami conflittuali
                conflictualExamsID.add(choosenExamID+1);
                MyExam e;
                MyTimeslot to = scenarioTimeslots.get(timeslotID);
                MyExam examMoving = scenarioExams.get(choosenExamID);
                for (int j = 0; j < to.getExamListSize(); j++) {
                    e = to.getExam(j);
                    if(e.getExamID() > examMoving.getExamID() && collisionMatrix[examMoving.getExamID()-1][e.getExamID()-1] > 0){
                        scenarioConflicts++;
                        if(!conflictualExamsID.contains((Integer)e.getExamID())){
                            conflictualExamsID.add(e.getExamID());
                        }
                    }
                    if(e.getExamID() < examMoving.getExamID() && collisionMatrix[e.getExamID()-1][examMoving.getExamID()-1] > 0){
                        scenarioConflicts++;
                        if(!conflictualExamsID.contains((Integer)e.getExamID())){
                            conflictualExamsID.add(e.getExamID());
                        }
                    }
                }
            }

            //dopo aver deciso in che timeslot metterlo aggiorno i timeslot disponibili per ogni esame
            MyExam e;
            for (int k = 0; k < choosenExamID ; k++) {
                e = scenarioExams.get(k);
                //se non posso stare in un timeslot -> lo rimuovo dalla mia lista
                if (collisionMatrix[k][choosenExamID]>0 && e.checkIfContained(timeslotID)){
                    e.removePossibleTimeslot(timeslotID);
                }
            }
            for (int k = choosenExamID + 1; k < numOfExams ; k++) {
                e = scenarioExams.get(k);
                //se non posso stare in un timeslot -> lo rimuovo dalla mia lista
                if (collisionMatrix[choosenExamID][k]>0 && e.checkIfContained(timeslotID)){
                    e.removePossibleTimeslot(timeslotID);
                }
            }
        }

    }

    public boolean isFeasible(){
        if(scenarioConflicts > 0){
            return false;
        }else{
            return true;
        }
    }

    public void updateScenario(MyExam examMoving, MyTimeslot to, int initConflicts, int endConflicts){
        MyExam e;
        MyExam e1;
        boolean found = false;

        MyTimeslot from = scenarioTimeslots.get(examMoving.getTimeslotID(scenarioID));
        //controllo endConflict per sapere se l'esame va tolto o meno dal vettore dei conflittuali
        if(endConflicts == 0){
            conflictualExamsID.remove((Integer)examMoving.getExamID());
        }
        //sommo il numero di conflitti
        scenarioConflicts = scenarioConflicts - initConflicts + endConflicts;
        //sposto l'esame e aggiorno il suo timeslot
        from.removeExam(examMoving);
        to.addExam(examMoving);
        //Controllo in caso di conflitti se ci sono nuovi esami da aggiungere alla lista dei conflittuali
        //TO
        for (int i = 0; i < to.getExamListSize(); i++) {
            e = to.getExam(i);
            if(e.getExamID() > examMoving.getExamID() && collisionMatrix[examMoving.getExamID()-1][e.getExamID()-1] > 0){
                if(!conflictualExamsID.contains((Integer)e.getExamID())){
                    conflictualExamsID.add(e.getExamID());
                }
            }
            if(e.getExamID() < examMoving.getExamID() && collisionMatrix[e.getExamID()-1][examMoving.getExamID()-1] > 0){
                if(!conflictualExamsID.contains((Integer)e.getExamID())){
                    conflictualExamsID.add(e.getExamID());
                }
            }
        }
        //FROM
        for (int j = 0; j < conflictualExamsID.size(); j++) {
            e1 = scenarioExams.get(conflictualExamsID.get(j)-1);
            if(e1.getTimeslotID(scenarioID) == from.getTimesotID()){
                found = false;
                for(int i = 0; i < from.getExamListSize(); i++){
                    e = from.getExam(i);
                    if(e.getExamID() > e1.getExamID() && collisionMatrix[e1.getExamID()-1][e.getExamID()-1] > 0){
                        found = true;
                    }
                    if(e.getExamID() < e1.getExamID() && collisionMatrix[e.getExamID()-1][e1.getExamID()-1] > 0){
                        found = true;
                    }
                }
                if(!found){
                    conflictualExamsID.remove((Integer)e1.getExamID());
                }
            }

        }


        //Aggiorno timeslot esame
        examMoving.addTimeslot(scenarioID, to.getTimesotID());

    }

    /** METODI SUPPORTO TABUSEARCH*/
    public int conflictPerMove(MyTimeslot t, MyExam eMov){
        int conflicts = 0;
        MyExam e;

        for (int i = 0; i < t.getExamListSize(); i++) {
            e = t.getExam(i);
            if(e.getExamID() > eMov.getExamID() && collisionMatrix[eMov.getExamID()-1][e.getExamID()-1] > 0){
                conflicts++;
            }
            if(e.getExamID() < eMov.getExamID() && collisionMatrix[e.getExamID()-1][eMov.getExamID()-1] > 0){
                conflicts++;
            }
        }

        return conflicts;
    }

    /** METODI PER GESTIRE LA PENALTY */
    public void calculatePenalty(){

        double _penalty = 0;


        for (int i = 0; i < collisionMatrix.length; i++) {
            for (int j = i+1; j < collisionMatrix.length ; j++) {
                if (collisionMatrix[i][j] > 0){
                    int id_timeslot1 = scenarioExams.get(i).getTimeslotID(scenarioID);
                    int id_timeslot2 = scenarioExams.get(j).getTimeslotID(scenarioID);
                    int distance = Math.abs(id_timeslot1-id_timeslot2);

                    if(distance <= 5){
                        int exponential = 5 - distance;
                        _penalty        += (collisionMatrix[i][j]*Math.pow(2,exponential));
                    }
                }
            }
        }

        this.penalty = _penalty/numOfStudents;
    }

    /** METODI SUPPORTO GENETICO */
    public void swapTimeslots(int t1, int t2){

        Collections.swap(scenarioTimeslots, t1, t2);
        scenarioTimeslots.get(t1).setTimeslotID(t1);
        scenarioTimeslots.get(t2).setTimeslotID(t2);
    }

    public boolean MoveIsFeasible(MyExam e, MyTimeslot t){

        int  comparisonIDX;
        for (int i = 0; i < t.timeslotExams.size(); i++) {
            comparisonIDX = t.timeslotExams.get(i).getExamID()-1;
            if (e.getExamID()-1 > comparisonIDX && collisionMatrix[comparisonIDX][e.getExamID()-1]>0){
                return false;
            }
            if (e.getExamID()-1 < comparisonIDX && collisionMatrix[e.getExamID()-1][comparisonIDX]>0){
                return false;
            }
        }
        return true;
    }

    public double PenaltyPerMove(MyExam e,MyTimeslot t){
        int inizio;
        int fine;
        double _penalty = 0;
        MyTimeslot nearTimeslot;
        MyExam nearExam;
        int distance;
        double penalty;

        inizio = Math.max(0, t.getTimesotID()-5);
        fine   = Math.min(t.getTimesotID()+5, tMax-1);


        for (int i = inizio; i <= fine; i++) {
            if (i!= t.getTimesotID()) {
                nearTimeslot = scenarioTimeslots.get(i);
                distance = Math.abs(t.getTimesotID() - i);
                for (int j = 0; j < nearTimeslot.getExamListSize(); j++) {
                    nearExam = nearTimeslot.getExam(j);
                    if (nearExam.getExamID()-1 > e.getExamID()- 1 && collisionMatrix[e.getExamID() - 1][nearExam.getExamID()-1 ]>0) {
                        int exponential = 5 - distance;
                        _penalty += (collisionMatrix[e.getExamID() - 1][nearExam.getExamID()-1 ] * Math.pow(2, exponential));
                    }
                    if (nearExam.getExamID()-1 < e.getExamID()- 1 && collisionMatrix[nearExam.getExamID()-1 ][e.getExamID() - 1]>0) {
                        int exponential = 5 - distance;
                        _penalty += (collisionMatrix[nearExam.getExamID()-1 ][e.getExamID() - 1] * Math.pow(2, exponential));
                    }
                }
            }
        }
        penalty = _penalty/numOfStudents;

        return penalty;
    }

    /** METODI COMPARE */
    public int compareTo(MyScenario scenario) {

        double comparePenalty = ((MyScenario) scenario).penalty;

        //ascending
        double result = this.penalty - comparePenalty;

        if(result < 0){
            return -1;
        }else if(result == 0){
            return 0;
        }else{
            return 1;
        }

    }

    public static Comparator<MyScenario> MyScenarioComparator = new Comparator<MyScenario>() {

        public int compare(MyScenario scen1, MyScenario scen2) {
            //ascending order
            return scen1.compareTo(scen2);
        }

    };



}
