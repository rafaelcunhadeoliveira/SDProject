/**
 * Created by Rafael on 15/09/2016.
 */
public class Resposta {
    private int responseCode;
    private String responseMessage = "";

    public String resp(int code)
    {
        this.responseCode = code;

        switch(responseCode)
        {
            case 200:
                responseMessage = "Ok";
                break;
            case 301:
                responseMessage = "Moved Permanently";
                break;
            case 400:
                responseMessage = "Bad Request";
                break;
            case 404:
                responseMessage = "Not Found";
                break;
            case 505:
                responseMessage = "Version not supported";
                break;
        }
        return responseMessage;
    }
}