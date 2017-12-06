import java.util.LinkedList;
import java.util.List;

public class TextPartition {
    private PartitionType partitionType;
    private String partitionName;
    private List<TextPartition> subPartitions = new LinkedList<TextPartition>();
    private String text;

    public String toString(){
        StringBuilder builder = new StringBuilder();
        builder.append(text);
        builder.append(System.lineSeparator());
        for(TextPartition i : subPartitions){
            builder.append(i.toString());
            builder.append(System.lineSeparator());
        }
        return builder.toString();
    }
}
