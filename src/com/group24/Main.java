package com.group24;

import Interface.ArgsParser;
import Interface.FileManager;
import Interface.ProgressBarRotating;
import genetic_algorithm.MyGeneration;
import genetic_algorithm.MyGeneticAlgorithm;
import tabu_search.MyTabuSearch;

import java.io.FileNotFoundException;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {

        if(ArgsParser.parsing(args).equals("acquire")){
            mainLoop(args[0],Integer.parseInt(args[2]));
        }
        return;

    }

    public static void mainLoop(String fileName, int seconds){


        try {

            String path             = fileName;
            int numberOfExams       = FileManager.getExamsNumber(path);
            int[][] matrix          = new int[numberOfExams][numberOfExams];
            int     numberOftimeSlots = FileManager.loadSlots(path);
            ArrayList<MyExam>         exams;

            //DEFINIZIONE PARAMETRI
            int maxNumberOfIterationTabuSearcher = 500000;
            int dimTabuList = 8;//numberOftimeSlots;
            //int numberAttemptsImproveGeneration = 500;
            int sizeGeneration = 500;
            //int maxMovesToImprovePenalty = 100; //toDo capire che numero usare
            int neighborhoodSize = 15;//Math.round(numberOfExams/10);

            //SETTAGGIO PARAMETRI NELLE CLASSI
            MyTabuSearch.setMaxNumberOfIteration(maxNumberOfIterationTabuSearcher);
            MyTabuSearch.setTabuListDimension(dimTabuList);
            MyTabuSearch.setNeighborhoodSize(neighborhoodSize);

            //GeneticAlgorithm.setNumberAttemptsImproveGeneration(numberAttemptsImproveGeneration);
            //TabuSearchImprovePenalty.setMaxNumberOfIteration(maxMovesToImprovePenalty);
            //TabuSearchImprovePenalty.setNeighborhoodSize(neighborhoodSize);
            //TabuSearchImprovePenalty.setTabuListDimension(dimTabuList);

            exams = FileManager.getExams(path, sizeGeneration);
            FileManager.makeCollisions(matrix, path, exams);

            int numberOfStudents = FileManager.getNumberOfStudents(path);

            MyScenario best;
            MyGeneticAlgorithm g;

            g = new MyGeneticAlgorithm(sizeGeneration, matrix, numberOfExams, numberOfStudents, numberOftimeSlots, exams,seconds);
            best = g.getBestScenario();
            System.out.print("\nBest penalty: " + best.getPenalty());

            FileManager.solWriter(best,path);
            System.out.println();
        }catch (FileNotFoundException e){
            System.out.println("File error: file not found");
        }

    }
}
