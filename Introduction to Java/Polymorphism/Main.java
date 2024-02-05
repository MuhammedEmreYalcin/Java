// Polymorphism means "many forms", and it occurs when we have many classes that are related to each other by inheritance.
// Polymorphism uses those methods to perform different tasks. This allows us to perform a single action in different ways.

public class Main
{
    public static void main(String[] args)
    {
/*        EmailLogger emailLogger = new EmailLogger();
        emailLogger.Log ("Logg Mesajı");*/

        CustomerManager customerManager = new CustomerManager(new Filelogger());
        customerManager.add();

    }
}
