
import java.util.LinkedList;
import java.util.List;

public class TextPartition {
    private PartitionType partitionType;
    private String partitionName;
    private LinkedList<TextPartition> subPartitions = new LinkedList<TextPartition>();
    private String text;

    public TextPartition(PartitionType type){
        this.partitionType = type;
        this.text = "";
        this.partitionName = "";

        if(type == PartitionType.Trash  ||
           type == PartitionType.Trash2 ||
           type == PartitionType.Text)
                System.out.println("Something went horribly wrong");
                //TODO Throw an exception

    }

    public String toString(){
        StringBuilder builder = new StringBuilder();
        builder.append(partitionName);

        if(partitionType == PartitionType.Section ||
           partitionType == PartitionType.Chapter ||
           partitionType == PartitionType.Article)
               builder.append(System.lineSeparator());
        else   builder.append(" ");

        builder.append(text);
        builder.append(System.lineSeparator());
        for(TextPartition i : subPartitions){
             builder.append(i.toString());
             builder.append(System.lineSeparator());
        }
        return builder.toString();
    }

    public void addText(String addition){
        this.text += System.lineSeparator();
        this.text += addition;
    }

    public void setPartitionName(String name){
        if(this.partitionName != "") this.partitionName = name;
    }

    public PartitionType getPartitionType() {
        return partitionType;
    }

    public void addSubPartition(TextPartition addition){
        subPartitions.add(addition);
    }

    public TextPartition lastSubPartition(){
        return subPartitions.getLast();
    }

    public boolean isListEmpty(){
        return subPartitions.isEmpty();
    }
}
