package Features;

public class LessImportantFeature extends Feature {
    public LessImportantFeature(String attribute, double value) {
        super(attribute, value);
    }

    @Override
    public double getMultiplier() {
        return 0.8;
    }

    public String toString(){
        return "Less Important: " + super.toString();
    }
}
