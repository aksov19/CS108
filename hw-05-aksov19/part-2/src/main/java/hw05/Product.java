package hw05;

public class Product {
    private final String id;
    private final String name;
    private final String imageFile;
    private final double price;

    public Product(String id, String name, String imageFile, double price) {
        this.id = id;
        this.name = name;
        this.imageFile = "store-images/" + imageFile;
        this.price = price;
    }

    public double getPrice() {
        return price;
    }

    public String getId() {
        return id;
    }

    public String getImageFile() {
        return imageFile;
    }

    public String getName() {
        return name;
    }
}
