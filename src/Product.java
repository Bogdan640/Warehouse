import java.util.HashMap;
import java.util.Map;

public class Product {

    private String name;
    private MesurableUnit mesurableUnit;
    private ProductCategory productCategory;
    private String unitDetails;
    private double pricePerUnit;
    private Map<String, String> extraFields;
    private double kgPerUnit;



    public Product(String name, MesurableUnit mesurableUnit, ProductCategory productCategory,
                   String unitDetails, double pricePerUnit) {
        this.name = name;
        this.mesurableUnit = mesurableUnit;
        this.productCategory = productCategory;
        this.unitDetails = unitDetails;
        this.pricePerUnit = pricePerUnit;
        this.extraFields = new HashMap<>();
        this.kgPerUnit = calculateKgPerUnit();
    }

    private double calculateKgPerUnit() {

        if (mesurableUnit == MesurableUnit.KG){
                return 1.0;
        }
        String mesurableUnitDetails = unitDetails.toLowerCase().trim();
        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("([0-9]+\\.?[0-9]*)");
        java.util.regex.Matcher matcher = pattern.matcher(mesurableUnitDetails);

        if (matcher.find()) {
            try {
                return Double.parseDouble(matcher.group(1));
            } catch (NumberFormatException e) {
                System.err.println("The format is not accepted as:  " + unitDetails);
            }
        }

        return 1.0; // Took as default for crackers pack

    }

    public String getName() {
        return name;
    }

    public MesurableUnit getMesurableUnit() {
        return mesurableUnit;
    }

    public ProductCategory getProductCategory() {
        return productCategory;
    }

    public String getUnitDetails() {
        return unitDetails;
    }

    public double getPricePerUnit() {
        return pricePerUnit;
    }

    public Map<String, String> getExtraFields() {
        return extraFields;
    }

    public double getKgPerUnit() {
        return kgPerUnit;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMesurableUnit(MesurableUnit mesurableUnit) {
        this.mesurableUnit = mesurableUnit;
    }

    public void setProductCategory(ProductCategory productCategory) {
        this.productCategory = productCategory;
    }

    public void setUnitDetails(String unitDetails) {
        this.unitDetails = unitDetails;
    }

    public void setPricePerUnit(double pricePerUnit) {
        this.pricePerUnit = pricePerUnit;
    }

    public void setExtraFields(Map<String, String> extraFields) {
        this.extraFields = extraFields;
    }

    public void setKgPerUnit(double kgPerUnit) {
        this.kgPerUnit = kgPerUnit;
    }

}
