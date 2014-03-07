public class OffWorld extends Locale{


    //
    // Public
    //

    // Constructor
    public OffWorld(int id){
        super(id);
    }

    public String getInhabitants() {
        return inhabitants;
    }
    public void setInhabitants(String inhabitants) {
        this.inhabitants = inhabitants;
    }

    public int getNumberOfInhabitants() {
        return numberOfInhabitants;
    }
    public void setNumberOfInhabitants(int numberOfInhabitants) {
        this.numberOfInhabitants = numberOfInhabitants;
    }
    //
    // Private
    //

    private String inhabitants;
    private int numberOfInhabitants;


}
