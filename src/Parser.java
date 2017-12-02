import java.io.Console;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Scanner;

public class Parser{
    private Scanner scanner;
    private String filePath;
    private StatuteType fileType;


    //-----------------------------------------------------------------\\
    // openFile returns null if file with given path cannot be opened  \\
    // openFile returns Scanner if file exists and can be opened       \\
    //-----------------------------------------------------------------\\
    private Scanner openFile(String path){
        Scanner result = null;
        FileInputStream inputStream = null;

        try{
            inputStream = new FileInputStream(path);
            result = new Scanner(inputStream, "UTF-8");
            if(result.ioException() != null) {
                throw result.ioException();
            }
        } catch(IOException ex){
            return null;
            // todo Handling the exception
        }
        return result;
    }


    //-------------------------------------------------------------------------------\\
    // parse checks whether or not one of the arguments is path to the desired file  \\
    // and assigns this path to 'filePath' and assigns Scanner to 'scanner' variable \\
    //-------------------------------------------------------------------------------\\
    public Parser(String[] arguments){
        boolean foundRightFile = false;
        Scanner tmpScanner;
        StatuteType tmpType = null;

        for(String tmpPath : arguments){
            tmpScanner = openFile(tmpPath);
            if(tmpScanner != null){
                tmpType = checkType(tmpScanner);
                if(tmpType != null){
                    foundRightFile = true;
                    fileType = tmpType;
                    scanner = tmpScanner;
                    break;
                }
            }
        }

        if(foundRightFile){
            System.out.println("Statute type: " + tmpType.toString());     //todo Delete
        } else {
            System.out.println("Need to specify file path to the right file");
            // todo Demand an input
        }
    }


    //--------------------------------------------------------------------\\
    // checkType returns the type of the file with 'file' handle variable \\
    // checkType returns null if it is not the desirable file             \\
    //--------------------------------------------------------------------\\
    private StatuteType checkType(Scanner file){
        for(int i = 0; (i < 50) && (file.hasNextLine()); i++){
            String readLine = file.nextLine();
            if(readLine.equals("KONSTYTUCJA")) return StatuteType.Constitution;
            if(readLine.equals("USTAWA")) return StatuteType.Act;
        }
        return null;
    }
}

enum StatuteType {
    Constitution,
    Act;

    @Override
    public String toString() {
        if(this == Act) return "an act";
        if(this == Constitution) return "a constitution";
        return null;
    }
}
