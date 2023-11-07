import java.util.Locale;

public class Main {
    public static void main(String[] args) {
        String mesaj = "Bugun hava cok güzel";
        System.out.println(mesaj.charAt(4)); // 4. Indexteki harfi bulur
        System.out.println(mesaj.concat(" Yaşasın")); //Mesajın sonuna Yaşasını ekler
        System.out.println(mesaj.startsWith("B")); //mesaj B len başlıyorsa true başlamıyorsa false döner
        System.out.println(mesaj.endsWith("B")); //mesaj B len bitiyorsa true bitmiyorsa false döner
        char [] karakterler = new char[5];
        mesaj.getChars(0,5,karakterler,0); // Mesajın 0-5 kadar olan karakterlerini yeni bir Arraye atar
        System.out.println(karakterler);
        System.out.println(mesaj.indexOf("a")); //Mesajdaki a harfin indexini gösterir baştan başlar
        System.out.println(mesaj.lastIndexOf("b")); //Mesajdaki b harfin indexini gösterir sondan başlar
        String yenimesaj = mesaj.replace(" ","-"); //Boşlukları - len değiştirir
        System.out.println(yenimesaj); //Bugun-hava-cok-güzel
        System.out.println(mesaj.substring(2,5)); // Mesajı 2. Indexten itibaren 5.Indexe kadar alır (gun)
        for(String kelime:mesaj.split(" ")){ //Mesajı boşluklara göre bölür
            System.out.println(kelime);
        }
        System.out.println(mesaj.toLowerCase()); // Bütün harfleri küçük yazar
        System.out.println(mesaj.toUpperCase()); // Bütün harfleri büyük yazar
        System.out.println(mesaj.trim()); // mesajın başındakı ve sonundaki boşlukları siler

}
