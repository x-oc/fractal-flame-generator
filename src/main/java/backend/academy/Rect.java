package backend.academy;

public record Rect(double x, double y, double width, double height) {
    boolean contains(Point p) {
        return p.x() >= x && p.x() < x + width && p.y() >= y && p.y() < y + height;
    }
}
