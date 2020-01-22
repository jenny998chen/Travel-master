package travelagency;

import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import static travelagency.ConverterWindow.destComboBox;
import static travelagency.AnimationWindow.*;

public class TravelMasterUI extends JFrame implements ActionListener{
    public TravelMasterUI() throws IOException {
        initComponents();
    }
    //creates all buttons and windows
    BufferedImage img;
    JRadioButton [] buttonList = new JRadioButton[numCities];
    JButton myRoute= new JButton("Calculate My Route");
    JButton clear= new JButton("Clear");
    JButton showAnimation= new JButton("<html>Show Animation for<br>traveling to all cities<html>");
    JButton Converter= new JButton("Converter Tools");
    
    AnimationWindow animationWindow;
    ConverterWindow converterWindow;
    //creates an array of city that will later be filled in with the user's selection
    City[] selectedCities;
    int [] shortestPath;
    int mytotaldistance;
    
    //determines whether or not a connected line of the shortest path will be shown
    boolean connectCities=false;
    JPanel panel = new JPanel(){
    public void paintComponent(Graphics g){        
        super.paintComponent(g);      
        g.drawImage(img, 5, 5, this);
        
        g.setColor(Color.red);
        //this will not be drawn if the clear button is clicked since connectCities is set to false
        if(connectCities==true){
            //connect all selected cities
            for (int i = 0; i < selectedCities.length-1; i++) {
                City city = selectedCities[shortestPath[i]];
                City city2 = selectedCities[shortestPath[i+1]];
                
                g.drawLine(city.x+5, city.y+5, city2.x+5, city2.y+5);
            }
            //shows total distance on the top right corner, the scale of (pixel: km) is (1.00:3.98)
            g.drawString("Total Distance "+ Math.round(mytotaldistance*3.98*100.0)/100.0+" km", 730, 20);
        }
    }
    };  
    //initializes the properties of all Components
    private void initComponents() throws IOException {
        //the layout is set to null so that the components could be added at a desired location
        panel.setLayout(null);
        panel.setVisible(true);
        //fills in the list of buttons corresponding to the cities and its location 
        for (int i = 0; i < buttonList.length; i++) {
            buttonList[i]=new JRadioButton(cList[i].name);
            buttonList[i].addActionListener(this);
            buttonList[i].setBounds(cList[i].x, cList[i].y, 20, 20);
            panel.add(buttonList[i]);     
        }
        //for each button add an action listener, set its location and size 
        myRoute.addActionListener(this);
        myRoute.setBounds(20, 30, 148, 20);
        panel.add(myRoute);
        
        clear.addActionListener(this);
        clear.setBounds(20, 70, 148, 20);
        panel.add(clear);
        
        showAnimation.addActionListener(this);
        showAnimation.setBounds(20, 150, 148, 40);
        panel.add(showAnimation);
        
        Converter.addActionListener(this);
        Converter.setBounds(20, 110, 148, 20);
        panel.add(Converter);
        
        //the panel which contains all components is added to the frame
        this.getContentPane().add(panel);
        //creates the other two windows, which have their initial visibility set to false
        animationWindow = new AnimationWindow();
        converterWindow = new ConverterWindow();
        img = ImageIO.read(new File("capital_cities.jpg"));
        pack();
    } 
    
    public void findShortestPath() {
        mytotaldistance = Integer.MAX_VALUE;
        int previousTotal;
        for (int i = 0; i < selectedCities.length; i++) {
            shortestPath[i] = i;
        }
        //while total distance can still be improved.
        do {
            previousTotal = mytotaldistance;
            //compairs all pairs of lines
            for (int i = 0; i < selectedCities.length - 2; i++) {
                for (int j = i + 2; j < selectedCities.length-1; j++) { // i+1
                    int change = calculateChange(i, j,selectedCities,shortestPath);
                    //if the distance connecting from the (i — j and i+1 — j+1) is greater than the original path (i — i+1 and j — j+1)
                    if (change > 0) {
                        //reduce the total distance by reconnecting the path
                        swap(i, j,shortestPath);
                    }
                }
            }
            //updates the current total distance of the path
            mytotaldistance = 0;
            for (int i = 0; i < selectedCities.length-1; i++) {
                City city1 = selectedCities[shortestPath[i]];
                City city2 = selectedCities[shortestPath[i+1]];
                mytotaldistance += dist(city1, city2);
            }
        //ends if the total distance can no longer be improved (totalDistance=previousTotal)    
        } while (mytotaldistance < previousTotal);
        //since the first city is re-added to the end of the list for calculation purposes,
        //if the user selects only two cities the distance would be doubled the actual distance
        if(selectedCities.length==3) mytotaldistance/=2;
    }
      
    @Override
    public void actionPerformed(ActionEvent e) {
        //if the Calculate My Route button is clicked
        if ( e.getSource() ==myRoute){
            //check each city and add the selected cities to an array list
            ArrayList<City> selected = new ArrayList();           
            for (int i = 0; i < numCities; i++) {
               if(buttonList[i].isSelected()==true) selected.add(cList[i]);

            }
            //re-add the first city to the end of the list
            selected .add(selected.get(0));
            
            //selectedCities is reassigned each time the button is clicked
            selectedCities = new City[selected.size()];
            shortestPath=new int[selected.size()];
            for (int i = 0; i < selected.size(); i++) {
                selectedCities[i]=selected.get(i);   
            }
            findShortestPath();
            connectCities=true;
            panel.repaint();
        }
        else if(e.getSource() ==clear){
            for (int i = 0; i < numCities; i++) {
               buttonList[i].setSelected(false);
            }
            connectCities=false;
            panel.repaint();
        }
        //if the show animation button is clicked, set the window visible and perform the animation.
        else if(e.getSource() ==showAnimation){
            animationWindow.setVisible(true);
            animationWindow.twoOptAnimation();                     
        }
        //if the converter button is clicked
        else if(e.getSource() ==Converter){
            int i=0;
            while(i!=buttonList.length&&buttonList[i].isSelected()==false){
                i++;
            }
            //set the destination city to the city selected on the map
            if(i!=buttonList.length){
                destComboBox.setSelectedIndex(i);
            }
            converterWindow.setVisible(true);
            
        }
        //if any radio button is selected, set selected to true
        else{
            for (int i = 0; i < buttonList.length; i++) {
                if ( e.getSource() ==buttonList[i]){
                    buttonList[i].setSelected(true);
                }     
            }
        }
    }      

    public static void main(String args[]) throws IOException  {
        //reads information of cities from a file
        FileReader r = new FileReader("cityInfo.txt");
        Scanner s = new Scanner(r);
        for (int i = 0; i < cList.length; i++) {
            cList[i] = new City(s.next(), s.nextInt(), s.nextInt(),s.next(),s.nextDouble(),s.next());
        }

        TravelMasterUI wind = new TravelMasterUI();
        wind.setTitle("Travel Master");
        wind.setSize(908, 800);
        wind.setDefaultCloseOperation(EXIT_ON_CLOSE);
        wind.setVisible(true); 
    }      
}
