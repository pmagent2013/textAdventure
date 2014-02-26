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

    public String getLookDesc() {
        return this.lookDesc;
    }
    public void setLookDesc(String value) {
        this.lookDesc = value;
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
    private String lookDesc; //an extended description to be displayed on first visit or upon typing look
    private boolean hasVisited = false;
    private boolean hasItem = false;
    private Item whichItem;
}