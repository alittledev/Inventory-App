/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InvApp.Models;

/**
 *
 * @author allis
 */
public class InHouse extends Part{
    
    //variable for the machine ID of the part
    private int machineID;
    
//    public InHouse(int machineID){
//        this.machineID = machineID;
//    }
//
//    public InHouse() {
//       
//    }
    
    //Set machine ID
    public void setMachineID(int machineID){
        this.machineID = machineID;
    }
    
    //Get machine ID
    public int getMachineID(){
        return machineID;
    }
}