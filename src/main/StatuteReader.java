
import java.util.Scanner;

public class StatuteReader {

    public static void main(String[] arguments){
        boolean q = false;
        Parser parser = new Parser();
        parser.parse(arguments);

        while(!q){
            System.out.println(System.lineSeparator() + "Type q to quit");
            System.out.println("Type t to see table of content");
            if(parser.getFileType() == StatuteType.Constitution) System.out.println("Type p to see preamble");
            System.out.println("Type s to choose section(s) you wish to see");
            System.out.println("Type c to choose chapter(s) you wish to see");
            System.out.println("Type a to choose article(s) you wish to see");

            String lastChar;
            Scanner s1 = new Scanner(System.in);
            lastChar = s1.next();
            lastChar = lastChar.toLowerCase();
            System.out.println();
            switch(lastChar){
                case "q": case "quit":
                    q = true;
                    break;
                case "t":
                    TextCutter test = new TextCutter(parser.getFileType());


            }
        }

    }
}
