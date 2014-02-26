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
    public Double getCost() {
        return cost;
    }
    public void setCost(Double cost) {
        this.cost = cost;
    }
    // Other methods
    @Override
    public String toString() {
        return " name=" + this.name + " desc=" + this.desc;
    }


    //
    // Private
    //

    private String name;
    private String desc;
    private Double cost;
    private ListItem next = null;

}
