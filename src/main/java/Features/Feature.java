package Features;

public class Feature {
    private final String attribute;
    private final double value;
    public Feature(String attribute, double value) {
        this.attribute = attribute;
        this.value = value;
    }
    public String getAttribute() {
        return attribute;
    }
    public double getValue(){
        return value;
    }
    public double getMultiplier(){
        return 1;
    }
    public String toString(){
        return attribute + ": " + value;
    }
}
