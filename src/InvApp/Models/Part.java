/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InvApp.Models;

import InvApp.Exceptions.ValidationException;

/**
 *
 * @author allis
 */
public abstract class Part {
   
    private int partID; //ID number of the part
    private String name; //name of the part
    private double price; //price of the part
    private int inStock;  //Number of the parts in stock
    private int min;  //minimum inventory level
    private int max;  //maximum inventory level
   
    //Constructor
    public Part(){
        
    }
    
    //Set the name of the part
     public void setName(String name){    
         this.name = name;    
}
    //Get the name of the part
    public String getName(){
        return name;
    }
       
    //Set the price
    public void setPrice(double price){
        this.price = price;
}
    
    //Return the price
    public double getPrice(){
        return price;
    }
    
    //Set the inventory level
    public void setInStock(int inStock){
        this.inStock = inStock;
    }
    
    //Get inventory level
    public int getInStock(){
        return inStock;
    }
    
    //Set the minimum inventory
    public void setMin(int min){
        this.min = min;
    }
    
    //Return the minimum inventory
    public int getMin(){
        return min;
    }
    
    //Set the maximum inventory
    public void setMax(int max){
        this.max = max;
    }
    
    //Get the maximum inventory
    public int getMax(){
        return max;
    }
    
    //set the part ID
     public void setPartID(int partID){
        this.partID = partID;
    }
    
     //Get the part ID
    public int getPartID(){
        return partID;
    } 
    //Check if part contains all necessary information, throw exception if not
    
    public boolean isValid() throws ValidationException {
        // Name is required
        if (getName().equals("")) {
            throw new ValidationException("The name field cannot be empty.");
        } // inventory must be positive
        if (getInStock() < 0) {
            throw new ValidationException("The current inventory must be greater than 0.");
        }
        
        // part price must be positive
        if (getPrice() < 0) {
            throw new ValidationException("The price must be greater than $0");
        }
        
        // the minimum must be positive
        if (getMin() < 0) {
            throw new ValidationException("The minimum inventory must be greater than 0.");
        }
        
        // the maximum must be greater than the minimum
        if (getMin() > getMax()) {
            throw new ValidationException("The minimum inventory must be less than the maximum.");
        }
        
        // the in stock inventory must be between min and max
        if (getInStock() < getMin() || getInStock() > getMax()) {
            throw new ValidationException("The current inventory must be between the minimum and maximum inventory.");
        }
        
        return true;
    }
}
