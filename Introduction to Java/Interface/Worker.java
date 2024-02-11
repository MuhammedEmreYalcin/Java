// Worker class implements IWorkable, IEatable, IPayable (means, that we can use funktions from IWorkable, IEatable, IPayable, but we should override it because the function is empty)

public class Worker implements IWorkable, IEatable, IPayable {

    @Override
    public void work() {

    }

    @Override
    public void eat() {

    }
}
