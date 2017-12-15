
import java.util.LinkedList;
import java.util.Scanner;

public class TextCutter {
    private StatuteType type;

    private LinkedList<TextPartition> cutFile = new LinkedList<>();


    public TextCutter(StatuteType t){
        this.type = t;
    }

    // Analyzes each line by assigning PartitionType and creating new PartitionType objects, which are then given to addPartition method
    public void cutPackAdd(Scanner scanner){

        String nextLine;
        String tmp;
        TextPartition currentNode = null;

        while(scanner.hasNextLine()){
            nextLine = scanner.nextLine();

            switch(assignType(nextLine)){
                case Trash:
                    break;


                case Trash2:
                    nextLine = scanner.nextLine();
                    break;


                case Text:
                    while(nextLine.charAt(nextLine.length()-1) == '-'){
                        tmp = scanner.nextLine();
                        if(assignType(tmp) == PartitionType.Trash2){
                            tmp = scanner.nextLine();
                            tmp = scanner.nextLine();
                        }

                        nextLine = nextLine.substring(0, nextLine.length()-1);
                        nextLine += firstWord(tmp);
                        tmp = deletedFirstWord(tmp);
                        if(!tmp.equals("")) nextLine += System.lineSeparator() + tmp;
                    }
                    if(currentNode != null) {
                        if(currentNode.getPartitionType() == PartitionType.Chapter){
                            currentNode.setPartitionName(currentNode.getPartitionName() + " " + nextLine);
                        }
                        else {
                            currentNode.addText(nextLine);
                        }
                    } else if(nextLine.substring(0,14).equals("W trosce o byt")){
                        currentNode = new TextPartition(PartitionType.Section);
                        currentNode.setPartitionName("Preambuła");
                        currentNode.addText("\n" + nextLine);
                        addPartition(currentNode);
                    }
                    break;


                case Section:
                    currentNode = new TextPartition(PartitionType.Section);
                    nextLine += System.lineSeparator() + scanner.nextLine();
                    currentNode.setPartitionName(nextLine);
                    addPartition(currentNode);
                    break;


                case Chapter:
                    currentNode = new TextPartition(PartitionType.Chapter);
                    if(type == StatuteType.Act) nextLine += System.lineSeparator() + scanner.nextLine();
                    currentNode.setPartitionName(nextLine);
                    addPartition(currentNode);
                    break;


                case Article:
                    if(type == StatuteType.Act && deletedFirstWord(deletedFirstWord(nextLine)).substring(0, 7).equals("(pomini")) break;
                    currentNode = new TextPartition(PartitionType.Article);

                    if(type == StatuteType.Constitution){
                        currentNode.setPartitionName(nextLine);
                    }
                    else{
                        tmp = firstWord(nextLine);                 // tmp = "Art."
                        nextLine = deletedFirstWord(nextLine);     // nextLine = "No. [text]"
                        tmp += " " + firstWord(nextLine);          // tmp = "Art. No."
                        nextLine = deletedFirstWord(nextLine);     // nextLine = "[text]"
                        currentNode.setPartitionName(tmp);
                        currentNode.addText(nextLine);
                    }

                    addPartition(currentNode);
                    break;


                case Paragraph: case Point: case Subpoint:
                    currentNode = new TextPartition(assignType(nextLine));
                    currentNode.setPartitionName(firstWord(nextLine));

                    while(nextLine.charAt(nextLine.length()-1) == '-'){
                        tmp = scanner.nextLine();
                        nextLine = nextLine.substring(0, nextLine.length()-1);
                        nextLine += firstWord(tmp);
                        if(!deletedFirstWord(tmp).equals("")) nextLine += System.lineSeparator() + deletedFirstWord(tmp);
                    }

                    currentNode.addText(deletedFirstWord(nextLine));
                    addPartition(currentNode);
                    break;


                default:
                    break;
            }
        }
    }

