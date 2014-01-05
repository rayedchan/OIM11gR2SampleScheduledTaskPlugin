package oim.scheduledtasks;

import java.util.HashMap;
import oracle.iam.identity.usermgmt.api.UserManager;
import oracle.iam.identity.usermgmt.vo.User;
import oracle.iam.platform.Platform;
import oracle.iam.scheduler.vo.TaskSupport;

public class CreateNewUser extends TaskSupport
{
    @Override
    public void execute(HashMap hm) throws Exception 
    {
        // Lookup a service
        UserManager usermgr = Platform.getService(UserManager.class);

        // Build new User
        String userKey = null; // USR_KEY
        String firstName = (String) hm.get("First Name"); // USR_FIRST_NAME
        String lastName = (String) hm.get("Last Name"); // USR_LAST_NAME
        String userLogin = (String) hm.get("User Login"); // USR_LOGIN
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

    @Override
    public HashMap getAttributes() 
    {
        return null;
    }

    @Override
    public void setAttributes() 
    {
        
    }
}
