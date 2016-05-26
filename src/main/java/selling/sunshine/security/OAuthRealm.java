package selling.sunshine.security;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import selling.sunshine.model.Role;
import selling.sunshine.model.User;

/**
 * Created by sunshine on 5/26/16.
 */
public class OAuthRealm extends AuthorizingRealm {
    
    private Logger logger = LoggerFactory.getLogger(OAuthRealm.class);

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
        return null;
    }
}
