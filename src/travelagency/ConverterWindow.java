/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package travelagency;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import static travelagency.AnimationWindow.*;

public class ConverterWindow extends JDialog implements ActionListener{
//the default origin city is set to Toronto. It could be altered by reselecting the combo box
    City toronto = new City("Toronto", -1605, 565,"CanadianDollar",1,"EDT");    
    City startCity =toronto;
    City destCity ;
    
    JComboBox startComboBox= new JComboBox();
    static JComboBox destComboBox= new JComboBox();
    
    JButton convertCurrency= new JButton("Convert Currency");
    JButton convertTime= new JButton("Convert Time");
   
    JTextField dateInput = new JTextField("0618");
    JTextField timeInput = new JTextField("00:00"); 
    JTextField moneyInput = new JTextField("1");
     
    JLabel timeOutput= new JLabel();
    JLabel moneyOutput= new JLabel();
    JLabel startTzone= new JLabel();
    JLabel startCurrency= new JLabel();
    JLabel destTzone= new JLabel();
    JLabel destCurrency= new JLabel();    
    

    JPanel converterPanel = new JPanel();
    public ConverterWindow() throws IOException {
        setTitle("Converter Window");
        setSize(800, 400);
        setLayout(null);
        //set the time from universal time for each city according to time zone
        for (int i = 0; i < numCities; i++) {
            if(cList[i].timeZone.equals("CEST")){
                cList[i].time=2;   
            }
            else if (cList[i].timeZone.equals("EEST")||cList[i].timeZone.equals("MSK")||cList[i].timeZone.equals("FET")){
                cList[i].time=3; 
            }
            else{
                cList[i].time=1;
            }
        }

        String[] cityNames = new String[cList.length];
        for (int i = 0; i < numCities; i++) {
            cityNames[i] = cList[i].name;
        }
        cityNames[numCities] ="Toronto";
        toronto.time = -4;
        
        //set initial properties for each combo box and button
        startComboBox.setModel(new DefaultComboBoxModel(cityNames));
        startComboBox.addActionListener(this);
        startComboBox.setBounds(180, 50, 120, 20);
        startComboBox.setSelectedIndex(numCities);
        add(startComboBox);
        
        destComboBox.setModel(new DefaultComboBoxModel(cityNames));
        destComboBox.addActionListener(this);
        destComboBox.setBounds(480, 50, 120, 20);
        destComboBox.setSelectedIndex(17);
        add(destComboBox);
        
        convertCurrency.addActionListener(this);
        convertCurrency.setBounds(20, 250, 135, 20);
        add(convertCurrency);
        
        convertTime.addActionListener(this);
        convertTime.setBounds(20, 150, 135, 20);
        add(convertTime);
        
        
        //set locations for each time zone converter component
        JLabel equalSign2= new JLabel("=");
        dateInput.setBounds(180, 150, 40, 20);
        timeInput.setBounds(240, 150, 40, 20);
        startTzone.setBounds(310, 150, 40, 20);
        equalSign2.setBounds(440, 150, 20, 20);
        timeOutput.setBounds(480, 150, 100, 20);
        destTzone.setBounds(600, 150, 40, 20);
        
        //set locations for each currency converter component
        JLabel equalSign= new JLabel("=");
        moneyInput.setBounds(180, 250, 100, 20);
        startCurrency.setBounds(310, 250, 120, 20);
        equalSign.setBounds(440, 250, 20, 20);
        moneyOutput.setBounds(480, 250, 100, 20);
        destCurrency.setBounds(600, 250, 120, 20);
        
        javax.swing.border.Border border = BorderFactory.createLineBorder(Color.black);
        moneyOutput.setBorder(border);
        timeOutput.setBorder(border);

        //add all components to ConverterWindow
        add(dateInput);
        add(timeInput);
        add(startTzone);
        add(equalSign);
        add(timeOutput);
        add(destTzone);
                
        add(moneyInput);
        add(startCurrency);
        add(startCurrency);
        add(equalSign2);
        add(moneyOutput);
        add(destCurrency);
    }
  
    @Override
    public void actionPerformed(ActionEvent e) {
        if ( e.getSource() ==startComboBox){
            //the last item added to the combo box is Toronto
            if(startComboBox.getSelectedIndex()==numCities){
                startCity =toronto;
            }//the index of the city in cList and in the combo boxes are the same
            else{
                startCity = cList[startComboBox.getSelectedIndex()];
            }
            //displays the time zone and currency of the current startCity
            startCurrency.setText(startCity.currency);
            startTzone.setText(startCity.timeZone);                 
        }
        else if ( e.getSource() ==destComboBox){
            if(destComboBox.getSelectedIndex()==numCities){
                destCity =toronto;
            }
            else{
                destCity = cList[destComboBox.getSelectedIndex()];
            }
            destTzone.setText(destCity.timeZone);
            destCurrency.setText(destCity.currency);
        }
        //if the convert currency button is clicked, the input is taken, converted and displayed in the outputbox
        else if ( e.getSource() ==convertCurrency){
            double m =Double.parseDouble(moneyInput.getText());
            moneyOutput.setText(m/startCity.conversion*destCity.conversion+"");
        }
        //if the convert time button is cliked
        else{
            timeOutput.setText(getConvertedTime(dateInput.getText(), timeInput.getText()));
        }
    }   
    //converts the time of the start city to the time of the destination city
    public String getConvertedTime(String date, String time){
        int d= Integer.parseInt(date.substring(2));
        
        int colon =time.indexOf(":");
        int h=Integer.parseInt(time.substring(0, colon));
        h+=destCity.time-startCity.time;
        if(h>=24) {
            h-=24;
            d+=1;
        }
        else if(h<0){
            h+=24;
            d-=1;                
        }
        return date.substring(0,2)+format(d)+"     "+format(h)+":"+time.substring(colon+1);
    }   
    public String format(int t){
        if(t<10) return "0"+t;
        else return ""+t;
    }
}

