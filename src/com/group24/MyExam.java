package com.group24;

import java.util.ArrayList;
import java.util.Arrays;

public class MyExam {

    private int examID;
    private ArrayList<Integer> possibleTimeslots;
    private int[] timeslotInScenario; //scenario è l'indice e il contenuto è il timeslot

    /** COSTRUTTORE */
    public MyExam(int examID, int numOfScenarios){
        this.examID = examID;
        this.timeslotInScenario = new int[numOfScenarios];
        Arrays.fill(timeslotInScenario, -1); //inizializzo a timeslot nulli
        this.possibleTimeslots = new ArrayList<>();
        possibleTimeslots.clear();
    }

    /** GETTER */
    public int getExamID() {
        return examID;
    }

    /** METODI GESTIONE ARRAY TIMESLOTS */
    public void addTimeslot(int sID, int tsID){
        timeslotInScenario[sID] = tsID;
    }

    public int getTimeslotID(int sID){
        return timeslotInScenario[sID];
    }

    /** METODI GESTIONE LISTA TIMESLOTS POSSIBILI */
    public void clearPossibleTSList(){
        this.possibleTimeslots.clear();
    } //svuota la lista di timeslot

    public void addPossibleTimeslot(Integer tsID){
        possibleTimeslots.add(tsID);
    }

    public void removePossibleTimeslot(int tsID){
        possibleTimeslots.remove((Integer) tsID);
    }

    public int getPossibleTimeslotSize(){
        return possibleTimeslots.size();
    }

    public int getFirstPossibleTimeslot(){
        return possibleTimeslots.get(0);
    }

    public boolean checkIfContained(Integer tsID){
        return possibleTimeslots.contains(tsID);
    }

    /** METODI OVERRIDE */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MyExam exam = (MyExam) o;

        return examID == exam.getExamID();
    }

}
