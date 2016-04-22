package selling.sunshine.security;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import selling.sunshine.model.Admin;
import selling.sunshine.service.AdminService;
import selling.sunshine.utils.ResponseCode;
import selling.sunshine.utils.ResultData;

/**
 * Created by sunshine on 4/21/16.
 */
public class AdminRealm extends AuthorizingRealm {

    @Autowired
    private AdminService adminService;


    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        return null;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
        String username = token.getUsername();
        String password = new String(token.getPassword());
        Admin admin = new Admin(username, password);
        ResultData loginResponse = adminService.login(admin);
        if (loginResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
            admin = (Admin) loginResponse.getData();
            if (admin != null) {
                Subject subject = SecurityUtils.getSubject();
                if (subject != null) {
                    Session session = subject.getSession();
                    session.setAttribute("current", admin);
                }
                return new SimpleAuthenticationInfo(admin, token.getPassword(), getName());
            }
        }
        return null;
    }
}
