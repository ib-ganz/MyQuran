package ib.ganz.myquran.network;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by GrT on 6/7/2017.
 */

public class MyException extends RuntimeException
{
    String message;
    String response;
    
    public MyException(String s)
    {
        this(s, "");
    }

    public MyException(String s, String response)
    {
        super(s);
        this.response = response;

        try
        {
            JSONObject jo = new JSONObject(s);
            message = jo.getString("message");
        }
        catch (JSONException e)
        {
            message = s;
        }
    }
    
    @Override
    public String getMessage()
    {
        return message;
    }

    public String getResponse()
    {
        return response;
    }
}
