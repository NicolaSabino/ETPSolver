package com.group24;

import java.util.ArrayList;

public class MyTimeslot {

    private int timesotID;
    public ArrayList<MyExam> timeslotExams;

    public MyTimeslot(int timesotID){
        this.timesotID = timesotID;
        this.timeslotExams = new ArrayList<>();
        clearExamList();
    }

    /** GETTER */
    public int getTimesotID() {
        return timesotID;
    }

    public void setTimeslotID(int id){
        this.timesotID = id;
    }

    /** METODI GESTIONE LISTA TIMESLOTS */
    public void clearExamList(){
        this.timeslotExams.clear();
    } //svuota la lista di timeslot

    public void addExam(MyExam e){
        timeslotExams.add(e);
    }

    public void removeExam(MyExam e){
        timeslotExams.remove(e);
    }

    public MyExam getExam(int i){
        return timeslotExams.get(i);
    }

    public int getExamListSize(){
        return timeslotExams.size();
    }

    /** METODI OVERRIDE */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MyTimeslot timeSlot = (MyTimeslot) o;

        return timesotID == timeSlot.getTimesotID();
    }

}
