import org.junit.Test;

import java.util.Scanner;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TextCutterTest{
    private StatuteType type;
    @Test


    public void test(){

        type = StatuteType.Constitution;

        assertEquals(assignType("Rozdział I"), PartitionType.Section);
        assertEquals(assignType("DZIAŁ I"), PartitionType.Chapter);
        assertEquals(assignType("RZECZPOSPOLITA"), PartitionType.Chapter);
        assertEquals(assignType("Rozdział 1"), PartitionType.Section);
        assertEquals(assignType("Art. 5."), PartitionType.Article);
        assertEquals(assignType("1. Abcdefgh"), PartitionType.Paragraph);
        assertEquals(assignType("5a. Abcdefgh"), PartitionType.Paragraph);
        assertEquals(assignType("5a) Abcdefgh"), PartitionType.Point);
        assertEquals(assignType("a) Abcdefgh"), PartitionType.Text);
        assertEquals(assignType("5sdffsdfds"), PartitionType.Trash);
        assertEquals(assignType("@Kancelaria Sejmu"), PartitionType.Trash2);
        assertEquals(assignType("K"), PartitionType.Trash);



        type = StatuteType.Act;

        assertEquals(assignType("Rozdział I"), PartitionType.Chapter);
        assertEquals(assignType("DZIAŁ I"), PartitionType.Section);
        assertEquals(assignType("RZECZPOSPOLITA"), PartitionType.Text);
        assertEquals(assignType("Rozdział 1"), PartitionType.Chapter);
        assertEquals(assignType("Art. 5."), PartitionType.Article);
        assertEquals(assignType("1. Abcdefgh"), PartitionType.Paragraph);
        assertEquals(assignType("5a. Abcdefgh"), PartitionType.Paragraph);
        assertEquals(assignType("5a) Abcdefgh"), PartitionType.Point);
        assertEquals(assignType("a) Abcdefgh"), PartitionType.Subpoint);
        assertEquals(assignType("5sdffsdfds"), PartitionType.Trash);
        assertEquals(assignType("@Kancelaria Sejmu"), PartitionType.Trash2);
        assertEquals(assignType("K"), PartitionType.Trash);

        assertEquals(firstWord("Abc def ghi"), "Abc");
        assertEquals(firstWord("Abc. def ghi"), "Abc.");
        assertEquals(firstWord("abc def ghi"), "abc");
        assertEquals(firstWord("abc. def ghi"), "abc.");
        assertEquals(firstWord("Art. 1."), "Art.");
        assertEquals(firstWord("1."), "1.");



        assertEquals(deletedFirstWord("Abc def ghi"), "def ghi");
        assertEquals(deletedFirstWord("Abc. def ghi"), "def ghi");
        assertEquals(deletedFirstWord("Abcdef ghi"), "ghi");
        assertEquals(deletedFirstWord("Abcdefghi"), "");



        String testString = "zobowiązani, by przekazać przyszłym pokoleniom wszystko, co cenne" + System.lineSeparator() +
        "pragnąc na zawsze zagwarantować prawa obywatelskie, a działaniu instytucji pu-" + System.lineSeparator() +
        "blicznych zapewnić rzetelność i sprawność,";

        Scanner testScanner = new Scanner(testString);

        /*while(testScanner.hasNextLine()){

        }*/

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

}
