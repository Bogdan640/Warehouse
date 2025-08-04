public class PriceInfo {

    private double currentPrice;
    private int discountPercentage;
    private boolean thrownAway;


    public PriceInfo(double currentPrice, int discountPercentage, boolean thrownAway) {
        this.currentPrice = currentPrice;
        this.discountPercentage = discountPercentage;
        this.thrownAway = thrownAway;
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

    public void setCurrentPrice(double currentPrice) {
        this.currentPrice = currentPrice;
    }

    public void setDiscountPercentage(int discountPercentage) {
        this.discountPercentage = discountPercentage;
    }

    public void setThrownAway(boolean thrownAway) {
        this.thrownAway = thrownAway;
    }
}
