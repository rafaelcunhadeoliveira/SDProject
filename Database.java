import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * Created by Rafael on 10/10/2016.
 */
public class Database {
    public Map<String, String> database = new ConcurrentSkipListMap<>();
    public Map<String, MetadataNode> metadata = new ConcurrentSkipListMap<>();
    public String response = "";
    public int  responseCode = 400;
    private final int tamanhoNo = 1024;
    public DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    public Date date = new Date();
    String temp = "";
    int length = 0;
    String metadataCreation = "Creation: ";
    String metadataModification = "Modification: ";
    String metadataVersion = "Version: ";
    String lengthStatement = "Content-Length:";

    public Database(String key, String value){
        database.put(key, value);
        metadata.put(key, nodeValue);
    }

    MetadataNode nodeValue = new MetadataNode(dateToString(date), "-");
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
//                    return "";
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
                metadataCreation = "";
                metadataModification = "";
                metadataVersion = "";
                lengthStatement = "";
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
                    metadataCreation = "Creation: ";
                    metadataModification = "Modification: ";
                    metadataVersion = "Version: ";
                    lengthStatement = "Content-Length:";
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
