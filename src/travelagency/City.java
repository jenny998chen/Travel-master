package travelagency;

public class City {
    String name;
    int x;
    int y;
    String currency;
    double conversion;
    String timeZone;
    int time;
    
    public City(String name, int x, int y, String currency, double conversion,String timeZone) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.currency = currency;
        this.conversion = conversion;
        this.timeZone = timeZone;
    }
   
}
