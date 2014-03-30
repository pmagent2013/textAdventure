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

    public Locale getNext() {
        return next;
    }
    public void setNext(Locale next) {
        this.next = next;
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

    public Locale getGoNorth() {
        return goNorth;
    }

    public void setGoNorth(Locale goNorth) {
        this.goNorth = goNorth;
    }

    public Locale getGoSouth() {

        return goSouth;
    }

    public void setGoSouth(Locale goSouth) {
        this.goSouth = goSouth;
    }

    public Locale getGoEast() {

        return goEast;
    }

    public void setGoEast(Locale goEast) {
        this.goEast = goEast;
    }

    public Locale getGoWest() {

        return goWest;
    }

    public void setGoWest(Locale goWest) {
        this.goWest = goWest;
    }

    public Locale getGoToBarren() {
        return goToBarren;
    }

    public void setGoToBarren(Locale goToBarren) {
        this.goToBarren = goToBarren;
    }

    public Locale getGoToAthos() {
        return goToAthos;
    }

    public void setGoToAthos(Locale goToAthos) {
        this.goToAthos = goToAthos;
    }

    public Locale getGoToWratih() {
        return goToWratih;
    }

    public void setGoToWratih(Locale goToWratih) {
        this.goToWratih = goToWratih;
    }

    public Locale getGoToMidway() {
        return goToMidway;
    }

    public void setGoToMidway(Locale goToMidway) {
        this.goToMidway = goToMidway;
    }

    public Locale getGoToEarth() {
        return goToEarth;
    }

    public void setGoToEarth(Locale goToEarth) {
        this.goToEarth = goToEarth;
    }

    public Locale getGoToAtlantis() {
        return goToAtlantis;
    }

    public void setGoToAtlantis(Locale goToAtlantis) {
        this.goToAtlantis = goToAtlantis;
    }

    public Locale getGoToAtlantisJumper() {
        return goToAtlantisJumper;
    }

    public void setGoToAtlantisJumper(Locale goToAtlantisJumper) {
        this.goToAtlantisJumper = goToAtlantisJumper;
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
    private Locale next = null;
    private Locale goNorth = null;
    private Locale goSouth = null;
    private Locale goEast  = null;
    private Locale goWest  = null;
    private Locale goToBarren = null;
    private Locale goToAthos = null;
    private Locale goToWratih  = null;
    private Locale goToMidway  = null;
    private Locale goToEarth  = null;
    private Locale goToAtlantis  = null;
    private Locale goToAtlantisJumper  = null;
}