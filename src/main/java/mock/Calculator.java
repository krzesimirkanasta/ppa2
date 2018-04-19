package mock;

/**
 * Created by pwilkin on 19-Apr-18.
 */
public class Calculator {

    protected double state = 0.0;

    public double add(double value) {
        state += value;
        return state;
    }

    public void reset() {
        state = 0.0;
    }

}
