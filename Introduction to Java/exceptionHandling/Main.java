//The try statement allows you to define a block of code to be tested for errors while it is being executed.
//The catch statement allows you to define a block of code to be executed, if an error occurs in the try block.
//The try and catch keywords come in pairs:
//The finally statement lets you execute code, after try...catch, regardless of the result:

public class Main {
    public static void main(String[] args) {

        try{
            int[] numbers = new int[]{1, 2, 3};
            System.out.println(numbers[5]);
        }catch(Exception exception){
            System.out.println("an error occurred");
        }finally{
            System.out.println("this code is always working");
        }
    }
}
