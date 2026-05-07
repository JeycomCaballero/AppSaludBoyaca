package co.sena.cimm.adso.saludboyaca.util;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebFilter(urlPatterns = {"/*"})
public class AuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        HttpSession session = req.getSession(false);

        String uri = req.getRequestURI();
        String action = req.getParameter("action");

        
        if (uri.contains("/login") || uri.contains("/otp") || uri.contains("/resources")) {
            chain.doFilter(request, response);
            return;
        }

        
        if (session == null || session.getAttribute("usuario") == null) {
            res.sendRedirect(req.getContextPath() + "/login");
            return;
        }

     
        Boolean otpVerificado = (Boolean) session.getAttribute("otpVerificado");

        if (otpVerificado == null || !otpVerificado) {
            res.sendRedirect(req.getContextPath() + "/otp");
            return;
        }

        
        String rol = (String) session.getAttribute("rol");

      
        if (action != null) {

            
            if ("ENFERMERO".equals(rol)) {

                if (action.equals("eliminar")
                        || action.equals("editar")
                        || action.equals("actualizar")) {

                    res.sendRedirect(req.getContextPath() + "/dashboard");
                    return;
                }
            }

            if ("MEDICO".equals(rol)) {

                if (action.equals("usuarios")
                        || action.equals("eliminar")
                        || action.equals("editar")) {

                    res.sendRedirect(req.getContextPath() + "/dashboard");
                    return;
                }
            }

       
            if ("RECEPCIONISTA".equals(rol)) {

                if (action.equals("eliminar")) {
                    res.sendRedirect(req.getContextPath() + "/dashboard");
                    return;
                }
            }
        }

      
        chain.doFilter(request, response);
    }
}
