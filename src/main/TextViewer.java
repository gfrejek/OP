import java.util.LinkedList;
import java.util.Random;
import java.util.Scanner;

public class TextViewer {
    private LinkedList<TextPartition> partitionsList;
    private LinkedList<TextPartition> articlesList = new LinkedList<TextPartition>();

    public TextViewer(LinkedList<TextPartition> newList){
        partitionsList = newList;
        addArticlesToList();
    }

    public String viewHelp(StatuteType type){
        StringBuilder sb = new StringBuilder();

        sb.append("Napisz `h` aby zobaczyć dostępne komendy");
        sb.append(System.lineSeparator());

        sb.append("Napisz `t` aby zobaczyć spis treści");
        sb.append(System.lineSeparator());

        if(type == StatuteType.Constitution) {
            sb.append("Napisz `p` aby zobaczyć preambułę");
            sb.append(System.lineSeparator());
        }

        sb.append("Napisz `s` aby wybrać ");
        if(type == StatuteType.Act) sb.append("dział, który chcesz wyświetlić");
        else sb.append("rozdział, który chcesz wyświetlić");
        sb.append(System.lineSeparator());

        sb.append("Napisz `c` aby wybrać ");
        if(type == StatuteType.Act) sb.append("rozdział, który chcesz wyświetlić");
        else sb.append("podrozdział, który chcesz wyświetlić");
        sb.append(System.lineSeparator());

        sb.append("Napisz `a` aby wybrać artykuł, który chcesz wyświetlić");
        sb.append(System.lineSeparator());

        sb.append("Napisz `r` aby wyświetlić losowy artykuł");
        sb.append(System.lineSeparator());

        sb.append("Napisz `q` aby wyjść");
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
            if(section.getPartitionName().equals("Preambuła")){
                sb.append(section.getPartitionName());
                sb.append("\n");
                continue;
            }
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



            for(TextPartition subSection : section.getSubPartitions()){
                if(subSection.getPartitionType() == PartitionType.Chapter) {
                    sb.append("    ");
                    tmp = subSection.getPartitionName().replace("\n", " ").replace("\r", "");
                    tmp = tmp.substring(0,1).toUpperCase() + tmp.substring(1,tmp.length()).toLowerCase();
                    sb.append(tmp);                      //"Chapter No. [text]"
                    sb.append(" (");
                    sb.append(subSection.firstSubPartition().getPartitionName().substring(0,subSection.firstSubPartition().getPartitionName().length()-1).toLowerCase());
                    tmp = subSection.firstSubPartition().getPartitionName().substring(5,subSection.firstSubPartition().getPartitionName().length()-1);
                    if(!tmp.equals(subSection.lastSubPartition().getPartitionName().substring(5,subSection.lastSubPartition().getPartitionName().length()-1))){
                        sb.append(" - ");
                        sb.append(subSection.lastSubPartition().getPartitionName().substring(5,subSection.lastSubPartition().getPartitionName().length()-1));
                    }

                    sb.append(")\n");
                }
            }
        }

        return sb.toString();
    }

    public String viewArticle(String articleNo){
        String tmp = "";

        for(TextPartition art : articlesList){
            tmp = art.getPartitionName().toLowerCase();
            if(articleNo.equals(tmp) ||
               articleNo.equals("art. " + tmp.substring(5,tmp.length()-1))    ||
               articleNo.equals("art " + tmp.substring(5,tmp.length()))       ||
               articleNo.equals("art " + tmp.substring(5,tmp.length()-1))     ||
               articleNo.equals("artykuł " + tmp.substring(5,tmp.length()))   ||
               articleNo.equals("artykuł " + tmp.substring(5,tmp.length()-1)) ||
               articleNo.equals(tmp.substring(5,tmp.length()))                ||
               articleNo.equals(tmp.substring(5,tmp.length()-1)))
                return art.toString();
        }
        return "Artykuł z takim numerem nie istnieje";

    }

    public String viewArticleRange(String articlesNo){
        String tmp = "";
        StringBuilder sb = new StringBuilder();
        boolean inRange = false;


        for(TextPartition art : articlesList){
            if(inRange) {
                sb.append(art.toString());
                sb.append(System.lineSeparator());
            }
            tmp = deletedFirstWord(art.getPartitionName());
            tmp = tmp.substring(0,tmp.length()-1);

            if(firstWord(articlesNo).equals(tmp)) {
                inRange = true;
                sb.append(art.toString());
                sb.append(System.lineSeparator());
            }
            if(deletedFirstWord(articlesNo).equals(tmp) && !inRange) return "Wrong scope. First number is incorrect or it is higher than second number\n";
            if(deletedFirstWord(articlesNo).equals(tmp)) inRange = false;
        }
        if(inRange) return "Wrong scope. Second article couldn't be found\n";
        if(sb.toString().equals("")) return "Wrong scope. Neither first nor second number could be found\n";
        return sb.toString();
    }

    //public void viewChapter(String chapterNo);

    //public void viewChapterRange(String firstChapter, String lastChapter);

    public String viewSection(String sectionNo, StatuteType type){
        String tmp = "";
        sectionNo = sectionNo.toLowerCase();

        for(TextPartition section : partitionsList){
            if(section.getPartitionName().equals("Preambuła")) continue;

            tmp = section.getPartitionName().toLowerCase(); // "Rozdział V\n[...]"
            tmp = deletedFirstWord(tmp);                    // "V\n[...]"
            tmp = firstWord(tmp);                           // "V"

            if(sectionNo.equals(tmp) || tmp.equals(deletedFirstWord(sectionNo)) || sectionNo.equals(tmp + ".") || deletedFirstWord(sectionNo).equals(tmp + "."))
                return section.toString();
        }
        if(type == StatuteType.Constitution) return "Rozdział z takim numerem nie istnieje";
        return "Dział z takim numerem nie istnieje";  //TODO exception?
    }

    //public void viewSectionRange(String firstSection, String lastSection);

    public String viewPreAmbule(){
        return partitionsList.getFirst().toString();
    }

    public String viewRandomArticle(){
        Random randomGenerator = new Random();
        int randomInt = randomGenerator.nextInt(articlesList.size());

        return articlesList.get(randomInt).toString();
    }

    private void addArticlesToList(){
        for(TextPartition section : partitionsList){
            for(TextPartition subSection : section.getSubPartitions()){
                if(subSection.getPartitionType() == PartitionType.Article){
                    articlesList.add(subSection);
                }
                else{
                    for(TextPartition article : subSection.getSubPartitions()){
                        articlesList.add(article);
                    }
                }
            }
        }
    }

    private String firstWord(String k){
        String tmp = "";
        int i = 0;

        while(k.length()>i && k.charAt(i)!=' ' && !k.substring(i+1,i+2).equals("\n")){
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
}
