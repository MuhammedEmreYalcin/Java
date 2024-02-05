// Filelogger inherits Baselogger

public class Filelogger extends Baselogger{
    public void log (String massage)
    {
        System.out.println("Logged to File: "+ massage);
    }
}
