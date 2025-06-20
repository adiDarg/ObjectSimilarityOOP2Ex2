package Features;

public class ImportantFeature extends Feature{

    public ImportantFeature(String attribute, double value) {
        super(attribute, value);
    }

    @Override
    public double getMultiplier() {
        return 1.25;
    }

    public String toString(){
        return "Important: " + super.toString();
    }
}
