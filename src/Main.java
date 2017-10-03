import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static ArrayList<String> erlaubeZeichenImMittelTeil;

    //TIMO GEHT NICHT XD ÖL ÖDE ÖLEN
    public static void main(String[] args) {
        ArrayList<String> kennzeichen = liesKennzeichenEin();
        ArrayList<String> testDaten = liesTestDatenEin();
        erlaubeZeichenImMittelTeil = liesDatenVonFile("erlaubteZeichenImMittelTeil.txt");
        System.out.println(testeKennzeichen("rofl", kennzeichen, 0, false, null).get(0).equals("TRUE"));
        A:
        for (String s : testDaten) {
            ArrayList<String> zeichen = testeKennzeichen(s, kennzeichen, 0, false, null);
            System.out.println(s + ": " + zeichen.get(0).equals("TRUE"));

            if (!zeichen.get(0).equals("TRUE")) {
                continue A;
            }
            for (int i = zeichen.size() - 2; i > 0; i -= 2) {
                System.out.print(zeichen.get(i + 1) + "-" + zeichen.get(i) + "-000 ");
            }
            System.out.println("");
        }
        //Scanner s= new Scanner(System.in);
        //String input= s.next();
    }

    public static ArrayList<String> liesKennzeichenEin() {
        return liesDatenVonFile("kuerzelliste.txt");
    }

    public static ArrayList<String> liesTestDatenEin() {
        return liesDatenVonFile("autoscrabble.txt");
    }

    public static ArrayList<String> testeKennzeichen(String s, ArrayList<String> kennzeichen, int position, boolean istImMittelTeil, ArrayList<String> einzelneKennZeichen) {
        if (einzelneKennZeichen == null) {
            einzelneKennZeichen = new ArrayList<String>();
            einzelneKennZeichen.add("TRUE");
        }
        if (!einzelneKennZeichen.get(0).equals("TRUE")) {
            einzelneKennZeichen.clear();
            einzelneKennZeichen.add("TRUE");
        }
        //System.out.println("Kennzeichen Aufrufen");//DEBUG
        if (position >= s.length()) {
            if (istImMittelTeil) {
                einzelneKennZeichen.remove(0);
                einzelneKennZeichen.add(0, "FALSE");
                return einzelneKennZeichen;
            } else {
                return einzelneKennZeichen;
            }
        } else if (!istImMittelTeil) {
            //System.out.println("Vorderteil:");//DEBUG
            boolean char1 = false;
            boolean char2 = false;
            boolean char3 = false;
            String b = "" + s.charAt(position);
            //System.out.println("Mit einem char: "+b);//DEBUG
            //System.out.println("Ist erlaubt: "+kennzeichen.contains(b));//DEBUG
            if (kennzeichen.contains(b)) {
                char1 = testeKennzeichen(s, kennzeichen, position + 1, true, einzelneKennZeichen).get(0).equals("TRUE");
                //System.out.println("True?: "+char1);//DEBUG
            }
            if (char1) {
                einzelneKennZeichen.add("" + b);
                return einzelneKennZeichen;
            }
            if (position + 1 == s.length()) {
                einzelneKennZeichen.remove(0);
                einzelneKennZeichen.add(0, "FALSE");
                return einzelneKennZeichen;
            }
            b = s.substring(position, position + 2);
            //System.out.println("Zwei char: "+b.length());//DEBUG
            //System.out.println("Mit zwei chars: "+b);//DEBUG
            //System.out.println("Ist erlaubt: "+kennzeichen.contains(b));//DEBUG
            if (kennzeichen.contains(b)) {
                char2 = testeKennzeichen(s, kennzeichen, position + 2, true, einzelneKennZeichen).get(0).equals("TRUE");
                //System.out.println("True?: "+char2);//DEBUG
            }
            if (char2) {
                einzelneKennZeichen.add("" + b);
                return einzelneKennZeichen;
            }
            if (position + 2 == s.length()) {
                einzelneKennZeichen.remove(0);
                einzelneKennZeichen.add(0, "FALSE");
                return einzelneKennZeichen;
            }
            b = s.substring(position, position + 3);
            //System.out.println("Mit drei chars: "+b);//DEBUG
            //System.out.println("Ist erlaubt: "+kennzeichen.contains(b));//DEBUG
            if (kennzeichen.contains(b)) {
                char3 = testeKennzeichen(s, kennzeichen, position + 3, true, einzelneKennZeichen).get(0).equals("TRUE");
                //System.out.println("True?: "+char3);//DEBUG
            }
            if (char3) {
                einzelneKennZeichen.add("" + b);
                return einzelneKennZeichen;
            } else {
                einzelneKennZeichen.remove(0);
                einzelneKennZeichen.add(0, "FALSE");
                return einzelneKennZeichen;
            }


        } else {
            //System.out.println("Mittelteil:");//DEBUG
            boolean char1 = false;
            boolean char2 = false;
            boolean erlaubechar1 = erlaubtImMittelTeil("" + s.charAt(position));
            //System.out.println("Char: "+s.charAt(position)+" erlaubt: "+ erlaubechar1);//DEBUG
            boolean erlaubechar2 = false;
            if (position + 1 < s.length()) {
                erlaubechar2 = erlaubtImMittelTeil(s.substring(position, position + 2));
                //System.out.println("Chars: "+s.substring(position,position+2)+" erlaubt: "+ erlaubechar2);//DEBUG
            }

            if (erlaubechar1) {
                char1 = testeKennzeichen(s, kennzeichen, position + 1, false, einzelneKennZeichen).get(0).equals("TRUE");
                if (char1) {
                    einzelneKennZeichen.add("" + s.charAt(position));
                    return einzelneKennZeichen;
                }
            }
            if (erlaubechar2) {
                char2 = testeKennzeichen(s, kennzeichen, position + 2, false, einzelneKennZeichen).get(0).equals("TRUE");
                if (char2) {
                    einzelneKennZeichen.add("" + s.substring(position, position + 2));
                }
            }
            if (!(char1 || char2)) {
                einzelneKennZeichen.remove(0);
                einzelneKennZeichen.add(0, "FALSE");
            }
            return einzelneKennZeichen;
        }
    }

    public static boolean erlaubtImMittelTeil(String s) {
        for (char c : s.toCharArray()) {
            if (!erlaubeZeichenImMittelTeil.contains("" + c)) {
                return false;
            }
        }
        return true;
    }

    public static ArrayList<String> liesDatenVonFile(String path) {
        try {
            BufferedReader in = new BufferedReader(new FileReader(path));

            ArrayList<String> daten = new ArrayList<String>();
            String line = in.readLine();
            line = line.substring(1);//Weil der erste Charakter immer so ein ' ist aus Gründen

            while (line != null) {
                daten.add(line);
                //System.out.println(line);//DEBUG
                line = in.readLine();
            }
            in.close();
            return daten;
        } catch (Exception e) {
            System.out.println("no such file");
            System.exit(0);
        }
        return null;
    }

}