public class Item{

        //
        // -- PUBLIC --
        //

        // Constructor
        public Item(int id) {
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

        public boolean getHasPicked() {
            return hasPicked;
        }
        public void setHasPicked(boolean hasVisited) {
            this.hasPicked = hasPicked;
        }


        // Other methods
        public String toString() {
            return "[Item object: id=" + this.id + " name="+ this.name + " desc=" + this.desc + "]";
        }


//
// -- PRIVATE --
//
private int     id;
private String  name;
private String  desc;
private boolean hasPicked = false;




}
