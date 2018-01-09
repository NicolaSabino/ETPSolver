package Interface;

import java.util.ArrayList;
import java.util.List;

public final class ArgsParser {

    //attributi
    private static int             numberOfAttributes;
    private static List<String>    arguments;
    private static String          istanceName;
    private static Integer         maxTime;


    public static String parsing(String[] args){

        numberOfAttributes = args.length;
        arguments = new ArrayList<String>();

        for (String argument : args)
        {
            arguments.add(argument);
        }

        istanceName = null;

        String error    = "Syntax Error: type ETPsolver_OMAAL_group24 -h or --help";
        String help     = "Use the program with the proper syntax \nETPsolver_OMAAL_group24 [ISTANCE NAME] -t [MAX TIME]";

        switch (numberOfAttributes) {

            case 1:{
                if(arguments.get(0).equals("-h") || arguments.get(0).equals("--help")){
                    System.out.println(help);
                    return "help";
                }
                else{
                    System.out.println(error);
                    return "error";
                }
            }

            case 3:{

                if(arguments.get(1).equals("-t")){
                    istanceName = arguments.get(0);
                    maxTime     = Integer.parseInt(arguments.get(2));
                    return "acquire";
                }
                else return "error";
            }

            default:{
                System.out.println(error);
                return "error";
            }
        }
    }

    public static String getIstanceName() {
        return istanceName;
    }

    public static Integer getMaxTime() {
        return maxTime;
    }

}
