public enum MesurableUnit {

    KG ("KG", 50, 250, 1.0),
    BAG ("BAG", 15, 25, 0.0),
    BOX ("BOX", 30, 60, 0.0),
    PACK ("PACK", 100, 500, 0.0);

    private final String name;
    private final int minQuantity;
    private final int maxQuantity;
    private final double kgPerUnit;

    MesurableUnit(String name, int minQuantity, int maxQuantity, double kgPerUnit) {
        this.name = name;
        this.minQuantity = minQuantity;
        this.maxQuantity = maxQuantity;
        this.kgPerUnit = kgPerUnit;
    }

    public String getName() {
        return name;
    }

    public int getMinQuantity() {
        return minQuantity;
    }

    public int getMaxQuantity() {
        return maxQuantity;
    }

    public double getKgPerUnit() {
        return kgPerUnit;
    }
}
