/**
 * Created by aoliu on 2016/8/22.
 */
import sun.net.www.protocol.http.HttpURLConnection;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;


class GetURL
{
    URL strURL;
    private String curLine = "";
    private String content = "";
    private HttpURLConnection connection;

    GetURL(String url) throws Exception{
        this.strURL = new URL(url);
    }
    void connectToURL() throws  Exception{
        connection = (HttpURLConnection)strURL.openConnection();
        connection.connect();
    }
    String getContent()throws Exception{
        InputStream is = connection.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        while((curLine = reader.readLine())!= null){
            content  = content + curLine +"\r\n";
        }

        is.close();
        return content;
    }
}
public class GrapInfoTool {


    public static void main(String [] args)throws Exception{
        String strURL = "http://www.zhaopin.com/";
        GetURL connectURL = new GetURL(strURL);
        connectURL.connectToURL();

        System.out.println(connectURL.getContent());

    }
}
