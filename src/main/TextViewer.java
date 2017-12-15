
import java.util.LinkedList;

public class TextViewer {
    private LinkedList<TextPartition> partitionsList;
    private LinkedList<TextPartition> articlesList = new LinkedList<>();

    // Everything is self-explanatory

    public TextViewer(LinkedList<TextPartition> newList){
        partitionsList = newList;
        addArticlesToList();
    }

    public String viewHelp(){
        return ("arg1 arg2 arg3 arg4\n" +
                "arg1 - ścieżka do pliku\n" +
                "arg2 - tryb wyświetlania [spis, tresc]\n" +
                "arg3 - obiekt do wyświetlenia [calosc, preambula, dzial, rozdzial, artykul, ustep, punkt, litera]\n" +
                "arg4 - numer/identyfikator obiektu (np. /SCIEZKA/ tresc artykul 105) \n" +
                "W przypadku obiektów których nie można wyświetlić samodzielnie trzeba podać również\n" +
                "numery obiektów, które je zawierają, w kolejności od największego do najmniejszego\n" +
                "np. /SCIEZKA/ tresc punkt \"nrArtykulu nrUstepu nrPunktu\"\n");
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
        String tmp;

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
        String tmp;

        for(TextPartition art : articlesList){
            tmp = art.getPartitionName().toLowerCase().replace("\"", "");
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
        throw new IllegalArgumentException("Artkuł o numerze " + articleNo + " nie został znaleziony.");
    }

    public String viewArticleRange(String articlesNo){
        articlesNo = articlesNo.replace("\"", "");
        String tmp;
        StringBuilder sb = new StringBuilder();
        boolean inRange = false;
        articlesNo = articlesNo.replace('-', ' ');


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
            if(deletedFirstWord(articlesNo).equals(tmp) && !inRange) return "Nieprawidłowy zakres. Artykuł o numerze " + firstWord(articlesNo) + " jest nieprawidłowy lub jest wyższy niż numer drugiego artykułu.\n";
            if(deletedFirstWord(articlesNo).equals(tmp)) inRange = false;
        }
        if(inRange) return "Nieprawidłowy zakres. Artykuł o numerze " + deletedFirstWord(articlesNo) + " nie został znaleziony.\n";
        if(sb.toString().equals("")) return "Nieprawidłowy zakres. Żaden z podanych numerów artykułów nie został znaleziony.\n";
        return sb.toString();
    }

    public String viewActChapter(String chapterNo){
        chapterNo = chapterNo.replace("\"", "").toLowerCase();

        if(deletedFirstWord(chapterNo).equals("")){
            String tmp;
            tmp = "Aby wyświetlić rozdział, musisz podać zarówno numer rozdziału, jak i działu";
            throw new IllegalArgumentException(tmp);
        }

        for(TextPartition section : partitionsList){
            String secName = section.getPartitionName().toLowerCase();

            if(firstWord(chapterNo).equals(firstWord(deletedFirstWord(secName)))){

                for(TextPartition chapter : section.getSubPartitions()){
                    if(deletedFirstWord(chapterNo).equals(firstWord(deletedFirstWord(chapter.getPartitionName().toLowerCase())))){
                        return chapter.toString();
                    }
                }
            }
        }
        throw new IllegalArgumentException("Rozdział o numerze " + deletedFirstWord(chapterNo).toUpperCase() + " w dziale " + firstWord(chapterNo).toUpperCase() + " nie został znaleziony");
    }

