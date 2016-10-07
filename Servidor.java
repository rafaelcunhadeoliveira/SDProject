import java.util.ArrayList;
import java.util.TreeMap;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * Created by Rafael on 15/09/2016.
 */
public class Servidor {

    public TreeMap <String, String> database = new TreeMap<>();
    public TreeMap<String, MetadataNode> metadata = new TreeMap<>();
    public String response = "";
    public int  responseCode = 400;
    private final int tamanhoNo = 1024;
    public DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    public Date date = new Date();
    String temp = "";
    int length = 0;
    String metadataCreation = "";
    String metadataModification = "";
    String metadataVersion = "";
    String lengthStatement = "";

    MetadataNode nodeValue = new MetadataNode(dateToString(date), "-");

    public Servidor(String key, String value)
    {
        database.put(key, value);
        metadata.put(key, nodeValue);
    }
    public boolean isEmpty()
    {
        return database.isEmpty();
    }

    public boolean Search (String key)
    {
        return database.containsKey(key);
    }

    public String dateToString(Date data){
        String tempData = dateFormat.format(data);
        return tempData;
    }

    public  String GetRequest(String key)
    {
        try {
            if (Search(key) == true) {
                responseCode = 200;
                length = database.get(key).length();
                metadata.get(key).value();
                metadataCreation = metadata.get(key).creationStatement;
                metadataModification = metadata.get(key).modificationStatement;
                metadataVersion = metadata.get(key).versionStatement;
                lengthStatement = "Content-Length: "+length;
                return database.get(key);
            } else {
                responseCode = 404;
                return "Not Found";
            }
        } catch (Exception e) {
            responseCode = 505;
            return "Bad Request";
        }
    }

    public String PostRequest(String key, String value)
    {
        try {
            if (value.length() < tamanhoNo) {
                if (Search(key) != true) {
                    database.putIfAbsent(key, value);
                    Date cDate = new Date();
                    temp = dateToString(cDate);
                    MetadataNode nValue = new MetadataNode(temp, "-");
                    metadata.put(key, nValue);
                    length = database.get(key).length();
                    metadata.get(key).value();
                    metadataCreation = metadata.get(key).creationStatement;
                    metadataModification = metadata.get(key).modificationStatement;
                    metadataVersion = metadata.get(key).versionStatement;
                    lengthStatement = "Content-Length: "+length;
                    responseCode = 200;
                    return "POST in: "+key+" as: "+value;
                } else {
                    responseCode = 400;
                    return "Bad Request";
                }
            } else {
                responseCode = 400;
                return "Bad Request";
            }
        } catch (Exception e) {
            responseCode = 505;
            return "Bad Request";
        }
    }

    public String DeleteRequest(String key)
    {
        try {
            if (Search(key) == true) {
                metadata.get(key).value();
                metadataCreation = metadata.get(key).creationStatement;
                metadataModification = metadata.get(key).modificationStatement;
                metadataVersion = metadata.get(key).versionStatement;
                lengthStatement = "Content-Length: "+length;
                database.remove(key);
                metadata.remove(key);
                responseCode = 200;

                return "Node Deleted";
            } else {
                responseCode = 404;
                return "Not Found";
            }
        } catch (Exception e) {
            responseCode = 505;
            return "Bad Request";
        }
    }

    public String PutRequest(String key, String value) {
        try {
            if (value.length() < tamanhoNo) {
                if (Search(key) == true) {
                    database.put(key, value);
                    Date mDate = new Date();
                    temp = dateToString(mDate);
                    metadata.get(key).setModification(temp);
                    metadata.get(key).newVersion();
                    length = database.get(key).length();
                    metadata.get(key).value();
                    metadataCreation = metadata.get(key).creationStatement;
                    metadataModification = metadata.get(key).modificationStatement;
                    metadataVersion = metadata.get(key).versionStatement;
                    lengthStatement = "Content-Length: "+length;
                    responseCode = 200;
                    return "PUT in: "+key+" as: "+value;
                } else {
                    responseCode = 404;
                    return "Not Found";
                }
            } else {
                responseCode = 505;
                return "Bad Request";
            }
        } catch (Exception e) {
            responseCode = 505;
            return "Bad Request";
        }
    }

    public  String HeadRequest(String key)
    {
        try {
            if (Search(key) == true) {
                responseCode = 200;
                metadata.get(key).value();
                metadataCreation = metadata.get(key).creationStatement;
                metadataModification = metadata.get(key).modificationStatement;
                metadataVersion = metadata.get(key).versionStatement;
                lengthStatement = "Content-Length: "+length;
                response = "The metadatas of the node: "+key+ " are: " + metadata.get(key).creationStatement+" "+metadata.get(key).modificationStatement +" "+metadata.get(key).versionStatement;;
                return response;
            } else {
                responseCode = 404;
                return "Not Found";
            }
        } catch (Exception e) {
            responseCode = 505;
            return "Bad Request";
        }
    }

}

