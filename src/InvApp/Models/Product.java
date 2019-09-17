/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InvApp.Models;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import InvApp.Exceptions.ValidationException;

/**
 *
 * @author allis
 */
public class Product {
    
    private ObservableList<Part> associatedParts = FXCollections.observableArrayList();  //ObservableList to hold The part which is associated with the product
    private int productID; //ID number of product
    private String name; //Name of product
    private double price; //Product price
    private int inStock;  //Numbers of products in inventory
    private int min;  //minimum inventory level
    private int max;  //maximum inventory level
   
    
  

    public Product() {
        //constructor
    }
    
    //Set product name
     public void setName(String name){    
         this.name = name;    
}
    //Get product name
    public String getName(){
        return name;
    }
    
    //Set product price
    public void setPrice(double price){
        this.price = price;
}
    //Get product price
    public double getPrice(){
        return price;
    }
    
    //Set priduct inventory number
    public void setInStock(int inStock){
        this.inStock = inStock;
    }
    
    //Get product inventory number
    public int getInStock(){
        return inStock;
    }
    
    //Set minimum inventory
    public void setMin(int min){
        this.min = min;
    }
    
    //Get minimum inventory
    public int getMin(){
        return min;
    }
    
    //Set maximum inventory
    public void setMax(int max){
        this.max = max;
    }
    
    //Get maximum inventory
    public int getMax(){
        return max;
    }
    
    //Set product ID
    public void setProductID(int productID){
        this.productID = productID;
    }
    
   //Get product ID
    public int getProductID(){
       return productID;
    } 
    
    public ObservableList<Part> getAssociatedParts() {
        return associatedParts;
    }
    
    public int getAssociatedPartsCount() {
        return associatedParts.size();
    }
    
    //Check to make sure part contains valid info, throw exception if not
    public boolean isValid() throws ValidationException {
        double totalPartsPrice = 0.00;
        
        // summate the price of associated parts
        for(Part i : getAssociatedParts()) {
            totalPartsPrice += i.getPrice();
        }
        
        // ensure a product is named
        if (getName().equals("")) {
            throw new ValidationException("The name field cannot be empty.");
        }
        
        // inventory must be at least 1
        if (getInStock() < 0) {
            throw new ValidationException("The current inventory must be greater than 0.");
        }
        
        // price must be positive
        if (getPrice() < 0) {
            throw new ValidationException("The price must be greater than $0");
        }
        
        // a product must have at least one part
        if (getAssociatedPartsCount() < 1) {
            throw new ValidationException("The product must contain at least 1 part.");
        }
        
        // the sum of parts must be less than the price of the product
        if (totalPartsPrice > getPrice()) {
            throw new ValidationException("The product price must be greater than total cost of associated parts.");
        }
        
        // min inventory must be greater than zero
        if (getMin() < 0) {
            throw new ValidationException("The minimum inventory must be greater than 0.");
        }
        
        // max inventory must be greater than minimum
        if (getMin() > getMax()) {
            throw new ValidationException("The minimum inventory must be less than the maximum.");
        }
        
        // current inventory must be between the min and max.
        if (getInStock() < getMin() || getInStock() > getMax()) {
            throw new ValidationException("The current inventory must be between the minimum and maximum inventory.");
        }
        
        return true;
    }
    
    //Add an associated part to a product
    public void addAssociatedPart(Part associatedPart){
        this.associatedParts.add(associatedPart);
    }
   
    
    //Remove assoicated part
    public boolean removeAssociatedPart(int partID){
        for (Part i : associatedParts) //search parts list for index part i
            {
                if (i.getPartID()== partID) //Does the part id of index match the part id passed in?
                { 
                    associatedParts.remove(i); //remove the part at index i if it's a match 
                    return true;    
                }
                 
                
    }
    return false; //if no match      
    
}
     
   //Lookup associated parts by ID
   
    public Part lookupAssociatedPart(int partID){
       //search associatedParts list to see if partID matches index i; if so return index i
        for (Part i : associatedParts){
        if (i.getPartID() == partID){
            return i; 
        }
    }
        return null; //return nothing if no match
    }
        
    public void clearAssociatedParts() {
        associatedParts = FXCollections.observableArrayList();
    }
    
}