/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InvApp.View_Controllers;

import InvApp.Models.Inventory;
import InvApp.Models.Part;
import InvApp.Models.Product;
import static InvApp.View_Controllers.MainController.getModifiedProduct;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import InvApp.Exceptions.ValidationException;

/**
 * FXML Controller class
 *
 * @author allis
 */
public class ProductsController implements Initializable {
    
    //Page label
    @FXML
    private Label ProductsPageLabel;
    
    //ID field, Auto-generated
    @FXML
    private TextField ProductsIDField;

    //Name field
    @FXML
    private TextField ProductsNameField;

    //Inventory level field
    @FXML
    private TextField ProductsInStockField;

    //price field
    @FXML
    private TextField ProductsPPUField;

    //minimum inventory field
    @FXML
    private TextField ProductsMinField;

    //maximum inventory field
    @FXML
    private TextField ProductsMaxField;

    //table showing all parts in inventory
    @FXML
    private TableView<Part> ProductAllPartsTable;

    //column for part ID numbers
    @FXML
    private TableColumn<Part, Integer> ProductAllPartsIDCol;
    
    //column for part name
    @FXML
    private TableColumn<Part, String> ProductAllPartsNameCol;
    
    //column for part inv level
    @FXML
    private TableColumn<Part, Integer> ProductAllPartsInvCol;

    //column for part price
    @FXML
    private TableColumn<Part, Double> ProductAllPartsPPUCol;

    //table for parts associated with product
    @FXML
    private TableView<Part> ProductAssociatedPartsTable;

    //column for part ID
    @FXML
    private TableColumn<Part, Integer> ProductAssociatedPartsIDCol;

    //column for part name
    @FXML
    private TableColumn<Part, String> ProductAssociatedPartsNameCol;

    //column for part inv level
    @FXML
    private TableColumn<Part, Integer> ProductAssociatedPartsInvCol;

    //column for part price
    @FXML
    private TableColumn<Part, Double> ProductAssociatedPartsPPUCol;

    //search parts
    @FXML
    private TextField ProductPartsSearchField;

    //oberservablelist to store product parts
    private ObservableList<Part> productParts = FXCollections.observableArrayList();
    
    //NULL if product is being added; otherwise this is the product being modified
    private final Product modifiedProduct;
    
    //Constructor
    public ProductsController() {
        this.modifiedProduct = getModifiedProduct();
    }
    
  
    //Add a part to product
    @FXML
    void handleAddProductPart(ActionEvent event) {
        Part part = ProductAllPartsTable.getSelectionModel().getSelectedItem();
        productParts.add(part);
        generateAssociatedPartsTable();
    }
   
