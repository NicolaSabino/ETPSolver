//package tabu_search;
//
//public final class TabuSearchImprovePenalty {
//
//
//    private static TabuList                 tabuList;
//    private static int                      maxNumberOfIteration;
//    private static int[][]                  collisionMatrix;
//    //private static Exam                     prevExam;
//    //private static int                      countConsecutiveExam = 0;
//    private static int                      tabuListDimesion;
//    //private static int                      maxConsecutiveExams;
//    //private static int                      examTimeRemaining;
//    private static int                        neighborhoodSize;
//
//    public static Scenario generate(Scenario scenario,int tMax, int[][] matrix){
//
//
//        bootStrap(matrix);
//
//        int movesDone = 0;
//        Exam movable = new Exam(-1);
//
//        while ( movesDone < maxNumberOfIteration ){
//
//
//            //prevExam = checkAndChoose(scenario, prevExam); //Aggiorna esame WORST in SCENARIO
//            /* System.out.println("Exam: " + scenario.getWorst().getExamID());*/
//
//            TimeSlot to = scenario.bestMove(neighborhoodSize, tabuList);
//
//
//
//            scenario.updateScenario(to,tabuList,scenario.movable);
//
//
//            movesDone++;
//        }
//
//        //System.out.println("COUNTER MOSSE FATTE:" + counter);
//
//
//        return scenario;
//    }
//
//
//    private static void bootStrap(int[][] matrix){
//        //maxNumberOfIteration    =   5000;
//        tabuList                =   new TabuList(tabuListDimesion);//(matrix.length/2);
//        collisionMatrix         =   matrix;
//
//    }
//
//
//
//    public static void setMaxNumberOfIteration(int max){
//        maxNumberOfIteration = max;
//    }
//
//    public static void setTabuListDimension(int dimension){
//        tabuListDimesion = dimension;
//    }
//
//    /*public static void setMaxConsecutiveExams(int maxConsecutiveExams) {
//        TabuSearchImprovePenalty.maxConsecutiveExams = maxConsecutiveExams;
//    }
//
//    public static void setExamTimeRemaining(int timeRemaining){
//        examTimeRemaining = timeRemaining;
//    }*/
//
//    public static void setNeighborhoodSize(int size){
//        neighborhoodSize = size;
//    }
//
//    public static int getNeighborhoodSize(){
//        return neighborhoodSize;
//    }
//}