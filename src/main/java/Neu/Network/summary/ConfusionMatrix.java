package Neu.Network.summary;

public class ConfusionMatrix {
    private final int flowerSpecies;
    private double truePositives = 0.0;
    private double trueNegatives = 0.0;
    private double falsePositives = 0.0;
    private double falseNegatives = 0.0;

    public ConfusionMatrix(int flowerSpecies) {
        this.flowerSpecies = flowerSpecies;
    }

    public double precision() {
        return (this.truePositives)/(this.truePositives + this.falsePositives);
    }

    public double getAccuracy() {
        return ((this.truePositives + this.falseNegatives) / (this.truePositives + this.trueNegatives + this.falsePositives + this.falseNegatives));
    }

    public double recall() {
        return this.truePositives /(this.truePositives + this.trueNegatives);
    }

    public double fMeasure(double precision, double recall) {
        return 2*precision*recall/(precision+recall);
    }

    public void classificationLogic(double prediction, double actual) {
        // true, false -> actual condition
        // positive, negative -> predicion condition
        boolean predictionCondition = prediction == this.flowerSpecies;
        boolean actualCondition = actual == this.flowerSpecies;

        if (predictionCondition && actualCondition) this.truePositives++;
        if (!predictionCondition && actualCondition) this.trueNegatives++;
        if (predictionCondition && !actualCondition) this.falsePositives++;
        if (!predictionCondition && !actualCondition) this.falseNegatives++;
    }

    public void getInformation() {
        System.out.println("==================================================="
                + "\nSpecies: " + flowerSpecies
                + "\nPrecision: " + precision()
                + "\nAccuracy: " + getAccuracy()
                + "\nRecall: " + recall()
                + "\nF-Measure: " + fMeasure(precision(), recall())
                + "\n===================================================" );
    }

    public double getTruePositives() {
        return truePositives;
    }
}