    // Returns PartitionType based on given line (e.g. It would return PartitionType.Article on "Art. 5." input line)
    private PartitionType assignType(String sample){
        if(sample.length() < 2) return PartitionType.Trash;
        if(sample.length() > 16 && sample.substring(1,17).equals("Kancelaria Sejmu")) return PartitionType.Trash2;

        if(sample.length() > 5){
            if((sample.substring(0,5).equals("DZIAŁ") && type == StatuteType.Act) || ((sample.length() > 7) && (type == StatuteType.Constitution) && (sample.substring(0,8).equals("Rozdział")))) return PartitionType.Section;
            if(sample.substring(0,4).equals("Art.")) return PartitionType.Article;
            if(sample.length() > 8 && type == StatuteType.Act && sample.substring(0,8).equals("Rozdział")) return PartitionType.Chapter;
            if(!cutFile.isEmpty() && type == StatuteType.Constitution && sample.toUpperCase().equals(sample)) return PartitionType.Chapter;
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

    // Returns first word from a given string
    private String firstWord(String k){
        String tmp = "";
        int i = 0;

        while(k.length() > i && k.charAt(i)!=' '){
            tmp += k.charAt(i);
            i++;
        }
        return tmp;
    }

    // Returns string with deleted first word
    private String deletedFirstWord(String k){
        int i = 0;

        while(k.length() > i && k.charAt(i)!=' '){
            i++;
        }

        if(i == k.length()) return "";
        return k.substring(i+1,k.length());
    }

    // Adds TextPartition object to cutFile LinkedList
    private void addPartition(TextPartition addition){
        TextPartition tmp;

        switch(addition.getPartitionType()){
            case Section:
                cutFile.add(addition);
                break;

            case Chapter:
                tmp = cutFile.getLast();            // tmp = latest Section
                tmp.addSubPartition(addition);      // We add newest Chapter to the latest Section
                break;

            case Article:
                tmp = cutFile.getLast();                                // tmp = latest Section
                if(!tmp.isListEmpty() && tmp.lastSubPartition().getPartitionType() == PartitionType.Chapter) tmp = tmp.lastSubPartition();    // tmp = latest Chapter of latest Section
                tmp.addSubPartition(addition);                          // We add newest Article to the latest Chapter
                break;

            case Paragraph:
                tmp = cutFile.getLast();                                // tmp = latest Section
                if(!tmp.isListEmpty() && tmp.lastSubPartition().getPartitionType() == PartitionType.Chapter) tmp = tmp.lastSubPartition();    // tmp = latest Chapter of latest Section
                if(!tmp.isListEmpty() && tmp.lastSubPartition().getPartitionType() == PartitionType.Article) tmp = tmp.lastSubPartition();    // tmp = latest Article of latest Chapter...
                tmp.addSubPartition(addition);                          // We add newest Paragraph to the latest Article
                break;

            case Point:
                tmp = cutFile.getLast();                                // Section
                if(!tmp.isListEmpty() && tmp.lastSubPartition().getPartitionType() == PartitionType.Chapter) tmp = tmp.lastSubPartition();    // Chapter
                if(!tmp.isListEmpty() && tmp.lastSubPartition().getPartitionType() == PartitionType.Article) tmp = tmp.lastSubPartition();    // Article
                if(!tmp.isListEmpty() && tmp.lastSubPartition().getPartitionType() == PartitionType.Paragraph) tmp = tmp.lastSubPartition();    // Paragraph
                tmp.addSubPartition(addition);                          // add Point
                break;

            case Subpoint:
                tmp = cutFile.getLast();                                // Section
                if(!tmp.isListEmpty()) tmp = tmp.lastSubPartition();    // Chapter
                if(!tmp.isListEmpty()) tmp = tmp.lastSubPartition();    // Article
                if(!tmp.isListEmpty()) tmp = tmp.lastSubPartition();    // Paragraph
                if(!tmp.isListEmpty()) tmp = tmp.lastSubPartition();    // Point
                tmp.addSubPartition(addition);                          // add SubPoint
                break;
        }
    }


    public LinkedList<TextPartition> getPartitionsList(){
        return cutFile;
    }
}
