package Features;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class FeaturesObject {
    private final Map<String,Feature> features;
    private final String name;
    public FeaturesObject(String name) {
        features = new HashMap<>();
        this.name = name;
    }
    public String getName() {
        return name;
    }
    public void addFeature(Feature feature) {
        features.put(feature.getAttribute(),feature);
    }
    public static double compareFeatures(FeaturesObject f1, FeaturesObject f2) {
        double similarity = 0;
        Set<String> attributes = new HashSet<>(f1.features.keySet());
        attributes.addAll(f2.features.keySet());
        for (String attribute : attributes) {
            Feature feature1 = f1.features.getOrDefault(attribute,new Feature(attribute,-40));
            Feature feature2 = f2.features.getOrDefault(attribute,new Feature(attribute,-40));
            double val1 = feature1.getValue();
            double val2 = feature2.getValue();
            similarity += feature1.getMultiplier() * feature2.getMultiplier() * Math.pow(val1 - val2, 2);
        }
        /*Use scale function to scale between 0-7 */
        return (scale(Math.sqrt(similarity)));
    }
    private static double scale(double x) {
        double k = 0.015;
        return 7 * (Math.exp(-k * x));
    }
    public String toString(){
        StringBuilder result = new StringBuilder(name + "\n");
        for (String feature: features.keySet()){
            result.append(features.get(feature)).append("\n");
        }
        return result.toString();
    }
}
