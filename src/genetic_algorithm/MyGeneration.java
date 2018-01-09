package genetic_algorithm;

import com.group24.MyExam;
import com.group24.MyScenario;
import com.group24.MyTimeslot;
import sun.font.FontRunIterator;
import sun.text.resources.cldr.ia.FormatData_ia;
import tabu_search.MyTabuSearch;

import java.util.*;

public class MyGeneration {

    private int[][] collisionMatrix;
    private int numberOfExams;
    private int numberOfStudents;
    private int numberOftimeSlots;
    private ArrayList<MyExam> generationExams;
    private int sizeGeneration;
    private ArrayList<MyScenario> listOfScenarios;

    public MyGeneration(int sizeGeneration, int[][] collisionMatrix, int numberOfExams, int numberOfStudents, int numberOftimeSlots, ArrayList<MyExam> generationExams){
        this.sizeGeneration = sizeGeneration;
        this.listOfScenarios = new ArrayList<>();
        this.listOfScenarios.clear();
        this.collisionMatrix = collisionMatrix;
        this.numberOfExams = numberOfExams;
        this.numberOfStudents = numberOfStudents;
        this.numberOftimeSlots = numberOftimeSlots;
        this.generationExams = generationExams;

        //Creo array di scenari
        MyScenario s;
        for(int i = 0; i < sizeGeneration; i++){
            //i indica l'id dello scenario
            s = new MyScenario(i, collisionMatrix, numberOfExams, numberOfStudents, numberOftimeSlots, generationExams);
            listOfScenarios.add(s);
        }
        orderByPenalty();
    }

    /** GETTER */
    public ArrayList<MyScenario> getListOfScenarios() {
        return listOfScenarios;
    }

    /** METODI PER GESTIRE ARRAY DI SCENARI */
    public void orderByPenalty(){
        listOfScenarios.sort(MyScenario.MyScenarioComparator);
    }


    /** MUTATION */
    public void mutation(int sIDX){
        Random r = new Random();
        MyScenario sOld = getListOfScenarios().get(sIDX);
        MyExam e;
        MyTimeslot t;
        int sNewIDX = sizeGeneration-1-sIDX;
        int eID;
        int tID;
        int sNewID = listOfScenarios.get(sNewIDX).getScenarioID();
        MyScenario sNew = listOfScenarios.get(sNewIDX);
        //al temporaneo passo solo l'id dello scenario nuovo! tutto il resto è di quello vecchio
        MyScenario sTemp = new  MyScenario(sNew.getScenarioID(), sOld.getScenarioExams(), sOld.getScenarioTimeslots().size(), sOld.getScenarioExams().size(), collisionMatrix, numberOfStudents);


        copy(sTemp, sOld);
        //LE MODIFICHE LE FACCIO SOLO SULLO SCENARIO TEMPORANEO
        do {
            //prendo esame e timeslot a caso
            eID = r.nextInt(numberOfExams);
            e = generationExams.get(eID);
            do {
                tID = r.nextInt(numberOftimeSlots);
            } while (generationExams.get(eID).getTimeslotID(sNewID) == tID);
            t = sTemp.getScenarioTimeslots().get(tID);

        } while (sTemp.conflictPerMove(t, e) != 0);

        MyTimeslot from = sTemp.getScenarioTimeslots().get(e.getTimeslotID(sNewID));
        //sposto l'esame e aggiorno il suo timeslot
        from.removeExam(e);
        t.addExam(e);
        //Aggiorno timeslot esame nell'indice dello scenario giusto
        e.addTimeslot(sNewID, t.getTimesotID());
        //Ricalcolo penalty
        sTemp.calculatePenalty();
        //listOfScenarios.add(s);
        //CONTROLLO CHE QUESTA PENALTY NON SIA GIà PRESENTE IN GENERATION
        LocalSearch(sTemp);
        boolean isAlreadyThere = false;

        for (int i = 0; i < sizeGeneration; i++) {
            if(this.listOfScenarios.get(i).getPenalty() == sTemp.getPenalty())
                isAlreadyThere = true;
        }

        if(!isAlreadyThere){
            copy(sNew, sTemp);
        }




    }

