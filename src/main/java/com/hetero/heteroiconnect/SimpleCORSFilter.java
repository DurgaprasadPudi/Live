package com.hetero.heteroiconnect;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;  
import java.io.IOException;

@Component
public class SimpleCORSFilter implements Filter {

    private final Logger log = LoggerFactory.getLogger(SimpleCORSFilter.class);

    // Set inactivity timeout to 30 seconds (30,000 milliseconds)
      //private static final long EXPIRY = 10 * 1000L; // 30 seconds
    
     //private static final long EXPIRY = 12 * 60 * 60 * 1000L; // 2 hours in milliseconds
     
     private static final long EXPIRY = 2 * 60 * 60 * 1000L; // 2 hours in milliseconds
  
    
    public SimpleCORSFilter() {
        log.info("SimpleCORSFilter initialized with 30-second timeout");
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        String origin = request.getHeader("Origin");
        
        

        // Allow specific trusted origins (can be expanded to others as needed)
//        if ("http://localhost:4200".equals(origin) || "https://your-prod-domain.com".equals(origin)) {
//            response.setHeader("Access-Control-Allow-Origin", origin);
//        }
        
        if (origin != null && !origin.isEmpty()) {
            response.setHeader("Access-Control-Allow-Origin", origin);
            response.setHeader("Vary", "Origin");
        }


        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE, PUT");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Accept, X-Requested-With, remember-me, X-Last-Active,Access");

        // Allow preflight requests to pass
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);
            return;
        }

        // Check the X-Last-Active header for inactivity
        //String lastActive = "NA";
        
        String lastActive = request.getHeader("X-Last-Active");
        
        //String Access="NA";
        
        String  Access=request.getHeader("Access");
        
        //System.err.println(lastActive+"inactiveTooLong");

        if(lastActive==null&&"heteroiconnect".equals(Access))
        {
        	 response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401 Unauthorized
             return;
        }
        
        else if (lastActive != null) {
            try {
                long lastTime = Long.parseLong(lastActive);
                long now = System.currentTimeMillis();
                boolean inactiveTooLong = (now - lastTime > EXPIRY); // 30-second inactivity check
                 
                //System.out.println(inactiveTooLong+"inactiveTooLong");

                if (inactiveTooLong) {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401 Unauthorized
                    return;
                }
            } catch (NumberFormatException e) {
                log.warn("Invalid X-Last-Active header format.");
            }
        }

        // Continue the filter chain if not inactive
        chain.doFilter(req, res);
    }

    @Override
    public void init(FilterConfig filterConfig) {}
    @Override
    public void destroy() {}
}
