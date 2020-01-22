package travelagency;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JDialog;
import javax.swing.JPanel;

public class AnimationWindow extends JDialog{
    //the list of European cities and its properties are accessible to all classes
    static int numCities =43;
    static City[] cList = new City[numCities+1];
    static int[] path = new int[cList.length];
    

    int s1,s2;
    boolean printColor;
    int totalDistance;
    static BufferedImage map;
    JPanel animationPanel= new JPanel(){
    @Override
    public void paintComponent(Graphics g) {
          super.paintComponent(g);
          g.drawImage(map, 0, 0, this);
          g.setColor(Color.black);
          
          g.drawString("Total Distance "+ Math.round(totalDistance*3.98*100.0)/100.0, 726, 20);
          g.drawString("km", 865, 20);
          //connect all cities by their path
          for (int i = 0; i < numCities; i++) {
              City city = cList[path[i]];
              City city2 = cList[path[i+1]];
              //s1 and s2 is are the current indices being switched a red line will be used to indicate this
              if(printColor&&(i==s1||i==s2)){
                  g.setColor(Color.red); 
              }
              else{
                  g.setColor(Color.black); 
              }
              g.drawLine(city.x, city.y, city2.x, city2.y);
              }
          }
      };

    public AnimationWindow() throws IOException {
        setTitle("Animation Window");
        setSize(900, 800);       
        map= ImageIO.read(new File("translusentMap2.jpg"));
        add(animationPanel);       
    }

    
    //updates the graphic while performing the 2-opt algorithm
    public void twoOptAnimation() {
        int previousTotal = Integer.MAX_VALUE;
        totalDistance = Integer.MAX_VALUE;
        //the path taken for this trip is set to the order in which the of cities are connected
        for (int i = 0; i < numCities; i++) {
            path[i] = i;
        }
        printColor = true;
        //while total distance can still be improved.
        do {
            previousTotal = totalDistance;
            for (int i = 0; i < numCities - 1; i++) {
                for (int j = i + 2; j < numCities; j++) { // i+1
                    int change = calculateChange(i, j,cList,path);
                    //if the distance connecting from the (i — j and i+1 — j+1) is greater than the original path (i — i+1 and  — j+1)
                    if (change > 0) {
                        //the start point for two lines are tracked so that the red lines could be used toidicate swiches in animation
                        s1=i;
                        s2=j;
                        swap(i, j,path);
                        totalDistance = getTotalDistance();
                        //updates the graphic for every change made
                        sleep(500);
                        paint(this.getGraphics());
                    }
                }
            }
        //ends if the total distance can no longer be improved (totalDistance=previousTotal)
        } while (totalDistance < previousTotal);
        //for the last frame paint the picture without the red lines
        printColor = false;
        paint(this.getGraphics());
    }
    //calculates the total distance of the path connecting all cities
    public static int getTotalDistance() {
        double distance = 0;
        for (int i = 0; i < numCities; i++) {
            City city1 = cList[path[i]];
            City city2 = cList[path[i+1]];
            distance += dist(city1, city2);
        }
        return (int) distance;
    }

    public static int calculateChange(int i, int j,City[] cList,int[] path) {
        double s1s2 = dist(cList[path[i]], cList[path[j]]);
        double d1d2 = dist(cList[path[i+1]], cList[path[j+1]]);
        double s1d1 = dist(cList[path[i]], cList[path[i+1]]);
        double s2d2 = dist(cList[path[j]], cList[path[j+1]]);

        int distance = (int) ((s1d1 + s2d2 )-(s1s2 + d1d2));
        return distance;
    }
    //reverses all indices between the first and second index to complete the swap
    public static void swap(int cityIndex1,int cityIndex2,int[] path) {
        int start = cityIndex1+1;
        int end = cityIndex2;
        while (start < end) {
            int temp = path[end];
            path[end] = path[start];
            path[start] = temp;
            start++;
            end--;
        } 
    }


    public static double dist(City a, City b) {
        return Math.sqrt(Math.pow(a.x - b.x, 2) + Math.pow(a.y - b.y, 2));
    }

    public static void sleep( int numMilliseconds ) {
        try {
            Thread.sleep( numMilliseconds );
        }  
        catch (Exception e) {
        }
    }
}
