package Repository;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.net.http.HttpResponse;

public class SerObj {
    public static void main(){

    }

    public static String serHttpResponse(HttpResponse<String> httpresponse){
        String retour ="";

        try {
            Context ctx = new InitialContext();

            ctx.bind("dest", httpresponse);

            retour = (String)ctx.lookup("dest");


        }
        catch (NamingException ex){
            System.err.println("Naming exception : " + ex);
        }

        return retour;
    }
/*
    public static HttpResponse<String> serString(String string){
        HttpResponse retour=null;

        try {
            Context ctx = new InitialContext();

            ctx.bind("dest", string);

            retour = (HttpResponse<String>)ctx.lookup("dest");


        }
        catch (NamingException ex){
            System.err.println("Naming exception : " + ex);
        }

        return retour;
    }
    */
}
