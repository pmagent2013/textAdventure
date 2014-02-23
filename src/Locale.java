public class Locale {

    //
    // -- PUBLIC --
    //

    // Constructor
    public Locale(int id) {
        this.id = id;
    }

    // Getters and Setters
    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }
    public void setName(String value) {
        this.name = value;
    }

    public String getDesc() {
        return this.desc;
    }
    public void setDesc(String value) {
        this.desc = value;
    }

    public boolean getHasVisited() {
        return hasVisited;
    }
    public void setHasVisited(boolean hasVisited) {
        this.hasVisited = hasVisited;
    }

    public boolean getHasItem() {
        return hasItem;
    }
    public void setHasItem(boolean hasItem) {
        this.hasItem = hasItem;
    }

    public Item getWhichItem() {
        return whichItem;
    }
    public void setWhichItem(Item whichItem) {
        this.whichItem = whichItem;
    }

    // Other methods
    public String toString() {
        return "[Locale object: id=" + this.id + " name="+ this.name + " desc=" + this.desc + "]";
    }


    //
    // -- PRIVATE --
    //
    private int     id;
    private String  name;
    private String  desc;
    private boolean hasVisited = false;
    private boolean hasItem = false;
    private Item whichItem;
}