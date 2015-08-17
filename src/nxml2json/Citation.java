/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nxml2json;

import com.google.gson.annotations.SerializedName;
import java.util.HashMap;

/**
 *
 * @author Andrey
 */
public class Citation {

    @SerializedName("reference")
    public String reference = "";
    public String volume;
    @SerializedName("publication-type")
    public String publicationType;
    public String issue;
    public String fpage;
    public String lpage;
    public String year;
    @SerializedName("article-title")
    public String articleTitle = "";

    public HashMap<String, String> source = new HashMap<String, String>();

    @SerializedName("person-group")
    public PersonGroup personGroup = new PersonGroup();
    @SerializedName("pub-id")
    public PubId[] pubId = null;

    public Citation() {

    }

}
