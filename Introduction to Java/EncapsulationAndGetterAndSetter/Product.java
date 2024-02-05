public class Product
{
    // Encapsulation
    private int id;
    private String name;
    private String description;
    private double price;
    private int stockAmount;
    private String Kod;

    // Getter
    public int getId()
    {
        return id;
    }

    // Setter
    public void setId(int id)
    {
        this.id = id;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public double getPrice()
    {
        return price;
    }

    public void setPrice(double price)
    {
        this.price = price;
    }

    public int getStockAmount()
    {
        return stockAmount;
    }

    public void setStockAmount(int stockAmount)
    {
        this.stockAmount = stockAmount;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getKod()
    {
        return this.name.substring(0,2) + id;
    }

}
