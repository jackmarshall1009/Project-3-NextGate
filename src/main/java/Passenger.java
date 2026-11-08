public class Passenger {
    private String name;
    private String flightNumber;
    private int queueNumber;
    private boolean isPriority;
    private String screeningStatus;

    public Passenger(String n, String f, int q, boolean p){
        name = n;
        flightNumber = f;
        queueNumber = q;
        isPriority = p;
    }

    public String getName(){
        return name;
    }

    public String getFlightnumber(){
        return flightNumber;
    }

    public int getQueueNumber(){
        return queueNumber;
    }

    public boolean isPriority(){
        return isPriority;
    }

    public String getStatus(){
        return screeningStatus;
    }

    public void setStatus(String s){
        screeningStatus = s;
    }

    public void setPriority(boolean p){
        isPriority = p;
    }

    public boolean equals(Object obj){
        if(obj == null || !(obj instanceof Passenger)){
            return false;
        }
        if(this == obj){
            return true;
        }
        Passenger other = (Passenger)(obj);
        return this.name.equals(other.name) && this.flightNumber.equals(other.flightNumber);

    }

    public String toString(){
        return "Passenger: " + name + "   Flight: " + flightNumber 
            + "\nQueue Number: " + queueNumber + "   Priority: " + isPriority
            + "\nScreening Status: " + screeningStatus;
    }
}
