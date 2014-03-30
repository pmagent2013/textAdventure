public class LocaleList {

    //
    // Public
    //

    // Constructor
    public LocaleList() {
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

    public Locale getHead() {
        return head;
    }
    public void setHead(Locale head) {
        this.head = head;
    }

    public Locale getLast() {
        return last;
    }
    public void setLast(Locale last) {
        this.last = last;
    }

    public Locale getCurrent() {
        return current;
    }

    public void setCurrent(Locale current) {
        this.current = current;
    }
    // Other methods


    public void add(Locale locale){
        //if list is empty
        if(this.head == null){
            this.setHead(locale);
            this.last = locale;
        }
        //list is not empty
        //move to end of list
        else{
            this.last.setNext(locale);
            this.last = locale;
        }
    }


    //
    // Private
    //
    private String name;
    private String desc;
    private Locale head;
    private Locale last;
    private Locale current;

}