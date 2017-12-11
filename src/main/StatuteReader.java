
import java.util.Scanner;

public class StatuteReader {

    public static void main(String[] arguments){
        boolean q = false;
        Parser parser = new Parser();
        parser.parse(arguments);
/*
        int skippedLines;
        if(parser.getFileType() == StatuteType.Act) skippedLines = 2;
        else skippedLines = 44;
        for(int i = 0; i < skippedLines ; i++){
            parser.getScanner().nextLine();
        }*/

        TextCutter textCutter = new TextCutter(parser.getFileType());

        try{
            textCutter.cutPackAdd(parser.getScanner());
        }catch(IllegalArgumentException ex){
            System.out.println(ex.toString());
            q = true;
        }


        TextViewer textViewer = new TextViewer(textCutter.getPartitionsList());

        System.out.println(textViewer.viewHelp(parser.getFileType()));

        Scanner s1;

        while(!q){
            String lastLine;
            String tmp = "";
            System.out.print("/");
            s1 = new Scanner(System.in);
            lastLine = s1.next();
            lastLine = lastLine.toLowerCase();

            System.out.println();

            switch(lastLine.toLowerCase()){
                case "q": case "quit":{
                    q = true;
                    break;
                }

                case "h": case "help":{
                    System.out.println(textViewer.viewHelp(parser.getFileType()));
                    break;
                }

                case "w": case "whole": case "whole text":{
                    System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
                    System.out.print(textViewer.viewWhole());
                    System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
                    break;
                }

                case "t": case "table": case "table of content": case "content":{
                    System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
                    System.out.print(textViewer.viewTableOfContent());
                    System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
                    break;
                }

                case "a": case "art": case "art.": case "artykuł":{
                    boolean isSingle = false;

                    while(true){
                        System.out.println("Wyświetlić `pojedynczy` artykuł czy `zakres` artykułów?\n");
                        System.out.print("/artykuł/");
                        s1 = new Scanner(System.in);
                        tmp = s1.nextLine();
                        tmp = tmp.toLowerCase();
                        if(tmp.equals("zakres") || tmp.equals("pojedynczy")) break;
                    }

                    if(tmp.equals("pojedynczy")) isSingle = true;

                    while(true){
                        if(isSingle) {
                            System.out.println("\nWprowadź numer artykułu\n" + "lub napisz `e` żeby wrócić\n");
                            System.out.print("/artykuł/pojedynczy/");
                        }
                        else {
                            System.out.println("\nWprowadź numer pierwszego i ostatniego artykułu, które mają się znaleźć w zakresie, oddzielając te numery spacją\n" + "lub napisz `e` żeby wrócić\n");
                            System.out.print("/artykuł/zakres/");
                        }
                        s1 = new Scanner(System.in);
                        tmp = s1.nextLine();
                        tmp = tmp.toLowerCase();
                        if(tmp.equals("exit") ||
                                tmp.equals("e")    ||
                                tmp.equals("q")    ||
                                tmp.equals("quit")) break;
                        System.out.println();
                        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
                        if(isSingle) System.out.print(textViewer.viewArticle(tmp));
                        else System.out.print(textViewer.viewArticleRange(tmp));
                        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
                    }
                    break;
                }

                case "s": case "dział":{
                    while(true){
                        if(parser.getFileType() == StatuteType.Constitution) {
                            System.out.println("Wprowadź numer rozdziału\n" + "lub napisz `e` żeby wrócić\n");
                            System.out.print("/rozdział/");
                        }
                        else {
                            System.out.println("Wprowadź numer działu\n" + "lub napisz `e` żeby wrócić\n");
                            System.out.print("/dział/");
                        }

                        s1 = new Scanner(System.in);
                        tmp = s1.nextLine();
                        tmp = tmp.toLowerCase();
                        if(tmp.equals("exit") || tmp.equals("e") || tmp.equals("q") || tmp.equals("quit")) break;
                        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
                        System.out.print(textViewer.viewSection(tmp, parser.getFileType()));
                        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
                    }

                    break;
                }
                case "p": case "preambuła":{
                    if(parser.getFileType() == StatuteType.Constitution) {
                        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
                        System.out.print(textViewer.viewPreAmbule());
                        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
                    }
                    break;
                }
                case "r": case "losowy":{
                    System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
                    System.out.print(textViewer.viewRandomArticle());
                    System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
                    break;
                }


            }
        }

    }
}
