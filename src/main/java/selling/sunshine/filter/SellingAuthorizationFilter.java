package selling.sunshine.filter;

import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.CollectionUtils;
import org.apache.shiro.web.filter.authz.AuthorizationFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.util.List;

/**
 * Created by sunshine on 7/5/16.
 */
public class SellingAuthorizationFilter extends AuthorizationFilter {
    private Logger logger = LoggerFactory.getLogger(SellingAuthorizationFilter.class);

    @Override
    protected boolean isAccessAllowed(ServletRequest servletRequest, ServletResponse servletResponse, Object o) throws Exception {
        Subject subject = getSubject(servletRequest, servletResponse);
        String[] rolesArray = (String[]) o;

        if (rolesArray == null || rolesArray.length == 0) {
            //no roles specified, so nothing to check - allow access.
            return false;
        }

        List<String> roles = CollectionUtils.asList(rolesArray);
        boolean[] hasRoles = subject.hasRoles(roles);
        for (boolean hasRole : hasRoles) {
            if (hasRole) {
                return true;
            }
        }
        return false;
    }
}