    public String viewSection(String sectionNo, StatuteType type){
        String tmp;
        sectionNo = sectionNo.toLowerCase();

        for(TextPartition section : partitionsList){
            if(section.getPartitionName().equals("Preambuła")) continue;

            tmp = section.getPartitionName().toLowerCase(); // "Rozdział V\n[...]"
            tmp = deletedFirstWord(tmp);                    // "V\n[...]"
            tmp = firstWord(tmp);                           // "V"

            if(sectionNo.equals(tmp) || tmp.equals(deletedFirstWord(sectionNo)) || sectionNo.equals(tmp + ".") || deletedFirstWord(sectionNo).equals(tmp + "."))
                return section.toString();
        }
        if(type == StatuteType.Constitution) throw new IllegalArgumentException("Rozdział z takim numerem nie istnieje");
        throw new IllegalArgumentException("Dział z takim numerem nie istnieje");
    }

    public String viewSectionTOC(String sectionNo, StatuteType sType){

        sectionNo = sectionNo.replace("\"", "").toLowerCase();
        StringBuilder sb = new StringBuilder();
        String tmp;
        TextPartition section = null;

        for(TextPartition searchedSection : partitionsList){
            if(sectionNo.equals(firstWord(deletedFirstWord(searchedSection.getPartitionName().toLowerCase())))){
                section = searchedSection;
                break;
            }
        }

        if(section == null && sType == StatuteType.Constitution) throw new IllegalArgumentException("Rozdział o numerze " + sectionNo + " nie został znaleziony.");
        else if(section == null) throw new IllegalArgumentException("Dział o numerze " + sectionNo + " nie został znaleziony.");

        sb.append(section.getPartitionName().replace("\n", ". ").replace("\r", ""));                             // "Section No. "
        sb.append(section.getText());                                                                            // "Section No. [text]"
        sb.append(" (");                                                                                         // "Section No. [text] ("

        TextPartition tmpPartition = section;

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

        return sb.toString();
    }

    public String viewPreAmbule(){
        return partitionsList.getFirst().toString();
    }

    public String viewParagraph(String paragraphNo){                // "artNo paragraphNo"
        paragraphNo = paragraphNo.replace("\"", "").toLowerCase().replace(".", "");
        String tmp;
        TextPartition searchedArticle = null;

        if(deletedFirstWord(paragraphNo).equals("")){
            tmp = "Aby wyświetlić ustęp, musisz podać zarówno numer artykułu, jak i ustępu";
            throw new IllegalArgumentException(tmp);
        }

        for(TextPartition article : articlesList){
            if(deletedFirstWord(article.getPartitionName()).replace(".", "").equals(firstWord(paragraphNo).replace(".", ""))){
                searchedArticle = article;
                break;
            }
        }

        if(searchedArticle == null) throw new IllegalArgumentException("Nie znaleziono artykułu z numerem " + firstWord(paragraphNo));

        for(TextPartition paragraph : searchedArticle.getSubPartitions()){
            if(paragraph.getPartitionName().replace(".", "").replace(" ", "").equals(deletedFirstWord(paragraphNo).replace(".", ""))){
                return paragraph.toString();
            }
        }
        throw new IllegalArgumentException("Nie znaleziono ustępu " + deletedFirstWord(paragraphNo) + " w artykule o numerze " + firstWord(paragraphNo));
    }

