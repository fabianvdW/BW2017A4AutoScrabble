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

        A:
        for (String s : testDaten) {
            ArrayList<String> zeichen = testeKennzeichen(s, kennzeichen, 0, false, null);
            System.out.println(s + ": " + zeichen.get(0).equals("TRUE"));

            if (!zeichen.get(0).equals("TRUE")) {
                // Ist dass nicht eine Endlosschleife?
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

    /**
     * @return eingelesene Daten von den erlaubten Zeichen für das 1. Segment
     */
    public static ArrayList<String> liesKennzeichenEin() {
        return liesDatenVonFile("kuerzelliste.txt");
    }

    /**
     * @return eingelesene Daten von den Test-Wörtern
     */
    public static ArrayList<String> liesTestDatenEin() {
        return liesDatenVonFile("autoscrabble.txt");
    }

    /**
     * Hier passiert die Magic in rekursiven Aufrufen
     *
     * @param s                   Das zu untersuchende Wort
     * @param kennzeichen         Liste der gültigen Kennzeichen im 1. Segment
     * @param position            aktuelle Position der Überprüfung, ab welcher Stelle im String s untersucht werden soll
     * @param istImMittelTeil     Gibt an, ob untersucht werden soll, ob dies eine Möglichkeit für das 2. Segment ist.
     * @param einzelneKennZeichen Liste des aktuellen Fortschritts mit den Zwischenergebnissen
     * @return
     */
    public static ArrayList<String> testeKennzeichen(String s, ArrayList<String> kennzeichen, int position, boolean istImMittelTeil, ArrayList<String> einzelneKennZeichen) {
        if (einzelneKennZeichen == null) {
            einzelneKennZeichen = new ArrayList<String>();
            einzelneKennZeichen.add("TRUE");
        }
        if (!einzelneKennZeichen.get(0).equals("TRUE")) {
            einzelneKennZeichen.clear();
            einzelneKennZeichen.add("TRUE");
        }

        if (position >= s.length()) {
            if (istImMittelTeil) {
                einzelneKennZeichen.set(0, "FALSE");
            }
        } else if (!istImMittelTeil) {
            //Überprüfung des 1. Segments

            // Mit der Länge 1 (min. Länge)
            String b = "" + s.charAt(position);
            if (kennzeichen.contains(b)) {
                if (testeKennzeichen(s, kennzeichen, position + 1, true, einzelneKennZeichen).get(0).equals("TRUE")) {
                    einzelneKennZeichen.add("" + b);
                    return einzelneKennZeichen;
                }
            }
            if (position + 1 == s.length()) {
                einzelneKennZeichen.set(0, "FALSE");
                return einzelneKennZeichen;
            }

            // Mit der Länge 2
            b = s.substring(position, position + 2);
            if (kennzeichen.contains(b)) {
                if (testeKennzeichen(s, kennzeichen, position + 2, true, einzelneKennZeichen).get(0).equals("TRUE")) {
                    einzelneKennZeichen.add("" + b);
                    return einzelneKennZeichen;
                }
            }
            if (position + 2 == s.length()) {
                einzelneKennZeichen.set(0, "FALSE");
                return einzelneKennZeichen;
            }

            // Mit der Länge 3 (max. Länge)
            b = s.substring(position, position + 3);
            if (kennzeichen.contains(b)) {
                if (testeKennzeichen(s, kennzeichen, position + 3, true, einzelneKennZeichen).get(0).equals("TRUE")) {
                    einzelneKennZeichen.add("" + b);
                } else {
                    einzelneKennZeichen.set(0, "FALSE");
                }
            }
        } else {
            // Überprüfung des 2. Segments

            boolean char1 = false;
            boolean char2 = false;

            // Der Länge 1
            if (erlaubtImMittelTeil("" + s.charAt(position))) {
                char1 = testeKennzeichen(s, kennzeichen, position + 1, false, einzelneKennZeichen).get(0).equals("TRUE");
                if (char1) {
                    einzelneKennZeichen.add("" + s.charAt(position));
                }
            }

            // Der Länge 2
            if (position + 1 < s.length()) {
                if (erlaubtImMittelTeil(s.substring(position, position + 2))) {
                    char2 = testeKennzeichen(s, kennzeichen, position + 2, false, einzelneKennZeichen).get(0).equals("TRUE");
                    if (char2) {
                        einzelneKennZeichen.add("" + s.substring(position, position + 2));
                    }
                }
            }

            // Falls weder noch möglich sind
            if (!(char1 || char2)) {
                einzelneKennZeichen.set(0, "FALSE");
            }
        }
        return einzelneKennZeichen;
    }

    /**
     * Whitelist zum Erkennen von ungültigen Zeichen
     *
     * @param s der zu untersuchende String
     * @return ist erlaubt im Mittelteil
     */
    public static boolean erlaubtImMittelTeil(String s) {
        for (char c : s.toCharArray()) {
            if (!erlaubeZeichenImMittelTeil.contains("" + c)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Liest Dateien aus
     *
     * @param path der Pfad zur Datei
     * @return eingelesene Datei als Liste von Strings (für jede Zeile ein String)
     */
    public static ArrayList<String> liesDatenVonFile(String path) {
        try {
            BufferedReader in = new BufferedReader(new FileReader(path));

            ArrayList<String> daten = new ArrayList<String>();
            String line = in.readLine();
            //Weil der erste Charakter immer so ein ' ist aus Gründen
            line = line.substring(1);

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
