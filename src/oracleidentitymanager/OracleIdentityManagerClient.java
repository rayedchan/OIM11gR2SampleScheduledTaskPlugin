package oracleidentitymanager;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.security.auth.login.LoginException;
import oracle.iam.identity.exception.UserAlreadyExistsException;
import oracle.iam.identity.exception.UserCreateException;
import oracle.iam.identity.exception.UserSearchException;
import oracle.iam.identity.exception.ValidationFailedException;
import oracle.iam.identity.usermgmt.api.UserManager;
import oracle.iam.identity.usermgmt.vo.User;
import oracle.iam.platform.OIMClient;
import oracle.iam.platform.authz.exception.AccessDeniedException;
import oracle.iam.platform.entitymgr.vo.SearchCriteria;
import oracle.iam.provisioning.api.ProvisioningService;
import oracle.iam.provisioning.exception.AccountNotFoundException;
import oracle.iam.provisioning.exception.GenericProvisioningException;
 
/**
 * Uses the OIMClient to access services in Oracle
 * Identity Manager.
 */
 
public class OracleIdentityManagerClient
{
    public static final String OIM_HOSTNAME = "localhost";
    public static final String OIM_PORT = "14000";
    public static final String OIM_PROVIDER_URL = "t3://"+ OIM_HOSTNAME + ":" + OIM_PORT;
    public static final String OIM_USERNAME = "xelsysadm";
    public static final String OIM_PASSWORD = "Password1";
    public static final String OIM_CLIENT_HOME = "/home/oracle/Desktop/oimclient";
    public static final String AUTHWL_PATH = OIM_CLIENT_HOME + "/conf/authwl.conf";
 
    public static void main(String[] args) throws AccountNotFoundException, oracle.iam.platform.authopss.exception.AccessDeniedException, GenericProvisioningException
    {
        OIMClient oimClient = null;
 
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
 
            // Lookup a service
            UserManager usermgr = oimClient.getService(UserManager.class);
   
            // Build new User
            String userKey = null; // USR_KEY
            String firstName = "Jenny"; // USR_FIRST_NAME
            String lastName = "Lee"; // USR_LAST_NAME
            String userLogin = "jlee"; // USR_LOGIN
            String organizationKey = "1"; // ACT_KEY  [Organization]
            String userType = "End-User"; // USR_TYPE
            String role = "Full-Time"; // USR_EMP_TYPE [User Type]
            
            User newUser = new User(userKey);
            newUser.setLogin(userLogin);
            newUser.setFirstName(firstName);
            newUser.setLastName(lastName);
            newUser.setOrganizationKey(organizationKey);
            newUser.setUserType(userType);
            newUser.setEmployeeType(role);
            
            // Call a method from a service
            usermgr.create(newUser);   
        } 
        

        
        catch (ValidationFailedException ex) {
            Logger.getLogger(OracleIdentityManagerClient.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UserAlreadyExistsException ex) {
            Logger.getLogger(OracleIdentityManagerClient.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UserCreateException ex) {
            Logger.getLogger(OracleIdentityManagerClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (LoginException ex)
        {
            Logger.getLogger(OracleIdentityManagerClient.class.getName()).log(Level.SEVERE, null, ex);
        }
 
        catch (AccessDeniedException ex)
        {
            Logger.getLogger(OracleIdentityManagerClient.class.getName()).log(Level.SEVERE, null, ex);
 
        }
 
 
        finally
        {
            // Logout user from OIMClient
            if(oimClient != null)
                oimClient.logout();
        }
    }
}