    public String viewPoint(String pointPath){
        pointPath = pointPath.replace("\"", "").toLowerCase();
        String tmp;
        TextPartition searchedArticle = null;
        TextPartition searchedParagraph = null;

        if((deletedFirstWord(pointPath)).equals("")){
            tmp = "Aby wyświetlić punkt, musisz podać zarówno numer artykułu, (ustępu,) jak i punktu";
            throw new IllegalArgumentException(tmp);
        }

        for(TextPartition article : articlesList){
            if(deletedFirstWord(article.getPartitionName()).replace(".", "").equals(firstWord(pointPath).replace(".", ""))){
                searchedArticle = article;
                break;
            }
        }

        if(searchedArticle == null) throw new IllegalArgumentException("Nie znaleziono artykułu z numerem " + firstWord(pointPath));

        for(TextPartition paragraph : searchedArticle.getSubPartitions()){
            if(paragraph.getPartitionName().replace(".", "").equals(firstWord(deletedFirstWord(pointPath).replace(".", "")))){
                searchedParagraph = paragraph;
                break;
            }
            if(paragraph.getPartitionType() == PartitionType.Point && paragraph.getPartitionName().replace(")", "").equals(firstWord(deletedFirstWord(pointPath).replace(".", "")))){
                return paragraph.toString();
            }
        }

        if(searchedParagraph == null) throw new IllegalArgumentException("Nie znaleziono ustępu " + firstWord(deletedFirstWord(pointPath)) + " w artykule o numerze " + firstWord(pointPath));

        for(TextPartition point : searchedParagraph.getSubPartitions()){
            if(point.getPartitionName().replace(")", "").equals(deletedFirstWord(deletedFirstWord(pointPath).replace(")", "")))){
                return point.toString();
            }
        }

        throw new IllegalArgumentException("Nie znaleziono punktu " + deletedFirstWord(deletedFirstWord(pointPath)) + " w ustępie " + firstWord(deletedFirstWord(pointPath)) + " w artykule o numerze " + firstWord(pointPath));
    }

    public String viewLetter(String letterPath){

        letterPath = letterPath.replace("\"", "").toLowerCase();
        String tmp;
        TextPartition searchedArticle = null;
        TextPartition searchedParagraph = null;
        TextPartition searchedPoint = null;

        if(deletedFirstWord(deletedFirstWord(letterPath)).equals("")){
            tmp = "Aby wyświetlić literę, musisz podać zarówno numer artykułu, (ustępu,) punktu jak i samej litery";
            throw new IllegalArgumentException(tmp);
        }

        String artName = firstWord(letterPath).replace(".", "");
        String paraName = firstWord(deletedFirstWord(letterPath)).replace(".", "");
        String pointName = firstWord(deletedFirstWord(deletedFirstWord(letterPath))).replace(")", "");
        String letterName = firstWord(deletedFirstWord(deletedFirstWord(deletedFirstWord(letterPath)))).replace(")", "");

        if(deletedFirstWord(deletedFirstWord(deletedFirstWord(letterPath))).equals("")){
            letterName = firstWord(deletedFirstWord(deletedFirstWord(letterPath))).replace(")", "");
        }

        for(TextPartition article : articlesList){
            if(deletedFirstWord(article.getPartitionName()).replace(".", "").equals(artName)){
                searchedArticle = article;
                break;
            }
        }

        if(searchedArticle == null) throw new IllegalArgumentException("Nie znaleziono artykułu z numerem " + firstWord(letterPath));

        for(TextPartition paragraph : searchedArticle.getSubPartitions()){
            if(paragraph.getPartitionName().replace(".", "").equals(paraName)){
                searchedParagraph = paragraph;
                break;
            }
            if(paragraph.getPartitionType() == PartitionType.Point && paragraph.getPartitionName().replace(")", "").equals(firstWord(deletedFirstWord(letterPath).replace(".", "")))){
                searchedPoint = paragraph;
                break;
            }
        }

        if(searchedParagraph == null && searchedPoint == null) throw new IllegalArgumentException("Nie znaleziono ustępu " + firstWord(deletedFirstWord(letterPath)) + " w artykule o numerze " + firstWord(letterPath));

        if(searchedPoint == null){
            for(TextPartition point : searchedParagraph.getSubPartitions()){
                if(point.getPartitionName().replace(")", "").equals(firstWord(deletedFirstWord(deletedFirstWord(letterPath).replace(")", ""))))){
                    searchedPoint = point;
                    break;
                }
            }
        }

        if(searchedPoint == null) throw new IllegalArgumentException("Nie znaleziono punktu " + firstWord(deletedFirstWord(deletedFirstWord(letterPath))) + " w ustępie " + firstWord(deletedFirstWord(letterPath)) + " w artykule o numerze " + firstWord(letterPath));

        for(TextPartition letter : searchedPoint.getSubPartitions()){
            if(letter.getPartitionName().replace(")", "").equals(letterName)){
                return letter.toString();
            }
        }

        throw new IllegalArgumentException("Nie znaleziono litery " + deletedFirstWord(deletedFirstWord(deletedFirstWord(letterPath))) + " w punkcie " + firstWord(deletedFirstWord(deletedFirstWord(letterPath))) + " w ustępie " + firstWord(deletedFirstWord(letterPath)) + " w artykule o numerze " + firstWord(letterPath));
    }

