package oracleidentitymanager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Hashtable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.security.auth.login.LoginException;
import oracle.iam.platform.OIMClient;
import oracle.iam.platform.pluginframework.PluginException;
import oracle.iam.platformservice.api.PlatformService;
import oracle.iam.platformservice.exception.PlatformServiceAccessDeniedException;

public class RegisterPlugin
{
 
    public static final String OIM_HOSTNAME = "localhost";
    public static final String OIM_PORT = "14000";
    public static final String OIM_PROVIDER_URL = "t3://"+ OIM_HOSTNAME + ":" + OIM_PORT;
    public static final String OIM_USERNAME = "xelsysadm";
    public static final String OIM_PASSWORD = "Password1";
    public static final String OIM_CLIENT_HOME = "/home/oracle/Desktop/oimclient";
    public static final String AUTHWL_PATH = OIM_CLIENT_HOME + "/conf/authwl.conf";
    
    public static void main (String args[])
    {
        OIMClient oimClient = null;
        FileInputStream fis = null;
         
        try 
        { 
             //Set system properties required for OIMClient
            System.setProperty("java.security.auth.login.config", AUTHWL_PATH);
            System.setProperty("APPSERVER_TYPE", "wls");  

            // Create an instance of OIMClient with OIM environment information 
            Hashtable env = new Hashtable();
            env.put(OIMClient.JAVA_NAMING_FACTORY_INITIAL, "weblogic.jndi.WLInitialContextFactory");
            env.put(OIMClient.JAVA_NAMING_PROVIDER_URL, OIM_PROVIDER_URL);
            oimClient = new OIMClient(env);

            // Login to OIM with the approriate credentials
            oimClient.login(OIM_USERNAME, OIM_PASSWORD.toCharArray());
            
            String fileName = "/home/oracle/Desktop/mycustomplugin.zip";
            PlatformService service = oimClient.getService(PlatformService.class);
            File zipFile = new File(fileName);
            fis = new FileInputStream(zipFile);
            int size = (int) zipFile.length();
            byte[] b = new byte[size];
            int bytesRead = fis.read(b, 0, size);
            
            while (bytesRead < size) 
            {
                bytesRead += fis.read(b, bytesRead, size - bytesRead);
            }
            
            service.registerPlugin(b);
            //service.unRegisterPlugin(pluginID, version);
        }
        
        catch (FileNotFoundException ex) 
        {
            Logger.getLogger(RegisterPlugin.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        catch (PlatformServiceAccessDeniedException ex) 
        {
            Logger.getLogger(RegisterPlugin.class.getName()).log(Level.SEVERE, null, ex);
        } 
        
        catch (PluginException ex)
        {
            Logger.getLogger(RegisterPlugin.class.getName()).log(Level.SEVERE, null, ex);
        } 
        
        catch (IOException ex) 
        {
            Logger.getLogger(RegisterPlugin.class.getName()).log(Level.SEVERE, null, ex);
        }  
        
        catch (LoginException ex) 
        {
            Logger.getLogger(RegisterPlugin.class.getName()).log(Level.SEVERE, null, ex);
        } 
        
        finally 
        {
            // Logout user from OIMClient
            if(oimClient != null)     
                oimClient.logout();
            try 
            {
                fis.close();                   
            } 
            
            catch (IOException ex) 
            {
                Logger.getLogger(RegisterPlugin.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }  
}
