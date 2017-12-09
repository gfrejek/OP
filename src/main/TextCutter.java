
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class TextCutter {
    private StatuteType type;

    private List<TextPartition> cutFile = new LinkedList<TextPartition>();


    public TextCutter(StatuteType t){
        this.type = t;
    }


    public void cutCutCut(Scanner scanner, StatuteType type){

        String nextLine = "";
        String tmpLine = "";
        boolean canRead = true;

        while(scanner.hasNextLine()){
            if(canRead) nextLine = scanner.nextLine();
            canRead = true;

            switch(assignType(nextLine)){
                case Trash:
                    break;
                case Trash2:
                    nextLine = scanner.nextLine();
                    break;
                case Text:
                    if(type == StatuteType.Constitution && nextLine.charAt(nextLine.length()-1) == '-'){
                        tmpLine = scanner.nextLine();
                        canRead = false;
                        nextLine = nextLine.substring(0, nextLine.length()-1);
                        nextLine += firstWord(tmpLine);                      // nextLine is fine
                        tmpLine = deletedFirstWord(tmpLine);                    // tmpLine is fine
                    }
                    //add Text to currentNode
                    nextLine = tmpLine;
                    break;
                default:
                    break;
                //TODO rest cases
            }
        }
    }


    private PartitionType assignType(String sample){
        if(sample.length() < 2) return PartitionType.Trash;
        if(sample.length() > 16 && sample.substring(1,17).equals("Kancelaria Sejmu")) return PartitionType.Trash2;

        if(sample.length() > 5){
            if((sample.substring(0,5).equals("DZIAŁ") && type == StatuteType.Act) || ((sample.length() > 7) && (type == StatuteType.Constitution) && (sample.substring(0,8).equals("Rozdział")))) return PartitionType.Section;
            if(sample.substring(0,4).equals("Art.")) return PartitionType.Article;
            if(sample.length() > 8 && type == StatuteType.Act && sample.substring(0,8).equals("Rozdział")) return PartitionType.Chapter;
            if(type == StatuteType.Constitution && sample.toUpperCase().equals(sample)) return PartitionType.Chapter;
            if(Character.isDigit(sample.charAt(0))){
                int i=1;
                while(sample.length()>i && (Character.isDigit(sample.charAt(i)) || Character.isLetter(sample.charAt(i)))) i++;
                if(sample.length() == i) return PartitionType.Trash;
                else if(sample.charAt(i) == '.') return PartitionType.Paragraph;
                else return PartitionType.Point;
                }
            if(type == StatuteType.Act && Character.isLetter(sample.charAt(0)) && sample.charAt(1) == ')') return PartitionType.Subpoint;
            }
        return PartitionType.Text;
        }


    private String firstWord(String k){
        String tmp = "";
        int i = 0;

        while(k.length() > i && k.charAt(i)!=' '){
            tmp += k.charAt(i);
            i++;
        }
        return tmp;
    }


    private String deletedFirstWord(String k){
        int i = 0;

        while(k.length() > i && k.charAt(i)!=' '){
            i++;
        }

        if(i == k.length()) return "";
        return k.substring(i+1,k.length());
    }
    //TODO merge chapter1 chapter2 in cutCutCut

}
