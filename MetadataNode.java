/**
 * Created by Rafael on 16/09/2016.
 */
public class MetadataNode {

    public final String creation;
    String creationStatement = "";
    public String modification = "";
    public String modificationStatement = "";
    public int version = 0;
    public String versionStatement = "";

    public MetadataNode(String creationDate, String modData){
        this.creation = creationDate;
        this.modification = modData;
        this.version = 0;
    }

    public String getCreation(){
        return creation;
    }

    public String getModification(){
        return modification;
    }

    public int getVersion(){
        return version;
    }

    public void setModification(String newDate){
        this.modification = newDate;
    }

    public void newVersion(){
        version = version+1;
    }

    public String stringVersion(){
        String versionString = String.valueOf(this.version);
        return versionString;
    }

    public void value(){
        if(version == 0){
            creationStatement = "Creation: "+creation;
            modificationStatement = "Modification: There were no modifications yet";
            versionStatement = "Version: "+this.stringVersion();


        }
        else{
            creationStatement = "Creation: "+creation;
            modificationStatement = "Modification: "+modification;
            versionStatement = "Version: "+this.stringVersion();
        }
    }
}