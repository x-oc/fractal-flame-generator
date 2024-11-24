package backend.academy.model;

public record FractalImage(Pixel[][] data, int width, int height) {

    public static FractalImage create(int width, int height) {
        Pixel[][] pixels = new Pixel[width][height];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                pixels[x][y] = new Pixel(new Color(0, 0, 0), 0);
            }
        }
        return new FractalImage(pixels, width, height);
    }

    public boolean contains(int x, int y) {
        return x < width && y < height && data[x][y] != null;
    }

    public Pixel pixel(int x, int y) {
        if (!contains(x, y)) return null;
        return data[x][y];
    }
}
