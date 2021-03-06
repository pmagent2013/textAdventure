public class ListItem {

    //
    // Public
    //

    // Constructor
    public ListItem(){
    }

    // Getters and Setters
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }
    public void setDesc(String desc) {
        this.desc = desc;
    }

    public ListItem getNext() {
        return next;
    }
    public void setNext(ListItem next) {
        this.next = next;
    }

    public double getCost() {
        return cost;
    }
    public void setCost(double cost) {
        this.cost = cost;
    }

    public int getPower() {
        return power;
    }
    public void setPower(int power) {
        this.power = power;
    }

    // Other methods
    @Override
    public String toString() {
        return " name=" + this.name + " desc=" + this.desc + " cost=" + this.cost;
    }

    public String purchaseMenu() {
        return this.name + " Cost: " + this.cost + " coins";
    }


    //
    // Private
    //

    private String name;
    private String desc;
    private double cost;
    private int power;
    private ListItem next = null;

}
