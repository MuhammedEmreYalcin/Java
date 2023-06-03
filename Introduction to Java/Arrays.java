import java.util.ArrayList;

public class Main
{
    public static void main(String[] args)
    {
        String[] cars = {"Mercedes", "BMW", "Ford", "Volkswagen"};
        System.out.println(cars[0]); // Mercedes

        double [] mylist = {1.3,1.9,2.5,6.8,3.1};
        double total = 0;
        double max = mylist[0];

        for (double list:mylist) //For-Each Loop
        {
            if (max<list)
            {
                max = list;
            }
            total = total+list;
            System.out.println(list); // 1.3,1.9,2.5,6.8,3.1
        }
        System.out.println("Total:"+total); // Toplam: 15.6
        System.out.println("Greatest number:"+max); //En buyuk Sayi: 6.8


        // ArrayList
        ArrayList<String> citys = new ArrayList<String>();
        citys.add("Ankara");
        citys.add("İstanbul");
        for (String city : citys)
        {
            System.out.println(city); // Ankara, Istanbul
        }

        citys.remove(0);

        for (String city : citys)
        {
            System.out.println(city); // Istanbul
        }
    }
}
