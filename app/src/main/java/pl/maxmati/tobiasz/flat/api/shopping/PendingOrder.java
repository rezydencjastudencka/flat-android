package pl.maxmati.tobiasz.flat.api.shopping;

import java.util.Date;

/**
 * Created by mmos on 27.02.16.
 *
 * @author mmos
 */
public class PendingOrder {
    private final int id;
    private final Date creationDate;
    private final int user;
    private final String name;
    private final int priority;
    private final String description;
    private final Double price;
    private final double quantity;

    public PendingOrder(int id, Date creationDate, int user, String name, int priority,
                        String description, Double price, double quantity) {
        this.id = id;
        this.creationDate = creationDate;
        this.user = user;
        this.name = name;
        this.priority = priority;
        this.description = description;
        this.price = price;
        this.quantity = quantity;
    }

    public int getId() {
        return id;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public int getUser() {
        return user;
    }

    public String getName() {
        return name;
    }

    public int getPriority() {
        return priority;
    }

    public String getDescription() {
        return description;
    }

    public Double getPrice() {
        return price;
    }

    public double getQuantity() {
        return quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PendingOrder that = (PendingOrder) o;

        if (id != that.id) return false;
        if (user != that.user) return false;
        if (priority != that.priority) return false;
        if (Double.compare(that.quantity, quantity) != 0) return false;
        if (creationDate != null ? !creationDate.equals(that.creationDate) :
                that.creationDate != null)
            return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (description != null ? !description.equals(that.description) : that.description != null)
            return false;
        return !(price != null ? !price.equals(that.price) : that.price != null);

    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = id;
        result = 31 * result + (creationDate != null ? creationDate.hashCode() : 0);
        result = 31 * result + user;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + priority;
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (price != null ? price.hashCode() : 0);
        temp = Double.doubleToLongBits(quantity);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "PendingOrder{" +
                "id=" + id +
                ", creationDate=" + creationDate +
                ", user=" + user +
                ", name='" + name + '\'' +
                ", priority=" + priority +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", quantity=" + quantity +
                '}';
    }
}
