/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InvApp.Models;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


/**
 *
 * @author allis
 */
public class Inventory {
    

    //Observable list of all products
    private final static ObservableList<Product> products = FXCollections.observableArrayList();
   
    //observable list of all parts
    private final static ObservableList<Part> allParts = FXCollections.observableArrayList();
    
    //constructor
    public Inventory(){
        
    }
    
    //add a product to the inventory list
    public static void addProduct(Product product){
        products.add(product);
    }
    
    //remove a product from inventory
    public static boolean removeProduct(int productID){
        for (Product i : products){
            if (i.getProductID() == productID){
                products.remove(i);
                return true;
            }
        }
        return false;
    }
    
    //search for a product by ID
    public static Product lookupProduct(int productID){
        for(Product i : products){
            if (i.getProductID() == productID){
                return i;
            } 
        }
        return null;
        
    }
    
    //update product
    public static void updateProduct(Product productID){
        products.set(productID.getProductID(), productID);
        
        
    }
    
    //get the number of products
    public static int getProductsCount() {
        return products.size();
    }
       
    //Add a part to inventory
    public static void addPart(Part part){
        allParts.add(part);
    }
    
    //delete a part from inventory
    public static boolean deletePart(int partID){
        for (Part i : allParts){
            if (i.getPartID() == partID){
                allParts.remove(i);
                return true;
            }
        }
        
        return false;
    }
    
    //Search for a part in inventory
    public static Part lookupPart(int partID){
        for(Part i : allParts){
            if (i.getPartID() == partID){
                return i;
            } 
        }
        return null;
        
    }
    
    //Update part in inventory
     public static void updatePart(Part partID){
       allParts.set(partID.getPartID(), partID);
        
        
    }
     
     //remove part from inventory
     public static boolean removePart(int partID) {
        for (Part i : allParts) {
            if (i.getPartID() == partID) {
                allParts.remove(i);
                return true;
            }
        }
        
        return false;
    }
     
     //return a list of all parts
       public static ObservableList<Part> getParts() {
        return allParts;
    }
    
       //return number of parts in inventory
        public static int getPartsCount() {
        return allParts.size();
    }
        
        //return list of products in inventory
        public static ObservableList<Product> getProducts() {
        return products;
    }
        
    //check to see if product can be deleted.  Returns true if there are
     // 0 associated parts
    public static boolean canDeleteProduct(Product product) {
        return product.getAssociatedPartsCount() == 0;
    }     

}
