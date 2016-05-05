package selling.sunshine.security;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import selling.sunshine.model.Role;
import selling.sunshine.model.User;
import selling.sunshine.service.UserService;
import selling.sunshine.utils.ResponseCode;
import selling.sunshine.utils.ResultData;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by sunshine on 4/21/16.
 */
public class ShiroRealm extends AuthorizingRealm {

    @Autowired
    private UserService userService;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        String username = String.valueOf(principalCollection.getPrimaryPrincipal());
        Map<String, Object> condition = new HashMap<String, Object>();
        condition.put("username", username);
        ResultData fetchResponse = userService.fetchUser(condition);
        if (fetchResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
            User user = (User) fetchResponse.getData();
            Role role = user.getRole();
            info.addRole(role.getName());
        }
        return info;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
        String username = token.getUsername();
        String password = new String(token.getPassword());
        User user = new User(username, password);
        ResultData loginResponse = userService.login(user);
        if (loginResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
            user = (User) loginResponse.getData();
            if (user != null) {
                Subject subject = SecurityUtils.getSubject();
                if (subject != null) {
                    Session session = subject.getSession();
                    session.setAttribute("current", user);
                }
                return new SimpleAuthenticationInfo(user, token.getPassword(), getName());
            }
        }
        return null;
    }
}