    public String viewActChapterTOC(String chapterNo){
        chapterNo = chapterNo.replace("\"", "").toLowerCase();
        TextPartition desiredChapter = null;
        StringBuilder sb = new StringBuilder();
        String tmp;

        if(deletedFirstWord(chapterNo).equals("")){
            tmp = "Aby wyświetlić rozdział, musisz podać zarówno numer rozdziału, jak i działu";
            throw new IllegalArgumentException(tmp);
        }

        for(TextPartition section : partitionsList){
            String secName = section.getPartitionName().toLowerCase();

            if(firstWord(chapterNo).equals(firstWord(deletedFirstWord(secName)))){
                for(TextPartition chapter : section.getSubPartitions()){
                    if(chapter.getPartitionType() == PartitionType.Chapter && deletedFirstWord(chapterNo).equals(firstWord(deletedFirstWord(chapter.getPartitionName().toLowerCase())))){
                        desiredChapter = chapter;
                        break;
                    }
                }
                break;
            }
        }

        if(desiredChapter == null) throw new IllegalArgumentException("Rozdział o numerze " + deletedFirstWord(chapterNo) + " w dziale " + firstWord(chapterNo).toUpperCase() + " nie został znaleziony");

        tmp = desiredChapter.getPartitionName().replace("\n", " ").replace("\r", "");
        tmp = tmp.substring(0,1).toUpperCase() + tmp.substring(1,tmp.length()).toLowerCase();
        sb.append(tmp);                      //"Chapter No. [text]"
        sb.append(" (");
        sb.append(desiredChapter.firstSubPartition().getPartitionName().substring(0,desiredChapter.firstSubPartition().getPartitionName().length()-1).toLowerCase());
        tmp = desiredChapter.firstSubPartition().getPartitionName().substring(5,desiredChapter.firstSubPartition().getPartitionName().length()-1);
        if(!tmp.equals(desiredChapter.lastSubPartition().getPartitionName().substring(5,desiredChapter.lastSubPartition().getPartitionName().length()-1))){
            sb.append(" - ");
            sb.append(desiredChapter.lastSubPartition().getPartitionName().substring(5,desiredChapter.lastSubPartition().getPartitionName().length()-1));
        }

        sb.append(")\n");
        return sb.toString();
    }

    private void addArticlesToList(){
        for(TextPartition section : partitionsList){
            for(TextPartition subSection : section.getSubPartitions()){
                if(subSection.getPartitionType() == PartitionType.Article){
                    articlesList.add(subSection);
                }
                else{
                    articlesList.addAll(subSection.getSubPartitions());
                }
            }
        }
    }

    private String firstWord(String k){
        String tmp = "";
        int i = 0;

        while(!k.equals("") && k.charAt(0) == ' ') k = k.substring(1,k.length());

        while(k.length()>i && k.charAt(i)!=' '){
            if(k.length()>i+2 && k.substring(i+1, i+2).equals("\n")) break;
            tmp += k.charAt(i);
            i++;
        }

        return tmp;
    }

    private String deletedFirstWord(String k){
        int i = 0;


        while(k.length() > i && k.charAt(i)!=' '){
            if(k.length()>i+1 && k.substring(i+1, i+2).equals("\n")) break;
            i++;
        }

        if(i == k.length()) return "";
        return k.substring(i+1,k.length());
    }
}
