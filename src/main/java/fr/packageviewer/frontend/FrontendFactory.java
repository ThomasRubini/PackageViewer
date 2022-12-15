package fr.packageviewer.frontend;

public class FrontendFactory {

    public static Frontend get(String name){
        switch(name){
            case "terminal":{
                return new FrontendTerminal();
            }
            default:{
                throw new IllegalArgumentException("Invalid frontend");
            }
        }
    }

}
