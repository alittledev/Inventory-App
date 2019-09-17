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
public class OutSourced extends Part{
    //Name of the company that makes the part
    private String companyName;
    
    //
   
    //set the company's names
    public void setCompanyName(String companyName){
        this.companyName = companyName;
    }
    
    //Get the company's name
    public String getCompanyName(){
        return companyName;
    }
}