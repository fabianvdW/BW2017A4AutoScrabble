public class Main {
    public static void main(String[] args) {
        String[] kennzeichen=leseKennzeichenEin();
        String[] testDaten=leseTestDatenEin();
        System.out.println("TIMO: "+ testKennzeichen("TIMO"));
        for(String s: testDaten){
            System.out.println(s+": "+testKennzeichen(s));
        }
    }
    public static String[] leseKennzeichenEin(){
        return null;
    }
    public static  String[] leseTestDatenEin(){
        return null;
    }
    public static boolean testKennzeichen(String s){
        return false;
    }
}
