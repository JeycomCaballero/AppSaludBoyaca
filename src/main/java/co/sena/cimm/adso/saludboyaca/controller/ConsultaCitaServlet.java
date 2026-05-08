package co.sena.cimm.adso.saludboyaca.controller;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.WebServlet;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import co.sena.cimm.adso.saludboyaca.dao.CitaDAOImpl;
import co.sena.cimm.adso.saludboyaca.dao.PacienteDAO;
import co.sena.cimm.adso.saludboyaca.dao.PacienteDAOImpl;
import co.sena.cimm.adso.saludboyaca.dto.Cita;
import co.sena.cimm.adso.saludboyaca.dto.Paciente;
import co.sena.cimm.adso.saludboyaca.util.CaptchaGenerator;
import co.sena.cimm.adso.saludboyaca.util.PDFGenerator;

@WebServlet("/consulta-cita")
public class ConsultaCitaServlet extends HttpServlet {

    private CitaDAOImpl dao;

    @Override
    public void init() {
        dao = new CitaDAOImpl();
    }

    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String accion = request.getParameter("accion");

        System.out.println("Accion: " + accion);
        
        if ("descargar".equals(accion)) {
            descargarPDF(request, response);
            return;
        }

        generarCaptcha(request);
        request.getRequestDispatcher("/views/consulta_cita.jsp").forward(request, response);
    }

  
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String documento = request.getParameter("documento");
        String captcha = request.getParameter("captcha");

        HttpSession session = request.getSession();
        String captchaReal = (String) session.getAttribute("captchaText");

        if (!CaptchaGenerator.validarCaptcha(captcha, captchaReal)) {
            request.setAttribute("error", "CAPTCHA incorrecto");
            generarCaptcha(request);
            request.getRequestDispatcher("/views/consulta_cita.jsp").forward(request, response);
            return;
        }

        try {
            PacienteDAO pacienteDAO = new PacienteDAOImpl();
            Paciente paciente = pacienteDAO.buscarPorDocumento(documento);

            if (paciente == null) {
                request.setAttribute("error", "Paciente no encontrado");
                generarCaptcha(request);
                request.getRequestDispatcher("/views/consulta_cita.jsp").forward(request, response);
                return;
            }

            List<Cita> citas = dao.buscarPorDocumento(documento);

            request.setAttribute("registros", citas);
            request.setAttribute("paciente", paciente);

            session.setAttribute("documentoConsultado", documento);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Error en la consulta");
        }

        request.getRequestDispatcher("/views/consulta_cita.jsp").forward(request, response);
    }


    private void generarCaptcha(HttpServletRequest request) {

        HttpSession session = request.getSession();

        String texto = CaptchaGenerator.generarTextoCaptcha();
        String imagen = CaptchaGenerator.generarImagenCaptcha(texto);

        session.setAttribute("captchaText", texto);
        request.setAttribute("captchaImage", imagen);
    }

    private void descargarPDF(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        String idParam = request.getParameter("id");

        if (idParam == null) {
            response.sendRedirect("consulta-cita");
            return;
        }

        try {
            int id = Integer.parseInt(idParam);

            Cita cita = dao.buscarPorId(id);

            if (cita == null) {
                response.sendRedirect("consulta-cita");
                return;
            }

            byte[] pdf = PDFGenerator.generarComprobanteCita(cita);

            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition",
                    "attachment; filename=comprobante_cita_" + id + ".pdf");

            response.setContentLength(pdf.length);

            try (OutputStream out = response.getOutputStream()) {
                out.write(pdf);
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("consulta-cita");
        }
    }
}
