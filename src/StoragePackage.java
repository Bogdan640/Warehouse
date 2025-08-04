import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Map;

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

    public String toFileString() {
        StringBuilder sb = new StringBuilder();
        Product product = this.product;

        // Pattern:  ProductName|Category|MeasurableUnit|UnitDetails|PricePerUnit|Quantity|EntryDate|ExpirationDate|ExtraFields
        sb.append(product.getName()).append("|")
                .append(product.getProductCategory().name()).append("|")
                .append(product.getMesurableUnit().name()).append("|")
                .append(product.getUnitDetails()).append("|")
                .append(String.format("%.2f", product.getPricePerUnit())).append("|")
                .append(this.quantity).append("|")
                .append(this.entryDate).append("|")
                .append(this.expirationDate).append("|");

        StringBuilder extraFields = new StringBuilder();
        for (Map.Entry<String, String> entry : product.getExtraFields().entrySet()) {
            if (extraFields.length() > 0)
                extraFields.append(",");
            extraFields.append(entry.getKey()).append(":").append(entry.getValue());
        }
        sb.append(extraFields.toString());

        return sb.toString();
    }


    public static StoragePackage fromFileString(String line) {
        String parts[] = line.split("\\|");
        if (parts.length < 8) {
            throw new IllegalArgumentException("Invalid file format: " + line);
        }

        String name = parts[0];
        ProductCategory productCategory = ProductCategory.valueOf(parts[1]);
        MesurableUnit unit = MesurableUnit.valueOf(parts[2]);
        String unitDetails = parts[3];
        double pricePerUnit = Double.parseDouble(parts[4]);

        Product product = new Product(name, unit, productCategory, unitDetails, pricePerUnit);

        int quantity = Integer.parseInt(parts[5]);
        LocalDate entryDate = LocalDate.parse(parts[6]);
        LocalDate expirationDate = LocalDate.parse(parts[7]);

        if (parts.length > 8 && !parts[8].isEmpty()) {
            String[] extraFieldPairs = parts[8].split(",");
            for (String pair : extraFieldPairs) {
                String[] keyValue = pair.split(":");
                if (keyValue.length == 2) {
                    product.getExtraFields().put(keyValue[0], keyValue[1]);
                }
            }
        }

        return new StoragePackage(product, quantity, entryDate, expirationDate);
    }

}

