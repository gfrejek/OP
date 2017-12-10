
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class TextCutter {
    private StatuteType type;

    private LinkedList<TextPartition> cutFile = new LinkedList<TextPartition>();


    public TextCutter(StatuteType t){
        this.type = t;
    }


    public void cutPackAdd(Scanner scanner, StatuteType type){

        String nextLine = "";
        String tmp = "";
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
                    if(/*type == StatuteType.Constitution && */nextLine.charAt(nextLine.length()-1) == '-'){
                        tmp = scanner.nextLine();
                        nextLine = nextLine.substring(0, nextLine.length()-1);
                        nextLine += firstWord(tmp);
                        tmp = deletedFirstWord(tmp);
                        if(!tmp.equals("")) nextLine += System.lineSeparator() + tmp;
                    }
                    if(currentNode != null) currentNode.addText(nextLine);
                    break;


                case Section:
                    currentNode = new TextPartition(PartitionType.Section);
                    nextLine += scanner.nextLine();
                    currentNode.setPartitionName(nextLine);
                    addPartition(currentNode);
                    break;


                case Chapter:
                    currentNode = new TextPartition(PartitionType.Chapter);
                    if(type == StatuteType.Act) nextLine += scanner.nextLine();
                    currentNode.setPartitionName(nextLine);
                    addPartition(currentNode);
                    break;


                case Article:
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
                    currentNode.addText(deletedFirstWord(nextLine));
                    addPartition(currentNode);
                    break;


                default:
                    break;
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
                if(!tmp.isListEmpty()) tmp = tmp.lastSubPartition();    // tmp = latest Chapter of latest Section
                tmp.addSubPartition(addition);                          // We add newest Article to the latest Chapter
                break;

            case Paragraph:
                tmp = cutFile.getLast();                                // tmp = latest Section
                if(!tmp.isListEmpty()) tmp = tmp.lastSubPartition();    // tmp = latest Chapter of latest Section
                if(!tmp.isListEmpty()) tmp = tmp.lastSubPartition();    // tmp = latest Article of latest Chapter...
                tmp.addSubPartition(addition);                          // We add newest Paragraph to the latest Article
                break;

            case Point:
                tmp = cutFile.getLast();                                // Section
                if(!tmp.isListEmpty()) tmp = tmp.lastSubPartition();    // Chapter
                if(!tmp.isListEmpty()) tmp = tmp.lastSubPartition();    // Article
                if(!tmp.isListEmpty()) tmp = tmp.lastSubPartition();    // Paragraph
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
}
