import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class TextCutter {
    private Scanner scanner;
    private StatuteType type;

    private List<TextPartition> cutFile = new LinkedList<TextPartition>();


    public TextCutter(Scanner n, StatuteType t){
        this.scanner = n;
        this.type = t;
    }


    private PartitionType assignType(String sample){
        if(sample.length() < 2) return PartitionType.Trash;
        if(sample.length() > 16 && sample.substring(1,17).equals("Kancelaria Sejmu")) return PartitionType.Trash2;

        if(sample.length() > 5){
            if(sample.substring(0,5).equals("DZIAŁ") || ((Parser.getFileType()==StatuteType.Constitution) && (sample.substring(0,8).equals("Rozdział")))) return PartitionType.Section;
            if(sample.substring(0,4).equals("Art.")) return PartitionType.Article;
            if(sample.length() > 8 && Parser.getFileType()==StatuteType.Act && sample.substring(0,8).equals("Rozdział")) return PartitionType.Chapter1;
            if(Parser.getFileType()==StatuteType.Constitution && sample.toUpperCase().equals(sample)) return PartitionType.Chapter2;
            if(Character.isDigit(sample.charAt(0))){
                int i=1;
                while(sample.length()>i && (Character.isDigit(sample.charAt(i)) || Character.isLetter(sample.charAt(i)))) i++;
                if(sample.length() == i) return PartitionType.Trash;
                else if(sample.charAt(i) == '.') return PartitionType.Paragraph;
                else return PartitionType.Point;
                }
            if(Character.isLetter(sample.charAt(0)) && sample.charAt(1) == ')') return PartitionType.Subpoint;
            }
        return null;
        }
    }
