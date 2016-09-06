/**
 * Created by aoliu on 2016/8/22.
 */
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;
import sun.net.www.protocol.http.HttpURLConnection;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.LinkedHashSet;
import java.util.Iterator;
import java.util.LinkedList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

class URLClass
{
    URL strURL;
    HttpURLConnection connection;
    URLClass(String url)throws Exception {
        strURL= new URL(url);
    }
    protected void connectToURL() throws  Exception{
        connection = (HttpURLConnection)strURL.openConnection();
        connection.connect();
    }
    public boolean equal(URLClass cmpUrl){
        return(cmpUrl.strURL == this.strURL);
    }
    public int hashCode(){
        return strURL.hashCode();
    }
}

class GetURL extends URLClass
{
    private String curLine = "";
    private String content = "";


    GetURL(String url)throws Exception{
     //   super.strURL = new URL(url);
        super(url);
    }

    String getContent() {
        return content;
    }

    void connectGetContent()throws Exception{
        connectToURL();
        InputStream is = super.connection.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        while((curLine = reader.readLine())!= null){
            content  = content + curLine +"\r\n";
        }

        is.close();
    }
}


class ParseContent
{
    Document doc;

    ParseContent(String message)
    {

        doc = Jsoup.parse(message);
        System.out.println("doc.title"+doc.title());
    }
    void getElment(LinkedList<GetURL> hashURL)throws Exception{
        Element node = doc.body();
        Elements es = node.select("a");
        int i=0;
        for(Iterator it = es.iterator(); it.hasNext();){
            Element e = (Element)it.next();
            String tempURL=e.attr("href");
            if((tempURL.length()>0)||(tempURL!=null))
            {
                GetURL addURL = new GetURL(tempURL);
                hashURL.add(addURL);
                System.out.println("content:"+tempURL);
            }

           // System.out.println("nodeName:"+it.toString()+"outerHtml:"+e.attr("href"));
            i++;
        }
        System.out.println("count i: "+i);

    }

}



//public class
public class GrapInfoTool {
    public static LinkedList<GetURL> listURL = new LinkedList<GetURL>();

    public static void graphInfo(GetURL connectURL) throws Exception{
        //
        connectURL.connectGetContent();

        System.out.println(connectURL.getContent());

        ParseContent htmlParse = new ParseContent(connectURL.getContent());
        htmlParse.getElment(listURL);
        listURL.remove(listURL.indexOf(connectURL));
        graphInfo(listURL.pop());
    }

    public static void main(String [] args)throws Exception{
        String strURL = "http://www.zhaopin.com/";

        GetURL connectURL = new GetURL(strURL);
        listURL.add(connectURL);
        graphInfo(connectURL);
    }
}
