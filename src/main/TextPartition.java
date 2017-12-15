
import java.util.LinkedList;

public class TextPartition {
    private PartitionType partitionType;
    private String partitionName;
    private LinkedList<TextPartition> subPartitions = new LinkedList<>();
    private String text;

    public TextPartition(PartitionType type){
        this.partitionType = type;
        this.text = "";
        this.partitionName = "";

        if(type == PartitionType.Trash  ||
           type == PartitionType.Trash2 ||
           type == PartitionType.Text)
                throw new IllegalArgumentException("Błąd przetwarzania pliku, stworzono obiekt klasy `TextPartition` z typem `Trash` lub `Text`");
    }

    // Returns string which contains this.name, this.text and recursively adds name and text of every subPartition
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

    // Adds new line of text to this.text
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

    // Adds given TextPartition to subPartition LinkedList
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
