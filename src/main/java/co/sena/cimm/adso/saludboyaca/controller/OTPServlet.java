package co.sena.cimm.adso.saludboyaca.controller;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.Date;

import co.sena.cimm.adso.saludboyaca.dao.OTPTokenDAO;
import co.sena.cimm.adso.saludboyaca.dao.OTPTokenDAOImpl;
import co.sena.cimm.adso.saludboyaca.dto.Usuario;
import co.sena.cimm.adso.saludboyaca.util.OTPService;
import jakarta.servlet.annotation.WebServlet;

@WebServlet("/otp")
public class OTPServlet extends HttpServlet {

    private OTPTokenDAO otpDAO;

    @Override
    public void init() throws ServletException {
        try {
            otpDAO = new OTPTokenDAOImpl();
        } catch (Exception e) {
            throw new ServletException("Error inicializando OTPTokenDAO", e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        Usuario usuario = (Usuario) session.getAttribute("usuario");

        if (usuario == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        String emailEnmascarado = enmascararEmail(usuario.getEmail());
        request.setAttribute("emailEnmascarado", emailEnmascarado);

        session.setAttribute("otpStartTime", System.currentTimeMillis());
        request.getRequestDispatcher("/views/otp_verificacion.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String accion = request.getParameter("action");

        if ("verificar".equals(accion)) {
            verificarCodigo(request, response);
        } else if ("reenviar".equals(accion)) {
            try {
                reenviarCodigo(request, response);
            } catch (Exception e) {
                e.printStackTrace();
                request.setAttribute("error", "Error reenviando OTP");
                request.getRequestDispatcher("/views/otp_verificacion.jsp").forward(request, response);
            }
        }
    }

    private void verificarCodigo(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        Usuario usuario = (Usuario) session.getAttribute("usuario");

        if (usuario == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String codigo = request.getParameter("otp");

        if (codigo == null || codigo.trim().isEmpty()) {
            request.setAttribute("error", "Debes ingresar el código OTP");
            request.getRequestDispatcher("/views/otp_verificacion.jsp").forward(request, response);
            return;
        }

        try {
            boolean valido = otpDAO.validar(usuario.getId(), codigo);

            if (valido) {
                otpDAO.marcarUsado(usuario.getId(), codigo);
                session.setAttribute("otpVerificado", true);

                response.sendRedirect(request.getContextPath() + "/dashboard");
            } else {
                request.setAttribute("error", "Código incorrecto o expirado");
                request.getRequestDispatcher("/views/otp_verificacion.jsp").forward(request, response);
            }

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Error validando OTP");
            request.getRequestDispatcher("/views/otp_verificacion.jsp").forward(request, response);
        }
    }

    private void reenviarCodigo(HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        HttpSession session = request.getSession();
        Usuario usuario = (Usuario) session.getAttribute("usuario");

        if (usuario == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String otp = OTPService.generarOTP();
        Date expira = new Date(System.currentTimeMillis() + (5 * 60 * 1000));

        otpDAO.marcarUsado(usuario.getId(), "ALL");
        otpDAO.insertar(usuario.getId(), otp, expira);

        String html = buildOtpEmail(usuario, otp);

        OTPService.enviarOTP(usuario.getEmail(), "Código de verificación - SaludBoyacá", html);

        response.sendRedirect(request.getContextPath() + "/otp");
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

    private String enmascararEmail(String email) {

        if (email == null || !email.contains("@")) {
            return "***";
        }

        String[] partes = email.split("@");

        String local = partes[0];
        String dominio = partes[1];

        if (local.length() <= 3) {
            return local + "***@" + dominio;
        }

        return local.substring(0, 3) + "***@" + dominio;
    }

    @Override
    public String getServletInfo() {
        return "Servlet OTP con diseño profesional";
    }
}
