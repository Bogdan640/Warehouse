import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

public class Warehouse {


    private List<StoragePackage>  storagePackages;
    private List <Product> availableProducts;

    public Warehouse() {
        this.storagePackages = new ArrayList<>();
        this.availableProducts =createAvailableProducts();
    }


    private List<Product> createAvailableProducts() {
        List<Product> products = new ArrayList<>();

        products.add(new Product("Apples", MesurableUnit.KG , ProductCategory.FRUITS, "1 kg", 6.0));
        products.add(new Product("Potatoes",  MesurableUnit.BAG, ProductCategory.VEGETABLES,"10 kg", 15.0));
        products.add(new Product("Onions",  MesurableUnit.BAG, ProductCategory.VEGETABLES,"4 kg", 2.5));
        products.add(new Product("Peaches",  MesurableUnit.BOX, ProductCategory.FRUITS,"6 kg", 30.0));
        products.add(new Product("Oranges",  MesurableUnit.BAG, ProductCategory.FRUITS, "2.5 kg", 13.0));
        products.add(new Product("Crackers",  MesurableUnit.PACK, ProductCategory.OTHERS, "1 pack", 2.0));

        return products;
    }

    public void generateRandomPackages(int count ){


        Random random = new Random();
        LocalDate date = LocalDate.now().minusMonths(3);
        for (int i=1; i<=count; i++) {
            Product product = availableProducts.get(random.nextInt(availableProducts.size()));
            MesurableUnit mesurableUnit = product.getMesurableUnit();


            int quantity = random.nextInt(mesurableUnit.getMaxQuantity() - mesurableUnit.getMinQuantity() +1) + mesurableUnit.getMinQuantity() ;
            LocalDate entryDate = date.plusDays(random.nextInt(180));
            LocalDate expirationDate = entryDate.plusDays(30 + random.nextInt(150));
            StoragePackage storagePackage = new StoragePackage(product, quantity, entryDate, expirationDate);
            storagePackages.add(storagePackage);

        }
    }

    public void saveToFile(String filename) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            writer.write("WAREHOUSE DATA");
            writer.newLine();
            writer.write("PATTERN: ProductName|Category|MeasurableUnit|UnitDetails|PricePerUnit|Quantity|EntryDate|ExpirationDate|ExtraFields");
            writer.newLine();
            writer.write("\n");

            for (StoragePackage pkg : storagePackages) {
                writer.write(pkg.toFileString());
                writer.newLine();
            }
        }
        System.out.println("Warehouse saved to " + filename + " (" + storagePackages.size() +" packages)");
    }


    public void loadFromFile(String filename) throws IOException {

        storagePackages.clear();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            int lineNumber = 0;

            while ((line = reader.readLine()) != null) {
                lineNumber++;
                line = line.trim();

                if (line.isEmpty() || line.startsWith("WAREHOUSE DATA") || line.startsWith("PATTERN:")) {
                    continue;
                }
                try {
                    StoragePackage pkg = StoragePackage.fromFileString(line);
                    storagePackages.add(pkg);
                } catch (Exception e) {
                    System.err.println("Line " + lineNumber + "is invalid with error message: " + e.getMessage());
                }
            }
        }
        System.out.println("Warehouse loaded from " + filename + " (" + storagePackages.size() + " packages)");
    }



    public void printSummary(LocalDate date){

        System.out.print("WAREHOUSE SUMMARY ON " + date + ":\n");

        Map<ProductCategory, List<StoragePackage>> packagesByCategory = storagePackages.stream()
                .collect(Collectors.groupingBy(pkg -> pkg.getProduct().getProductCategory()));

        ///Group by product category

        for (ProductCategory productCategory : ProductCategory.values()) {
            List<StoragePackage> categoryPackages = packagesByCategory.getOrDefault(productCategory, new ArrayList<>());
            if (categoryPackages.isEmpty()) {
                continue;
            }
            double totalWeight = 0;
            double totalOriginalPrice = 0;
            double totalCurrentPrice = 0;


            /// Group by product name
            Map<String, List<StoragePackage>> packagesByProductName = categoryPackages.stream()
                    .collect(Collectors.groupingBy(pkg -> pkg.getProduct().getName()));

            System.out.println("\n" + productCategory.getName() + ":\n");


            /// go through each product in the category with the selected name
            for(Map.Entry<String, List<StoragePackage>> entry : packagesByProductName.entrySet()) {
                String productName = entry.getKey();
                List<StoragePackage> productPackages = entry.getValue();


                double productWeight = 0;
                double productOriginalPrice = 0;
                double productCurrentPrice = 0;
                int productTotalUnits = 0;
                int discountedPackages = 0;
                double avgDiscountPercentage = 0;
                int totalDiscountedItems= 0;

                for (StoragePackage pkg : productPackages) {
                    productWeight += pkg.getTotalWeight();
                    productOriginalPrice += pkg.getOriginalPrice();
                    productTotalUnits += pkg.getQuantity();
                    PriceInfo priceInfo = pkg.getCurrentPrice(date);

                    if (!priceInfo.isThrownAway()) {
                        productCurrentPrice += priceInfo.getCurrentPrice();

                        if (priceInfo.getDiscountPercentage() > 0) {
                            discountedPackages++;
                            avgDiscountPercentage += priceInfo.getDiscountPercentage();
                        }

                    }
                }

                if (discountedPackages > 0) {
                    avgDiscountPercentage /= discountedPackages;
                    totalDiscountedItems += discountedPackages;

                }

                System.out.printf("    %s: %d units (%.1f kg), Unit Price: $%.2f, Total Price: $%.2f",
                        productName, productTotalUnits, productWeight,
                        productPackages.get(0).getProduct().getPricePerUnit(), productCurrentPrice);

                if (discountedPackages > 0) {
                    double savedAmount = productOriginalPrice - productCurrentPrice;
                    System.out.printf(", Discount: %.0f%% ($%.2f )", avgDiscountPercentage, savedAmount);
                }
                System.out.println();

                totalWeight += productWeight;
                totalOriginalPrice += productOriginalPrice;
                totalCurrentPrice += productCurrentPrice;
            }

            System.out.printf("  Category Total: %.1f kg, Current Value: $%.2f", totalWeight, totalCurrentPrice);
        }

    }

    public List<StoragePackage> getStoragePackages() {
        return storagePackages;
    }

}
