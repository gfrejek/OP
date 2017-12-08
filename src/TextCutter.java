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
        if(sample.length() > 5){
            if(sample.substring(0,5).equals("DZIAŁ") || ((Parser.getFileType()==StatuteType.Constitution) && (sample.substring(0,8).equals("Rozdział")))) return PartitionType.Section;
            if(sample.substring(0,4).equals("Art.")) return PartitionType.Article;
            if(sample.length() > 16 && sample.substring(1,17).equals("Kancelaria Sejmu")) return PartitionType.Trash2;
            if(sample.length() > 8 && Parser.getFileType()==StatuteType.Act && sample.substring(0,8).equals("Rozdział")) return PartitionType.Chapter1;
            if(Parser.getFileType()==StatuteType.Constitution && sample.toUpperCase().equals(sample)) return PartitionType.Chapter2;

        }
    }
}
