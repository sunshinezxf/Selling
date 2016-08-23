package selling.sunshine.security;

import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import common.sunshine.model.selling.user.Role;
import common.sunshine.model.selling.user.User;
import selling.sunshine.service.UserService;
import common.sunshine.utils.ResponseCode;
import common.sunshine.utils.ResultData;

/**
 * Created by sunshine on 4/21/16.
 */
public class ShiroRealm extends AuthorizingRealm {

    @Autowired
    private UserService userService;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        User user = (User) principalCollection.getPrimaryPrincipal();
        Role role = user.getRole();
        info.addRole(role.getName());
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
                return new SimpleAuthenticationInfo(user, token.getPassword(), getName());
            }
        }
        return null;
    }
}
