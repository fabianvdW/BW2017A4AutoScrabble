import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    //TIMO GEHT NICHT XD ÖL ÖDE ÖLEN
    public static void main(String[] args) {
        ArrayList<String> kennzeichen=liesKennzeichenEin();
        ArrayList<String> testDaten=liesTestDatenEin();
        System.out.println(testeKennzeichen("HERRMÜLLER",kennzeichen,0,false));
        for(String s: testDaten){
            System.out.println(s+": "+testeKennzeichen(s,kennzeichen,0,false));
        }
        //Scanner s= new Scanner(System.in);
        //String input= s.next();
    }
    public static ArrayList<String> liesKennzeichenEin(){
        return liesDatenVonFile("kuerzelliste.txt");
    }
    public static  ArrayList<String> liesTestDatenEin(){
        return liesDatenVonFile("autoscrabble.txt");
    }
    public static boolean testeKennzeichen(String s,ArrayList<String> kennzeichen,int position,boolean istImMittelTeil){
        if(position>=s.length()){
            return true;
        }else if(!istImMittelTeil){
            boolean char1=false;
            boolean char2=false;
            boolean char3=false;
            String b=""+s.charAt(position);
            if(kennzeichen.contains(b)){
                char1= testeKennzeichen(s,kennzeichen,position+1,true);
            }
            if(char1) return true;
            b=s.substring(position,position+1);
            if(kennzeichen.contains(b)){
                char2= testeKennzeichen(s,kennzeichen,position+2,true);
            }
            if(char2)return true;
            b=s.substring(position,position+2);
            if(kennzeichen.contains(b)){
                char3= testeKennzeichen(s,kennzeichen,position+3,true);
            }
            return char3;


        }else{
            return testeKennzeichen(s,kennzeichen,position+1,false)|| testeKennzeichen(s,kennzeichen,position+2,false);
        }
    }
    public static ArrayList<String> liesDatenVonFile(String path){
        try {
            BufferedReader in = new BufferedReader(new FileReader(path));
            
            ArrayList<String> daten= new ArrayList<String>();
            String line = in.readLine();
            line=line.substring(1);//Weil der erste Charakter immer so ein ' ist aus Gründen

            while(line  != null)
            {
                daten.add(line);
                //System.out.println(line);//DEBUG
                line=in.readLine();
            }
            in.close();
            return daten;
        }catch(Exception e){
            System.out.println("no such file");
            System.exit(0);
        }
        return null;
    }

}
