public class Main
{
    public static void main(String[] args)
    {
        // For Loop
        for (int i=1; i<=10; i++){
            System.out.println(i+"."+" For Loop");
        }

        // For-each Loop (We use it for Arrays)
        String[] names = {"Ali","Hasan","Max"};
        for(String i : names){
            System.out.println(i);
        }

        // While Loop
        int i = 1;
        while(i<=10){
            System.out.println(i+"."+" While Loop");
            i++;
        }

        // Do-While Loop
        int j = 1;
        do{
            System.out.println(j+"."+" Do-While Loop");
            j++;
        }while(j<=10);
    }
}
