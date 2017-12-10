
import java.util.Scanner;

public class StatuteReader {

    public static void main(String[] arguments){
        boolean q = false;
        Parser parser = new Parser();
        parser.parse(arguments);

        int skippedLines;
        if(parser.getFileType() == StatuteType.Act) skippedLines = 2;
        else skippedLines = 44;
        for(int i = 0; i < skippedLines ; i++){
            parser.getScanner().nextLine();
        }

        TextCutter textCutter = new TextCutter(parser.getFileType());
        textCutter.cutPackAdd(parser.getScanner());


        TextViewer textViewer = new TextViewer(textCutter.getPartitionsList());

        System.out.println(textViewer.viewHelp(parser.getFileType()));

        while(!q){
            String lastChar;
            Scanner s1 = new Scanner(System.in);
            lastChar = s1.next();
            lastChar = lastChar.toLowerCase();

            System.out.println();

            switch(lastChar.toLowerCase()){
                case "q": case "quit":
                    q = true;
                    break;
                case "h": case "help":
                    System.out.println(textViewer.viewHelp(parser.getFileType()));
                    break;
                case "w": case "whole": case "whole text":
                    System.out.println(textViewer.viewWhole());
                    break;
                case "t": case "table": case "table of content": case "content":
                    System.out.println(textViewer.viewTableOfContent());
                    break;


            }
        }

    }
}
