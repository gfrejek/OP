import java.io.Console;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Scanner;

public class Parser{
    private static Scanner scanner;
    private static StatuteType fileType;


    //-----------------------------------------------------------------\\
    // openFile returns null if file with given path cannot be opened  \\
    // openFile returns Scanner if file exists and can be opened       \\
    //-----------------------------------------------------------------\\
    private static Scanner openFile(String path){
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


    //-------------------------------------------------------------------------------------\\
    // Constructor checks whether or not one of the arguments is path to the desired file  \\
    // and assigns this path to 'filePath' and assigns Scanner to 'scanner' variable       \\
    //-------------------------------------------------------------------------------------\\
    public static void parse(String[] arguments){
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
        String newPath;
        Scanner s1;

        while(!foundRightFile){
            System.out.println("Specify file path to the right file (act or constitution)");
            System.out.print("Path: ");
            s1 = new Scanner(System.in);
            newPath = s1.next();


            tmpScanner = openFile(newPath);
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

        System.out.println("Statute type: " + tmpType.toString() + System.lineSeparator() + "File has been loaded successfully" + System.lineSeparator());
    }


    //--------------------------------------------------------------------\\
    // checkType returns the type of the file with 'file' handle variable \\
    // checkType returns null if it is not the desirable file             \\
    //--------------------------------------------------------------------\\
    private static StatuteType checkType(Scanner file){
        for(int i = 0; (i < 50) && (file.hasNextLine()); i++){
            String readLine = file.nextLine();
            if(readLine.equals("KONSTYTUCJA")) return StatuteType.Constitution;
            if(readLine.equals("USTAWA")) return StatuteType.Act;
        }
        return null;
    }

    public static StatuteType getFileType() {
        return fileType;
    }
    public static Scanner getScanner(){ return scanner; }
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
