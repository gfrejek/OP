
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
        if(!text.equals("")){
            builder.append(text);
        }
        builder.append(System.lineSeparator());


        if(!this.isListEmpty()){
            for(TextPartition i : subPartitions){
                if(i.getPartitionType() == PartitionType.Article || i.getPartitionType() == PartitionType.Chapter) builder.append(System.lineSeparator());
                builder.append(i.toString());
            }
        }

        return builder.toString();
    }

    public void addText(String addition){
        if(!text.equals("") || partitionType == PartitionType.Article) this.text += System.lineSeparator();
        else this.text += " ";
        this.text += addition;
    }

    public void setPartitionName(String name){
        this.partitionName = name;
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

    public TextPartition firstSubPartition() { return subPartitions.getFirst(); }

    public boolean isListEmpty(){
        return subPartitions.isEmpty();
    }

    public String getPartitionName(){
        return partitionName;
    }

    public String getText(){
        return text;
    }

    public LinkedList<TextPartition> getSubPartitions() {
        return subPartitions;
    }
}
