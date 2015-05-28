/* David Lee, A00783233 */

package a00783233.a1;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
 
/* Forces responses to use UTF-8 encoding, ensuring that international characters display properly */
public class UTF8Filter implements Filter {
 
    public void init(FilterConfig filterConfig) {}
 
    public void destroy() {}
 
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {
    	if (request.getCharacterEncoding() == null)
    		request.setCharacterEncoding("UTF-8");
        filterChain.doFilter(request, response);
    }
}