import java.io.File;
import java.util.Scanner;

public class StatuteReader {

    public static void main(String[] arguments){
        boolean q = false;
        Parser.parse(arguments);
        System.out.println(Parser.getScanner().nextLine());

        while(!q){
            System.out.println("Type q to quit");
            System.out.println("Type s to see section");
            System.out.println("Type c to see chapter");
            System.out.println("Type a to see article");
            if(Parser.getFileType() == StatuteType.Constitution) System.out.println("Type p to see preamble");


            String lastChar;
            Scanner s1 = new Scanner(System.in);
            lastChar = s1.next();
            lastChar = lastChar.toLowerCase();
            switch(lastChar){
                case "q": case "quit":
                    q = true;
            }
        }

    }
}
