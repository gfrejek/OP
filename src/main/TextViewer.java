import java.util.LinkedList;
import java.util.Scanner;

public class TextViewer {
    private LinkedList<TextPartition> partitionsList;

    public TextViewer(LinkedList<TextPartition> newList){
        partitionsList = newList;
    }

    public String viewHelp(StatuteType type){
        StringBuilder sb = new StringBuilder();

        sb.append("Type 'h' to see help");
        sb.append(System.lineSeparator());
        sb.append("Type 't' to see table of content");
        if(type == StatuteType.Constitution) {
            sb.append(System.lineSeparator());
            sb.append("Type 'p' to see preamble");
        }
        sb.append(System.lineSeparator());
        sb.append("Type 's' to choose section(s) you wish to see");
        sb.append(System.lineSeparator());
        sb.append("Type 'c' to choose chapter(s) you wish to see");
        sb.append(System.lineSeparator());
        sb.append("Type 'a' to choose article(s) you wish to see");
        sb.append(System.lineSeparator());
        sb.append("Type 'q' to quit");
        sb.append(System.lineSeparator());

        return sb.toString();
    }

    public String viewWhole(){
        StringBuilder sb = new StringBuilder();

        for(TextPartition node : partitionsList){
            sb.append(node.toString());
            sb.append(System.lineSeparator());
        }

        return sb.toString();
    }

    public String viewTableOfContent(){
        StringBuilder sb = new StringBuilder();
        String tmp = "";

        for(TextPartition section : partitionsList){
            TextPartition tmpPartition = section;

            sb.append(section.getPartitionName().replace("\n", ". ").replace("\r", ""));                             // "Section No. "
            sb.append(section.getText());                                                                            // "Section No. [text]"
            sb.append(" (");                                                                                         // "Section No. [text] ("

            if((tmpPartition.firstSubPartition().getPartitionType() == PartitionType.Chapter)) tmpPartition = tmpPartition.firstSubPartition();
            if((tmpPartition.firstSubPartition().getPartitionType() == PartitionType.Article)) tmpPartition = tmpPartition.firstSubPartition();

            sb.append(tmpPartition.getPartitionName().substring(0,tmpPartition.getPartitionName().length()-1).toLowerCase());      // "Section No. [text] (Art. No"
            tmp = tmpPartition.getPartitionName().substring(5,tmpPartition.getPartitionName().length()-1);           // tmp = "No"

            tmpPartition = section;
            if((tmpPartition.lastSubPartition().getPartitionType() == PartitionType.Chapter)) tmpPartition = tmpPartition.lastSubPartition();
            if((tmpPartition.lastSubPartition().getPartitionType() == PartitionType.Article)) tmpPartition = tmpPartition.lastSubPartition();
            if(!tmpPartition.getPartitionName().substring(5, tmpPartition.getPartitionName().length()-1).equals(tmp)){
                sb.append(" - ");                                                                                    // "Section No. [text] (Art. No. - "
                sb.append(tmpPartition.getPartitionName().substring(5, tmpPartition.getPartitionName().length()-1)); // "Section No. [text] (Art. No - Art. No"
            }
            sb.append(")\n");                                                                                        // "Section No. [text] (Art. No. - Art. No.)\n"



            for(TextPartition chapter : section.getSubPartitions()){
                if(chapter.getPartitionType() == PartitionType.Chapter) {
                    sb.append("    ");
                    tmp = chapter.getPartitionName().replace("\n", " ").replace("\r", "");
                    tmp = tmp.substring(0,1).toUpperCase() + tmp.substring(1,tmp.length()).toLowerCase();
                    sb.append(tmp);                      //"Chapter No. [text]"
                    sb.append(" (");
                    sb.append(chapter.firstSubPartition().getPartitionName().substring(0,chapter.firstSubPartition().getPartitionName().length()-1).toLowerCase());
                    tmp = chapter.firstSubPartition().getPartitionName().substring(5,chapter.firstSubPartition().getPartitionName().length()-1);
                    if(!tmp.equals(chapter.lastSubPartition().getPartitionName().substring(5,chapter.lastSubPartition().getPartitionName().length()-1))){
                        sb.append(" - ");
                        sb.append(chapter.lastSubPartition().getPartitionName().substring(5,chapter.lastSubPartition().getPartitionName().length()-1));
                    }

                    sb.append(")\n");
                }
            }
        }

        return sb.toString();
    }

    //public void viewArticle(String articleNo);
    //public void viewArticleRange(String firstArticle, String lastArticle);
    //public void viewChapter(String chapterNo);
    //public void viewChapterRange(String firstChapter, String lastChapter);
    //public void viewSection(String sectionNo);
    //public void viewSectionRange(String firstSection, String lastSection);
    //public void viewPreAmbule();
}
