public enum ProductCategory {



    FRUITS ("Fruits", 5, 0.10),
    VEGETABLES ("Vegetables", 4, 0.05),
    OTHERS ("Others", 0, 0.00);

    private final String name;
    private final int weeksBeforeDiscount;
    private final double discountPercentage;



    ProductCategory(String name, int weeksBeforeDiscount, double dicountPercentage) {
        this.name = name;
        this.weeksBeforeDiscount = weeksBeforeDiscount;
        this.discountPercentage = dicountPercentage;
    }


    public String getName() {
        return name;
    }

    public int getWeeksBeforeDiscount() {
        return weeksBeforeDiscount;
    }

    public double getDicountPercentage() {
        return discountPercentage;
    }
}
