/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nxml2json;

import com.google.gson.annotations.SerializedName;

/**
 *
 * @author Andrey
 */
public class PersonGroup {
    public Name[] name;
    @SerializedName("person-group-type") public String personGroupType;
    
    public PersonGroup(){
        
    }
}