    //Hand cancel.  Check to confirm cenacellation first, load main screen if user confirms.
    @FXML
    void handleCancel(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.NONE);
        alert.setTitle("Cancel Modification");
        alert.setHeaderText("Confirm Cancellation");
        alert.setContentText("Are you sure you want to cancel modification of product " + ProductsNameField.getText() + "?");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == ButtonType.OK) {
            Parent loader = FXMLLoader.load(getClass().getResource("Main.fxml"));
            Scene scene = new Scene(loader);
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(scene);
            window.show();
        }
    }

    //Delete a product part
    @FXML
    void handleDeleteProductPart(ActionEvent event) throws IOException {
        
        //Part can only be deleted if product has more than one part.  
        if (productParts.size() > 2) {
            Part part = ProductAssociatedPartsTable.getSelectionModel().getSelectedItem();
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.initModality(Modality.NONE);
            alert.setTitle("Part Delete");
            alert.setHeaderText("Confirm Delete");
            alert.setContentText("Are you sure you want to remove part " + part.getName() + " ?");
            Optional<ButtonType> result = alert.showAndWait();

            if (result.get() == ButtonType.OK) {
                productParts.remove(part);
            }
        }
        else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Part Delete Error!");
            alert.setHeaderText("Product requires one part!");
            alert.setContentText("This product must have at least one part.");
            alert.showAndWait();
        }
    }

    //Save the product
    @FXML
     void handleSave(ActionEvent event) throws IOException, InvApp.Exceptions.ValidationException {
        
        //Save the data from the input fields as strings
        String productName = ProductsNameField.getText();
        String productInv = ProductsInStockField.getText();
        String productPrice = ProductsPPUField.getText();
        String productMin = ProductsMinField.getText();
        String productMax = ProductsMaxField.getText();

        //Create a new Product        
        Product newProduct = new Product();
        
        //Set the data we saved in the Product we jsut created
        newProduct.setName(productName);
        newProduct.setPrice(Double.parseDouble(productPrice));
        newProduct.setInStock(Integer.parseInt(productInv));
        newProduct.setMin(Integer.parseInt(productMin));
        newProduct.setMax(Integer.parseInt(productMax));
       
        // If this is a modified product, clear old parts.
      
        if (modifiedProduct != null) {
            modifiedProduct.clearAssociatedParts();
        }
        
        // Iterate productParts and add them to the product
        for (Part i: productParts) {
            newProduct.addAssociatedPart(i);
        }
        
        //Check if our new product is valid according to our exception controls
      try {
        newProduct.isValid();
        
        // Create or update product as required, add product to master inventory list
        if (modifiedProduct == null) {
            newProduct.setProductID(Inventory.getProductsCount());
            Inventory.addProduct(newProduct);
        } else {
            newProduct.setProductID(modifiedProduct.getProductID());
            Inventory.updateProduct(newProduct);
        }
        // Loaad the main screen
        Parent loader = FXMLLoader.load(getClass().getResource("Main.fxml"));
        Scene scene = new Scene(loader);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    } catch (ValidationException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("ValidationError");
            alert.setHeaderText("Product not valid");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
     }
     //Search parts
    @FXML
    void handleSearchParts(ActionEvent event) throws IOException {
        //Save the text from the search field in a string
        String partsSearchIdString = ProductPartsSearchField.getText();
        
        //Creat a new Part called searchedPart.  
        //Cast the text we jsut saved to Int and look for it in inventory
        Part searchedPart = Inventory.lookupPart(Integer.parseInt(partsSearchIdString));

        if (searchedPart != null) {
            //create a new observablelist to store the searchedpart Part
            //Display the list in our table
            ObservableList<Part> filteredPartsList = FXCollections.observableArrayList();
            filteredPartsList.add(searchedPart);
            ProductAllPartsTable.setItems(filteredPartsList);
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Search Error");
            alert.setHeaderText("Part not found");
            alert.setContentText("No matching parts found!");
            alert.showAndWait();
        }
    }
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //if modifiedProduct is NULL, we show our screen in 
        //the ADD format as this is a new product. If it is not
        //NULL, we show the screen in the MODIFY format. 
        
        if (modifiedProduct == null) {
            ProductsPageLabel.setText("Add Product");
            int productAutoID = Inventory.getProductsCount();
            ProductsIDField.setText("ID: " + productAutoID);
            System.out.println("Here");
        } else {
            ProductsPageLabel.setText("Modify Product");
            
            //Set the fields to contain our modified product's existing data. 
            //Cast int and doubles to string
            ProductsIDField.setText(Integer.toString(modifiedProduct.getProductID()));
            ProductsNameField.setText(modifiedProduct.getName());
            ProductsInStockField.setText(Integer.toString(modifiedProduct.getInStock()));
            ProductsPPUField.setText(Double.toString(modifiedProduct.getPrice()));
            ProductsMinField.setText(Integer.toString(modifiedProduct.getMin()));
            ProductsMaxField.setText(Integer.toString(modifiedProduct.getMax()));
        
            productParts = modifiedProduct.getAssociatedParts();
        }
        
        //Set the columns in our All Parts table to display data
        ProductAllPartsIDCol.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getPartID()).asObject());
        ProductAllPartsNameCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        ProductAllPartsInvCol.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getInStock()).asObject());
        ProductAllPartsPPUCol.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getPrice()).asObject());
        
        //Set the columns in our associated parts table to display data
        ProductAssociatedPartsIDCol.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getPartID()).asObject());
        ProductAssociatedPartsNameCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        ProductAssociatedPartsInvCol.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getInStock()).asObject());
        ProductAssociatedPartsPPUCol.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getPrice()).asObject());
        
        //Generate our tables
        generateAllPartsTable();
        generateAssociatedPartsTable();
    }
    
    //Generate parts table from master inventory list
    public void generateAllPartsTable() {
        ProductAllPartsTable.setItems(Inventory.getParts());
    }
    
     //generate table of associated parts   
     public void generateAssociatedPartsTable() {
        ProductAssociatedPartsTable.setItems(productParts);
    }
}
