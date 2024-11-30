package backend.academy.transformations;

public class TransformationFactory {

    public static final String TRANSFORM_NAMES =
        "disc, heart, horseshoe, linear, popcorn, sinusoidal, spherical, swirl";

    private TransformationFactory() {

    }

    public static Transformation getTransformation(String transformationName) {
        return switch (transformationName) {
            case "disc" -> new Disc();
            case "heart" -> new Heart();
            case "horseshoe" -> new Horseshoe();
            case "linear" -> new Linear();
            case "popcorn" -> new Popcorn();
            case "sinusoidal" -> new Sinusoidal();
            case "spherical" -> new Spherical();
            case "swirl" -> new Swirl();
            default -> throw new IllegalArgumentException(transformationName);
        };
    }
}
