import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class StoragePackage {

    private Product product;
    private int quantity; //number of units in the package
    private LocalDate entryDate;
    private LocalDate expirationDate;


    private double currentPrice;
    private int discountPercentage;
    private boolean thrownAway;
    private String priceStatus;


    public void setCurrentPrice(double currentPrice) {
        this.currentPrice = currentPrice;
    }

    public void setDiscountPercentage(int discountPercentage) {
        this.discountPercentage = discountPercentage;
    }

    public void setThrownAway(boolean thrownAway) {
        this.thrownAway = thrownAway;
    }

    public void setPriceStatus(String priceStatus) {
        this.priceStatus = priceStatus;
    }

    public StoragePackage(Product product, int quantity, LocalDate entryDate, LocalDate expirationDate) {
        this.product = product;
        this.quantity = quantity;
        this.entryDate = entryDate;
        this.expirationDate = expirationDate;

    }

    public double getTotalWeight() {
        return product.getKgPerUnit() * quantity;
    }

    public double getOriginalPrice() {
        return product.getPricePerUnit() * quantity;
    }

    public void updatePriceInfo(LocalDate currentDate) {
        long weeksUntilExpiration = ChronoUnit.WEEKS.between(currentDate, expirationDate);

        if (!currentDate.isBefore(expirationDate)) {
            this.currentPrice = 0.0;
            this.discountPercentage = 100;
            this.thrownAway = true;
            this.priceStatus = "Expired - thrown away";
            return;
        }

        ProductCategory category = product.getProductCategory();
        int discountStartWeeks = category.getWeeksBeforeDiscount();

        if (weeksUntilExpiration <= discountStartWeeks && discountStartWeeks > 0) {
            int weeksInDiscount = (int) (discountStartWeeks - weeksUntilExpiration);
            double discountPct = Math.min(100, weeksInDiscount * category.getDicountPercentage() * 100);
            this.discountPercentage = (int) discountPct;
            this.currentPrice = getOriginalPrice() * (1 - discountPct / 100);
            this.thrownAway = false;
            this.priceStatus = String.format("%.0f%% discount (%d weeks before expiration)", discountPct, weeksUntilExpiration);
        } else {
            this.discountPercentage = 0;
            this.currentPrice = getOriginalPrice();
            this.thrownAway = false;
            this.priceStatus = "No discount";
        }
    }





    public Product getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }

    public LocalDate getEntryDate() {
        return entryDate;
    }

    public LocalDate getExpirationDate() {
        return expirationDate;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setEntryDate(LocalDate entryDate) {
        this.entryDate = entryDate;
    }

    public void setExpirationDate(LocalDate expirationDate) {
        this.expirationDate = expirationDate;
    }

    public double getCurrentPrice() {
        return currentPrice;
    }

    public int getDiscountPercentage() {
        return discountPercentage;
    }

    public boolean isThrownAway() {
        return thrownAway;
    }

    public String getPriceStatus() {
        return priceStatus;
    }
}

