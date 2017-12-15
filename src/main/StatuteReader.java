
public class StatuteReader {

    public static void main(String[] arguments){
        boolean canStart = true;
        Parser parser = new Parser();

        try{
            parser.parse(arguments);
        } catch(IllegalArgumentException ex){
            System.out.println(ex.toString());
            canStart = false;
        }

        TextCutter textCutter = new TextCutter(parser.getFileType());

        try{
            if(parser.getScanner() != null) textCutter.cutPackAdd(parser.getScanner());
        }catch(IllegalArgumentException ex){
            System.out.println(ex.toString());
            canStart = false;
        }

        TextViewer textViewer = new TextViewer(textCutter.getPartitionsList());

        boolean contentMode;

        try{
            if(canStart){
                if(arguments.length < 3) throw new IllegalArgumentException("Za mało argumentów" + textViewer.viewHelp());
                String mode = arguments[1].replace("\"", "");

                if(mode.equals("-s") || mode.equals("-spis") || mode.equals("spis") || mode.equals("s")) contentMode = false;
                else if(mode.equals("-t") || mode.equals("-tresc") || mode.equals("t") || mode.equals("-treść") || mode.equals("treść") || mode.equals("tresc")) contentMode = true;
                else throw new IllegalArgumentException("Niepoprawny argument trybu działania");


                switch(arguments[2].replace("\"", "")){
                    case "-a": case "-art": case "-artykul": case "-artykuł": case "a": case "art": case "artykul": case "artykuł":{
                        if(arguments.length < 4) throw new IllegalArgumentException("Za mało argumentów");
                        if(contentMode) System.out.println(textViewer.viewArticle(arguments[3]));
                        else throw new IllegalArgumentException("Niemożliwe jest wyświetlenie spisu treści artykułu");
                        break;
                    }
                    case "-w": case "-calosc": case "-całość": case "w": case "calosc": case "całość":{
                        if(contentMode) System.out.println(textViewer.viewWhole());
                        else System.out.println(textViewer.viewTableOfContent());
                        break;
                    }
                    case "-r": case "-rozdzial": case "-rozdział": case "r": case "rozdzial": case "rozdział":{
                        if(arguments.length < 4) throw new IllegalArgumentException("Za mało argumentów");
                        if(contentMode && parser.getFileType() == StatuteType.Constitution) System.out.println(textViewer.viewSection(arguments[3], StatuteType.Constitution));
                        else if(contentMode) System.out.println(textViewer.viewActChapter(arguments[3]));
                        else if(parser.getFileType() == StatuteType.Constitution) System.out.println(textViewer.viewSectionTOC(arguments[3], parser.getFileType()));
                        else System.out.println(textViewer.viewActChapterTOC(arguments[3]));
                        break;
                    }
                    case "-dział": case "-d": case "-dzial": case "dzial": case "dział": case "d":{
                        if(parser.getFileType() == StatuteType.Constitution) throw new IllegalArgumentException("Konstytucja nie jest podzielona na działy");
                        if(contentMode) System.out.print(textViewer.viewSection(arguments[3],StatuteType.Act));
                        else System.out.print(textViewer.viewSectionTOC(arguments[3],parser.getFileType()));
                        break;
                    }
                    case "-artykuły": case "-artykuly": case "-zakres": case "artykuly": case "zakres": case "artykuły":{
                        if(arguments.length < 4) throw new IllegalArgumentException("Za mało argumentów");
                        if(contentMode) System.out.print(textViewer.viewArticleRange(arguments[3]));
                        else throw new IllegalArgumentException("Niemożliwe jest wyświetlenie spisu treści zakresu artykułów");
                        break;
                    }
                    case "-ustęp": case "-ustep": case "-u": case "u": case "ustęp": case "ustep":{
                        if(arguments.length < 4) throw new IllegalArgumentException("Za mało argumentów");
                        if(contentMode) System.out.print(textViewer.viewParagraph(arguments[3]));
                        else throw new IllegalArgumentException("Niemożliwe jest wyświetlenie spisu treści ustępu");
                        break;
                    }
                    case "-p": case "-punkt": case "p": case "punkt":{
                        if(arguments.length < 4) throw new IllegalArgumentException("Za mało argumentów");
                        if(contentMode) System.out.print(textViewer.viewPoint(arguments[3]));
                        else throw new IllegalArgumentException("Niemożliwe jest wyświetlenie spisu treści punktu");
                        break;
                    }
                    case "-l": case "-litera": case "litera": case "l":{
                        if(arguments.length < 4) throw new IllegalArgumentException("Za mało argumentów");
                        if(parser.getFileType() == StatuteType.Constitution) throw new IllegalArgumentException("W konstytucji w podpunktach nie występują litery");
                        if(contentMode) System.out.println(textViewer.viewLetter(arguments[3]));
                        else throw new IllegalArgumentException("Niemożliwe jest wyświetlenie spisu treści litery");
                        break;
                    }
                    case "preambula": case "preambuła":{
                        if(contentMode && parser.getFileType() == StatuteType.Constitution) System.out.println(textViewer.viewPreAmbule());
                        else if(parser.getFileType() == StatuteType.Constitution) throw new IllegalArgumentException("Niemożliwe jest wyświetlenie spisu treści preambuły");
                        else throw new IllegalArgumentException("Ustawa nie zawiera preambuły");
                        break;
                    }
                    default:{
                        throw new IllegalArgumentException("Niepoprawny argument treści, jaką chce się wyświetlić");
                    }
                }
            } else {
                System.out.println(textViewer.viewHelp());
            }
        }catch(IllegalArgumentException ex){
            System.out.println(ex.toString());
            System.out.println(textViewer.viewHelp());
        }
    }
}
