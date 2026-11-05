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


}
