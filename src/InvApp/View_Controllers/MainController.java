/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InvApp.View_Controllers;

import InvApp.InvApp;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import InvApp.Models.Part;
import InvApp.Models.Product;
import InvApp.Models.Inventory;
import static InvApp.Models.Inventory.getParts;
import static InvApp.Models.Inventory.getProducts;
import static InvApp.Models.Inventory.removePart;
import static InvApp.Models.Inventory.removeProduct;
import java.io.IOException;
import java.util.Optional;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Modality;
import javafx.stage.Stage;
/**
 * FXML Controller class
 *
 * @author allis
 */
public class MainController implements Initializable {
    
    
    @FXML
    private TableView<Part> PartsTable; // Main parts table

    @FXML
    private TableColumn<Part, Integer> PartID; //part ID number, int

    @FXML
    private TableColumn<Part, String> PartName; //part name, string

    @FXML
    private TableColumn<Part, Integer> InvLevel; //part inventory level, int

    @FXML
    private TableColumn<Part, Double> PartPPU; //part price per unit, double

    @FXML
    private TextField PartSearchField;

    @FXML
    private TableView<Product> ProductsTable; //main products table

    @FXML
    private TableColumn<Product, Integer> ProductID; //product ID, int

    @FXML
    private TableColumn<Product, String> ProductName; //product name, string

    @FXML
    private TableColumn<Product, Integer> ProdInvLevel; //product inventory level, int

    @FXML
    private TableColumn<Product, Double> ProdPPU; //product price per unit, double

    @FXML
    private TextField ProdSearchField;

    //the current modified part
    private static Part modifiedPart;
    
    //the current modified product
    private static Product modifiedProduct;
   
    
    public MainController() {
       //Constructor 
    }
     
    //returns current modified part
    public static Part getModifiedPart() {
        return modifiedPart;
    }
    
     
    //Set a part as our modified part.
    public void setModifiedPart(Part modifyPart) {
        MainController.modifiedPart = modifyPart;
    }
    
        
    //Get the current modified product.
    public static Product getModifiedProduct() {
        return modifiedProduct;
    }
    
   //Set a product as a modified product
    public void setModifiedProduct(Product modifiedProduct) {
        MainController.modifiedProduct = modifiedProduct;
    }
    
    //Handle exit.
    @FXML
    void handleExit(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.NONE);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Confirm Exit");
        alert.setContentText("Are you sure you want to exit?");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == ButtonType.OK) {
            System.exit(0);
        }
    }    

    //handle add part
    //load part screen when add button is clicked
    @FXML
    void handleAddPart(ActionEvent event) throws IOException {        
        showPartsScreen(event);
    }

    //Handle delete
    @FXML
    void handleDeletePart(ActionEvent event) throws IOException {
        Part part = PartsTable.getSelectionModel().getSelectedItem();

        //show alert to confirm delete
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.NONE);
        alert.setTitle("Delete Part");
        alert.setHeaderText("Confirm Delete?");
        alert.setContentText("Are you sure you want to delete " + part.getName() + "?");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == ButtonType.OK) {
            removePart(part.getPartID());
            generatePartsTable();
        }

    }
    
    //Handle add product
    //show prodcut screen when add button is clicked
    @FXML
    void handleAddProduct(ActionEvent event) throws IOException {
        showProductScreen(event);
    }
    
    //Return true if product has 0 associated parts
      public static boolean canDeleteProduct(Product product) {
        return product.getAssociatedPartsCount() == 0;
    }
    
    //Handle product delete  
    @FXML
    void handleDeleteProduct(ActionEvent event) throws IOException {
        Product product = ProductsTable.getSelectionModel().getSelectedItem();
        
        //Alert if product has associated parts
        if (!canDeleteProduct(product)) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Produt Deletion Error!");
            alert.setHeaderText("Produt cannot be removed!");
            alert.setContentText("This product has associated parts.");
            alert.showAndWait();
        } 
         //Alert to confirm delete
        else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.initModality(Modality.NONE);
            alert.setTitle("Product Delete");
            alert.setHeaderText("Confirm deletion?");
            alert.setContentText("Are you sure you want to delete " + product.getName() + "?");
            Optional<ButtonType> result = alert.showAndWait();

            if (result.get() == ButtonType.OK) {
                removeProduct(product.getProductID());
                generatePartsTable();
            }
        }
    }
   
    //handle modified parts
    //shows modify parts screen
    @FXML
    void handleModifyPart(ActionEvent event) throws IOException {
        
        modifiedPart = PartsTable.getSelectionModel().getSelectedItem();
        setModifiedPart(modifiedPart);

        showPartsScreen(event);
    }
    
    //handler for modify product
    //shows the product screen
    @FXML
    void handleModifyProduct(ActionEvent event) throws IOException {
        modifiedProduct = ProductsTable.getSelectionModel().getSelectedItem();
        setModifiedProduct(modifiedProduct);
        
        showProductScreen(event);
    }
    
     //Search for a part
    @FXML
    void handleSearchPart(ActionEvent event) throws IOException {
        String partSearch = PartSearchField.getText();
        Part searchedPart = Inventory.lookupPart(Integer.parseInt(partSearch));

        if (searchedPart != null) {
            ObservableList<Part> filteredPartsList = FXCollections.observableArrayList();
            filteredPartsList.add(searchedPart);
            PartsTable.setItems(filteredPartsList);
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Search Error");
            alert.setHeaderText("Part not found");
            alert.setContentText("No matching parts found!");
            alert.showAndWait();
        }
    }
    
    
     //Search for a product
    @FXML
    void handleSearchProduct(ActionEvent event) throws IOException {
        String productSearch = ProdSearchField.getText();
        Product searchedProduct= Inventory.lookupProduct(Integer.parseInt(productSearch));

        if (searchedProduct != null) {
            ObservableList<Product> filteredProductList = FXCollections.observableArrayList();
            filteredProductList.add(searchedProduct);
            ProductsTable.setItems(filteredProductList);
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Search Error");
            alert.setHeaderText("Product not found");
            alert.setContentText("No matching products found!");
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
        // initialize part and product with nulls
        setModifiedPart(null);
        setModifiedProduct(null);

        //Set part table columns with data
        PartID.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getPartID()).asObject());
        PartName.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        InvLevel.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getInStock()).asObject());
        PartPPU.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getPrice()).asObject());
        
        //Set product table columns with data
        ProductID.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getProductID()).asObject());
        ProductName.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        ProdInvLevel.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getInStock()).asObject());
        ProdPPU.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getPrice()).asObject());
        
        //generate tables
        generatePartsTable();
        generateProductsTable();
    }  
    
     //generate parts table
    public void generatePartsTable() {
          PartsTable.setItems(getParts());
    }

    //generate products table
     public void generateProductsTable() {
        ProductsTable.setItems(getProducts());
    }
     
     
     public void setMainApp(InvApp mainApp) {
        generatePartsTable();
        generateProductsTable();
    }
     
      
    //Load Part screen
     public void showPartsScreen(ActionEvent event) throws IOException {
        Parent loader = FXMLLoader.load(getClass().getResource("Parts.fxml"));
        Scene scene = new Scene(loader);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }
    
    //Load product screen
    public void showProductScreen(ActionEvent event) throws IOException {
        Parent loader = FXMLLoader.load(getClass().getResource("Products.fxml"));
        Scene scene = new Scene(loader);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }
}
