public class MagicItemsList {

    //
    // Public
    //

    // Constructor
    public MagicItemsList() {
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

    public ListItem getHead() {
        return head;
    }
    public void setHead(ListItem head) {
        this.head = head;
    }

    // Other methods
    @Override
    public String toString() {
        String retVal = new String();
        retVal = " name=" + this.name + " desc=" + this.desc + "\n";
        ListItem currentItem = this.head;
        while (currentItem != null) {
            retVal = retVal + "   " + currentItem.toString() + "\n";
            currentItem = currentItem.getNext();
        }
        return retVal;
    }

    public String Shop() {
        String retVal = new String();
        ListItem currentItem = this.head;
        while (currentItem != null) {
            retVal = retVal + "   " + currentItem.purchaseMenu() + "\n";
            currentItem = currentItem.getNext();
        }
        return retVal;
    }

    public void add(ListItem item){
        //if list is empty
        if(this.head == null){
            this.setHead(item);
        }
        //list is not empty
        //move to end of list
        else{
            ListItem lastItem = this.head;
            while (lastItem.getNext() != null) {
                lastItem = lastItem.getNext();
            }
            //attach the passed in item to end of list
            lastItem.setNext(item);


        }
    }


    //
    // Private
    //
    private String name;
    private String desc;
    private ListItem head;

}
