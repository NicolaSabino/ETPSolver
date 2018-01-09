package tabu_search;

import com.group24.MyExam;
import com.group24.MyTimeslot;
import java.util.LinkedList;


public class TabuList{

    private LinkedList<MyExam>         exams;
    private LinkedList<MyTimeslot>     t_from;
    private LinkedList<MyTimeslot>     t_to;
    private int                      maxSize;

    public TabuList(int size) {
        this.exams          = new LinkedList<>();
        this.t_from         = new LinkedList<>();
        this.t_to           = new LinkedList<>();
        this.maxSize        = size;
    }

    public void addToTabu(MyExam e,MyTimeslot from,MyTimeslot to){

        if( this.exams.size() < maxSize){

            this.exams.add(e);
            this.t_from.add(from);
            this.t_to.add(to);

        }else {

            this.exams.remove();
            this.t_from.remove();
            this.t_to.remove();

            this.exams.add(e);
            this.t_from.add(from);
            this.t_to.add(to);

        }
    }

    public boolean istabu(MyExam e,MyTimeslot from,MyTimeslot to){

        for (int i = 0; i < exams.size(); i++) {

            boolean right   = this.t_from.get(i).equals(from) && this.t_to.get(i).equals(to);
            boolean reverse = this.t_from.get(i).equals(to) && this.t_to.get(i).equals(from);

            if (this.exams.get(i).equals(e) && (right || reverse)){
                return true;
            }
        }
        return false;
    }

}