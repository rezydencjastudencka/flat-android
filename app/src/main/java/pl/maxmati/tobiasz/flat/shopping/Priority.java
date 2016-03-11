package pl.maxmati.tobiasz.flat.shopping;

import pl.maxmati.tobiasz.flat.R;

/**
 * Created by mmos on 03.03.16.
 *
 * @author mmos
 */
public class Priority {
    private final int id;
    private final String name;
    private final int colorResId;

    public static final int PRIORITY_LOW = 0;
    public static final int PRIORITY_NORMAL = 1;
    public static final int PRIORITY_HIGH = 2;

    public static Priority[] getPredefined() {
        return new Priority[] {
                new Priority(PRIORITY_LOW, "Low priority", R.color.priorityLow),
                new Priority(PRIORITY_NORMAL, "Normal priority", R.color.priorityNormal),
                new Priority(PRIORITY_HIGH, "High priority", R.color.priorityHigh)
        };
    }

    public Priority(int id, String name, int colorResId) {
        this.id = id;
        this.name = name;
        this.colorResId = colorResId;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getColorResId() {
        return colorResId;
    }

    @Override
    public String toString() {
        return getName();
    }
}