    /** PERM TIMESLOT */
    public void permTimeslot(int sIDX){
        int ts1;
        int ts2;
        int sNewIDX = sizeGeneration-1-sIDX; //id scenario
        int tsInExam;
        MyExam e;
        MyTimeslot t;
        MyScenario sNew = listOfScenarios.get(sNewIDX);
        MyScenario sOld = listOfScenarios.get(sIDX);
        MyScenario sTemp = new  MyScenario(sNew.getScenarioID(), sOld.getScenarioExams(), sOld.getScenarioTimeslots().size(), sOld.getScenarioExams().size(), collisionMatrix, numberOfStudents);


        copy(sTemp, sOld);

        //COPIO TUTTI I VALORI DELLO SCENARIO IN POSIZIONE sIDX NELLO SCENARIO IN POSIZIONE sizeGeneration-1-sIDX

        //listOfScenarios.remove(sizeGeneration-1-sIDX);

        //crea il nuovo scenario uguale al primo
        //s = new MyScenario(listOfScenarios.get(sIDX), sID);
        //sTemp = this.listOfScenarios.get(sNewIDX);

        //Prendo due timeslot a caso
        Random r = new Random();
        ts1 = r.nextInt(numberOftimeSlots);
        do{
            ts2 = r.nextInt(numberOftimeSlots);
        }while(ts1 == ts2);

        //Aggiorno esami nel timeslot dello scenario
        sTemp.swapTimeslots(ts1, ts2);
        int a;
        //Aggiorno timeslot dell'esame
        int sNewID= listOfScenarios.get(sNewIDX).getScenarioID();
        for (int i = 0; i < numberOfExams; i++) {
            e = generationExams.get(i);
            tsInExam = e.getTimeslotID(sNewID);
            if(tsInExam == ts1){

                if (ts2 == -1){
                    a =2;
                }

                e.addTimeslot(sNewID, ts2);
            }else if(tsInExam == ts2){
                if (ts1 == -1){
                    a =2;
                }
                e.addTimeslot(sNewID, ts1);
            }
        }

        //Ricalcolo penalty
        sTemp.calculatePenalty();
        //listOfScenarios.add(s);

        //FACCIO LA RICERCA LOCALE
        LocalSearch(sTemp);
        boolean isAlreadyThere = false;

        for (int i = 0; i < sizeGeneration; i++) {
            if(this.listOfScenarios.get(i).getPenalty() == sTemp.getPenalty())
                isAlreadyThere = true;
        }

        if(!isAlreadyThere){
            copy(sNew, sTemp);
        }

    }


    private void copy(MyScenario ScenarioNew,  MyScenario ScenarioToBeCopied){
        //COPIO TUTTO TRANNE L'INDICE DELLO SCENARIO

        //leggo lo scenario to e lo scenario from
         //MyScenario ScenarioToBeCopied = this.getListOfScenarios().get(IDXtoBeCopied);
         //MyScenario ScenarioNew = this.getListOfScenarios().get(IDXnew);
        // SVUOTO IL CONTENUTO DI OGNI ARRAY LIST DI TIMESLOT
        for (int i = 0; i < ScenarioNew.getScenarioTimeslots().size(); i++) {
           ScenarioNew.getScenarioTimeslots().get(i).clearExamList();
        }
        //RIEMPIO IL CONTENUTO DI OGNI ARRAY LIST DI TIMESLOT CON IL CONTENUTO DELL'ARRAY CORRISPONDENTE NELLO SCENARIO TO BE COPIED
        for (int i = 0; i < ScenarioToBeCopied.getScenarioTimeslots().size(); i++) {
            for (int j = 0; j < ScenarioToBeCopied.getScenarioTimeslots().get(i).timeslotExams.size(); j++) {
                ScenarioNew.getScenarioTimeslots().get(i).timeslotExams.add(ScenarioToBeCopied.getScenarioTimeslots().get(i).timeslotExams.get(j));
            }
        }
        //E I TIMESLOT SONO A POSTO
         ScenarioNew.setScenarioConflicts(ScenarioToBeCopied.getScenarioConflicts());

        //DEVO CAMBIARE IL TIMESLOT CHE GLI ESAMI HANNO SALVATO IN POSIZIONE IDNEW
        int IDnew = ScenarioNew.getScenarioID();
        int IDtoBeCopied = ScenarioToBeCopied.getScenarioID();

        for (int i = 0; i < numberOfExams; i++) {
            ScenarioToBeCopied.getScenarioExams().get(i).addTimeslot(IDnew,  ScenarioToBeCopied.getScenarioExams().get(i).getTimeslotID(IDtoBeCopied) );
        }
        //COPIO LA PENALTY
        ScenarioNew.setPenalty(ScenarioToBeCopied.getPenalty());
    }

