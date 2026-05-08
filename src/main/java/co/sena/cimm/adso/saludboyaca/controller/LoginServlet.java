package co.sena.cimm.adso.saludboyaca.controller;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.WebServlet;

import java.io.IOException;
import java.util.Date;

import co.sena.cimm.adso.saludboyaca.dao.UsuarioDAO;
import co.sena.cimm.adso.saludboyaca.dao.UsuarioDAOImpl;
import co.sena.cimm.adso.saludboyaca.dao.OTPTokenDAO;
import co.sena.cimm.adso.saludboyaca.dao.OTPTokenDAOImpl;
import co.sena.cimm.adso.saludboyaca.dto.Usuario;
import co.sena.cimm.adso.saludboyaca.util.OTPService;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private UsuarioDAO usuarioDAO;
    private OTPTokenDAO otpDAO;

    @Override
    public void init() throws ServletException {
        try {
            usuarioDAO = new UsuarioDAOImpl();
            otpDAO = new OTPTokenDAOImpl();
        } catch (Exception e) {
            throw new ServletException("Error inicializando DAOs", e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.getRequestDispatcher("/views/login.jsp")
                .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        if (username == null || username.trim().isEmpty()
                || password == null || password.trim().isEmpty()) {

            request.setAttribute("msg", "Todos los campos son obligatorios");
            request.getRequestDispatcher("/views/login.jsp").forward(request, response);
            return;
        }

        try {

            Usuario usuario = usuarioDAO.validarLogin(username, password);

            if (usuario == null) {

                request.setAttribute("msg", "Credenciales incorrectas");
                request.getRequestDispatcher("/views/login.jsp").forward(request, response);
                return;
            }

            HttpSession oldSession = request.getSession(false);
            if (oldSession != null) {
                oldSession.invalidate();
            }

            HttpSession session = request.getSession(true);

            String otp = OTPService.generarOTP();

            Date expira = new Date(System.currentTimeMillis() + (5 * 60 * 1000));

            try {
                otpDAO.marcarUsado(usuario.getId(), "ALL");
                otpDAO.insertar(usuario.getId(), otp, expira);
            } catch (Exception ignore) {
            }

            try {
                String html = buildOtpEmail(usuario, otp);
                OTPService.enviarOTP(usuario.getEmail(), "Código de verificación - SaludBoyacá", html);
            } catch (Exception ex) {

                request.setAttribute("msg", "No se pudo enviar el OTP. Intenta nuevamente.");
                request.getRequestDispatcher("/views/login.jsp")
                        .forward(request, response);
                return;
            }

            session.setAttribute("usuario", usuario);
            session.setAttribute("rol", usuario.getRol());
            session.setAttribute("otpVerificado", false);

            response.sendRedirect(request.getContextPath() + "/otp");

        } catch (Exception e) {
            e.printStackTrace();

            request.setAttribute("msg", "Error interno del servidor");
            request.getRequestDispatcher("/views/login.jsp").forward(request, response);
        }
    }

    private String buildOtpEmail(Usuario usuario, String otp) {

        return "<!DOCTYPE html>"
                + "<html>"
                + "<head><meta charset='UTF-8'></head>"
                + "<body style='margin:0; padding:0; background:#eef2f7; font-family:Arial, sans-serif;'>"
                + "<div style='max-width:520px; margin:40px auto; background:#fff; border-radius:16px; overflow:hidden; box-shadow:0 10px 25px rgba(0,0,0,0.1);'>"
                + "<div style='background:linear-gradient(135deg,#1A5276,#39A900); padding:22px; text-align:center; color:white;'>"
                + "<h2 style='margin:0;'>SaludBoyacá</h2>"
                + "<p style='margin:5px 0 0; font-size:13px;'>Código de verificación</p>"
                + "</div>"
                + "<div style='padding:30px; text-align:center;'>"
                + "<p style='font-size:16px; color:#333;'>"
                + "Hola <b>" + usuario.getNombres() + "</b>"
                + "</p>"
                + "<p style='color:#666; font-size:14px;'>"
                + "Usa este código para continuar con tu inicio de sesión"
                + "</p>"
                + "<div style='display:inline-block; margin:25px 0; padding:18px 28px; font-size:32px; letter-spacing:8px; font-weight:bold; color:#1A5276; background:#f4f8ff; border:2px dashed #39A900; border-radius:12px;'>"
                + otp
                + "</div>"
                + "<p style='font-size:13px; color:#888;'>"
                + "Expira en <b>5 minutos</b>"
                + "</p>"
                + "<p style='font-size:12px; color:#aaa; margin-top:20px;'>"
                + "Si no solicitaste este código, ignora este mensaje."
                + "</p>"
                + "</div>"
                + "<div style='background:#f7f7f7; padding:12px; text-align:center; font-size:11px; color:#999;'>"
                + "© SaludBoyacá - Sistema de autenticación"
                + "</div>"
                + "</div>"
                + "</body>"
                + "</html>";
    }
}
