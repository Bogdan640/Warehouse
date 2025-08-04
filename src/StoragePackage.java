import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class StoragePackage {

    private Product product;
    private int quantity; //number of units in the package
    private LocalDate entryDate;
    private LocalDate expirationDate;



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


    public PriceInfo getCurrentPrice(LocalDate currentDate){
        int weeksUntilExpiration = (int) ChronoUnit.WEEKS.between(currentDate, expirationDate);
        if (currentDate.isAfter(expirationDate) || currentDate.isEqual(expirationDate)) {
            return new PriceInfo(0.0, 100, true);
        }

        ProductCategory productCategory = product.getProductCategory();
        int weeksForDiscount = productCategory.getWeeksForDiscount();

        if(weeksUntilExpiration <= weeksForDiscount && weeksUntilExpiration >0 && weeksForDiscount > 0){
            int weeksInDiscount = weeksForDiscount - weeksUntilExpiration;
            double discountPercentage = productCategory.getDicountPercentage();
            double originalPrice = getOriginalPrice();
            double discountFactor = Math.pow(1-discountPercentage, weeksInDiscount);
            double finalPrice = originalPrice * discountFactor;

            return new PriceInfo(finalPrice, (int)(discountPercentage*100), false);

        }
        return new PriceInfo(getOriginalPrice(), 0, false);
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

    @Override
    public String toString() {
        return String.format("%s: %d %s (Entry: %s, Expires: %s)",
                product.getName(), quantity, product.getMesurableUnit().getName(),
                entryDate, expirationDate);
    }

}