    public  void tryToEmptyTimeslot(int sIDX){

        int sNewIDX = sizeGeneration-1-sIDX; //id scenario su cui fare cose
        Random r = new Random();
        int timeslotToBeEmptyedID;
        MyTimeslot timeslotToBeEmptyed;
        MyScenario sNew = listOfScenarios.get(sNewIDX);
        MyScenario sOld = listOfScenarios.get(sIDX);
        MyScenario sTemp = new  MyScenario(sNew.getScenarioID(), sOld.getScenarioExams(), sOld.getScenarioTimeslots().size(), sOld.getScenarioExams().size(), collisionMatrix, numberOfStudents);

        // copio il precedente in un temporaneo
        copy(sTemp, sOld);



        MyExam e;
        boolean canBeMoved;
        MyTimeslot possibleTimeslot;
        MyTimeslot to;
        int newTimeslotID = -1;


        //scelgo un timeslot a caso
        timeslotToBeEmptyedID = r.nextInt(sTemp.getScenarioTimeslots().size());
        timeslotToBeEmptyed = sTemp.getScenarioTimeslots().get(timeslotToBeEmptyedID);

        for (int i = 0; i < timeslotToBeEmptyed.getExamListSize(); i++) {
            e = timeslotToBeEmptyed.getExam(i);
            canBeMoved = false;
            for (int j = 0; j < sTemp.getScenarioTimeslots().size(); j++) {
                possibleTimeslot = sTemp.getScenarioTimeslots().get(j);
                if (j != timeslotToBeEmptyedID && sTemp.conflictPerMove(possibleTimeslot, e) == 0){
                   canBeMoved = true;
                   newTimeslotID = j;
                   break;

                }
            }
            if (canBeMoved){
                //FACCIO LA MOSSA
                //sposto l'esame e aggiorno il suo timeslot
                timeslotToBeEmptyed.removeExam(e);
                to = sTemp.getScenarioTimeslots().get(newTimeslotID);
                to.addExam(e);
                //Aggiorno timeslot esame nell'indice dello scenario giusto
                e.addTimeslot(sTemp.getScenarioID(), to.getTimesotID());

            }
        }
        //Ricalcolo penalty

        sTemp.calculatePenalty();

        //FACCIO LA RICERCA LOCALE
        LocalSearch(sTemp);
        boolean isAlreadyThere = false;

        for (int i = 0; i < sizeGeneration; i++) {
            if(this.listOfScenarios.get(i).getPenalty() == sTemp.getPenalty())
                isAlreadyThere = true;
        }

        if(!isAlreadyThere){
            copy(sNew, sTemp);
        }

    }
    public void LocalSearch(MyScenario s){

        MyExam e;
        MyTimeslot t;
        double penalty;
        double leastPenalty;
        int bestTimeslotID = -1;
        MyTimeslot bestTimeslot;
        ArrayList<Integer> examsToBeMoved = new ArrayList<>();
        Random r = new Random();
        int newIDX;

        //CREO UN VETTORE DI INTERI RANDOM-ORDERED

        for (int i = 0; i < numberOfExams; i++) {
            do{
                newIDX = r.nextInt(numberOfExams);
            }while (examsToBeMoved.contains(newIDX));
        }

        for (int i = 0; i < numberOfExams; i++) {
            e = generationExams.get(i);
            leastPenalty = s.getPenalty();
            //per ogni esame vado a calcolare il timeslot migliore (vado a valutare anche il suo!!!!!)
            for (int j = 0; j < s.getScenarioTimeslots().size(); j++) {
                t = s.getScenarioTimeslots().get(j);
                if (s.MoveIsFeasible(e, t)) {
                    penalty = s.PenaltyPerMove(e, t);
                    if (penalty < leastPenalty) {
                        leastPenalty = penalty;
                        bestTimeslotID = j;
                    }
                }
            }
            if (bestTimeslotID != -1){
                //AGGIORNO LO SCENARIO
                int a;
                if (e.getTimeslotID(s.getScenarioID()) == -1)
                    a = 1;
                if (s.getScenarioID() == -1)
                    a = 0;


                MyTimeslot from = s.getScenarioTimeslots().get(e.getTimeslotID(s.getScenarioID()));
                //sposto l'esame e aggiorno il suo timeslot
                from.removeExam(e);
                bestTimeslot = s.getScenarioTimeslots().get(bestTimeslotID);
                //todo rimuovere
                // non gli ho mai aggiunto un meno uno!!!!!!
                /*if (bestTimeslot.getTimesotID() == -1){
                    a =2;
                }
                if (bestTimeslotID == -1){
                    a=1;
                }*/
                bestTimeslot.addExam(e);
                //Aggiorno timeslot esame nell'indice dello scenario giusto
                e.addTimeslot(s.getScenarioID(), bestTimeslot.getTimesotID());
                //Ricalcolo penalty
            }

        }
        s.calculatePenalty();

    }



}
