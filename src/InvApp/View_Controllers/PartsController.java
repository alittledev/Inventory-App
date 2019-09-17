/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InvApp.View_Controllers;

import InvApp.Models.Part;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import InvApp.Models.InHouse;
import InvApp.Models.Inventory;
import InvApp.Models.OutSourced;
import InvApp.Models.Part;
import static InvApp.View_Controllers.MainController.getModifiedPart;
import java.io.IOException;
import java.util.Optional;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Modality;
import javafx.stage.Stage;
import InvApp.Exceptions.ValidationException;

/**
 * FXML Controller class
 *
 * @author allis
 */
public class PartsController implements Initializable {
    
    //Label for parts page
    @FXML
    private Label PartsPageLabel;

    //InHouse Radio buttons 
    @FXML
    private RadioButton PartsInHouseRadioButton;

    //Outsourced radio button
    @FXML
    private RadioButton PartsOutsourcedRadioButton;

    //part manufacturor label
    @FXML
    private Label PartsMfgLabel;

    //part ID field
    @FXML
    private TextField PartsIDField;

    //Part name field
    @FXML
    private TextField PartsNameField;

    //part inventory level field
    @FXML
    private TextField PartsInvField;

    //part price field
    @FXML
    private TextField PartsPPUField;

    //part minimum inventory field
    @FXML
    private TextField PartsMinField;

    //part maximum inventory field
    @FXML
    private TextField PartsMaxField;

    //part manufacturor field
    @FXML
    private TextField PartsMfgField;

    //True if part is In-House; False if part is Outsourced
    private boolean isInHouse;
    
    // Part being modified if this is a modification, else null 
    private final Part modifyPart;
    
    //Constructor
    public PartsController() {
        this.modifyPart = getModifiedPart();
    }
    
    //If we have an inhouse part, 
    //the Machine ID  field is shown
    @FXML
    void handleInHouse(ActionEvent event) {
        isInHouse = true;
        PartsMfgLabel.setText("Machine ID");
    }
    
    //If we have an outsourced part,
    //The company name field is shown
    @FXML
    void handleOutsource(ActionEvent event) {
        isInHouse = false;
        PartsMfgLabel.setText("Company Name");
    }
    
    //Handle cancel
   @FXML
    void handleCancel(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.NONE);
        alert.setTitle("Cancel Modification");
        alert.setHeaderText("Confirm cancellation");
        alert.setContentText("Are you sure you want to cancel update of part " + PartsNameField.getText() + "?");
        Optional<ButtonType> result = alert.showAndWait();
        
        if (result.get() == ButtonType.OK) {
            Parent loader = FXMLLoader.load(getClass().getResource("Main.fxml"));
            Scene scene = new Scene(loader);
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(scene);
            window.show();
        }
    }
    
     @FXML
    void handleSave(ActionEvent event) throws IOException {
        // Get the data as text
        String partName = PartsNameField.getText();
        String partInv = PartsInvField.getText();
        String partPPU = PartsPPUField.getText();
        String partMin = PartsMinField.getText();
        String partMax = PartsMaxField.getText();
        String partMfg = PartsMfgField.getText();
        
        //set Inv to 0 if left blank
        if ("".equals(partInv)) {
            partInv = "0";
        }
        
        //If it's an inhouse part, create a new inhouse part and set the data in it.
        if (isInHouse) {
            
            InHouse modifiedPart = new InHouse();
            modifiedPart.setName(partName);
            modifiedPart.setPrice(Double.parseDouble(partPPU));
            modifiedPart.setInStock(Integer.parseInt(partInv));
            modifiedPart.setMin(Integer.parseInt(partMin));
            modifiedPart.setMax(Integer.parseInt(partMax));
            modifiedPart.setMachineID(Integer.parseInt(partMfg));

            try {
                modifiedPart.isValid();
                
                // If this is a modified part, we will update it. Otherwise we save a
                //    new part.
                if (modifyPart == null) {
                    modifiedPart.setPartID(Inventory.getPartsCount());
                    Inventory.addPart(modifiedPart);
                } else {
                    int partID = modifyPart.getPartID();
                    modifiedPart.setPartID(partID);
                    Inventory.updatePart(modifiedPart);
                }
                
                // Return to the main screen
                Parent loader = FXMLLoader.load(getClass().getResource("Main.fxml"));
                Scene scene = new Scene(loader);
                Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
                window.setScene(scene);
                window.show();
            } catch (ValidationException e) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("ValidationError");
                alert.setHeaderText("Part not valid");
                alert.setContentText(e.getMessage());
                alert.showAndWait();
            }
        } else {
            // If not an inhouse part, create a new outsourced part and set the data.
            OutSourced modifiedPart = new OutSourced();
            modifiedPart.setName(partName);
            modifiedPart.setPrice(Double.parseDouble(partPPU));
            modifiedPart.setInStock(Integer.parseInt(partInv));
            modifiedPart.setMin(Integer.parseInt(partMin));
            modifiedPart.setMax(Integer.parseInt(partMax));
            modifiedPart.setCompanyName(partMfg);
            
            try {
                modifiedPart.isValid();
                
                // If this is a modified part, we will update it. Otherwise we save a
                //    new part.
                if (modifyPart == null) {
                    modifiedPart.setPartID(Inventory.getPartsCount());
                    Inventory.addPart(modifiedPart);
                } else {
                    int partID = modifyPart.getPartID();
                    modifiedPart.setPartID(partID);
                    Inventory.updatePart(modifiedPart);
                }
                
                // Return to the main screen
                Parent loader = FXMLLoader.load(getClass().getResource("Main.fxml"));
                Scene scene = new Scene(loader);
                Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
                window.setScene(scene);
                window.show();
            } catch (ValidationException e) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("ValidationError");
                alert.setHeaderText("Part not valid");
                alert.setContentText(e.getMessage());
                alert.showAndWait();
            }
        }
    }
    /**
     * @param url
     * @param rb 
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
       if (modifyPart == null) {
            PartsPageLabel.setText("Add Part");
            int partAutoID = Inventory.getPartsCount();
            PartsIDField.setText("ID: " + partAutoID);
            
            isInHouse = true;
            PartsMfgLabel.setText("Machine ID");
        }
        else{
            PartsPageLabel.setText("Modify Part");
            PartsIDField.setText(Integer.toString(modifyPart.getPartID()));
            PartsNameField.setText(modifyPart.getName());
            PartsInvField.setText(Integer.toString(modifyPart.getInStock()));
            PartsPPUField.setText(Double.toString(modifyPart.getPrice()));
            PartsMinField.setText(Integer.toString(modifyPart.getMin()));
            PartsMaxField.setText(Integer.toString(modifyPart.getMax()));
            
            // Since modifyPart belongs to the subclass, we have to cast it to
            //   the appropriate super class.
            if (modifyPart instanceof InHouse) {
                PartsMfgField.setText(Integer.toString(((InHouse) modifyPart).getMachineID()));
                
                PartsMfgLabel.setText("Machine ID");
                PartsInHouseRadioButton.setSelected(true);

            } else {
                PartsMfgField.setText(((OutSourced) modifyPart).getCompanyName());
                PartsMfgLabel.setText("Company Name");
                PartsOutsourcedRadioButton.setSelected(true);
            }
        }
    }
    
}
