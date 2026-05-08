package co.sena.cimm.adso.saludboyaca.util;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebFilter("/*")
public class LocaleFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpSession session = req.getSession();

        String lang = request.getParameter("lang");

        if (lang != null && !lang.isEmpty()) {
            session.setAttribute("lang", lang);
        }

        if (session.getAttribute("lang") == null) {
            session.setAttribute("lang", "es");
        }

        chain.doFilter(request, response);
    }
}