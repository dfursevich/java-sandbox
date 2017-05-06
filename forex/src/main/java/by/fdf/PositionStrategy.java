package by.fdf;

/**
 * @author Dzmitry Fursevich
 */
public interface PositionStrategy {
    boolean open(PriceBar bar);

    boolean close(Position position, PriceBar priceBar);
}
