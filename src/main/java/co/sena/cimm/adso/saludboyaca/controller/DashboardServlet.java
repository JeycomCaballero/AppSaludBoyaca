package co.sena.cimm.adso.saludboyaca.controller;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;

import co.sena.cimm.adso.saludboyaca.dao.CitaDAO;
import co.sena.cimm.adso.saludboyaca.dao.CitaDAOImpl;
import co.sena.cimm.adso.saludboyaca.dto.Usuario;
import co.sena.cimm.adso.saludboyaca.dto.Cita;
import jakarta.servlet.annotation.WebServlet;

@WebServlet("/dashboard")
public class DashboardServlet extends HttpServlet {

    private CitaDAO citaDAO;

    @Override
    public void init() throws ServletException {
        try {
            citaDAO = new CitaDAOImpl();
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        Boolean otp = (Boolean) session.getAttribute("otpVerificado");

        if (usuario == null || otp == null || !otp) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        try {
            List<Cita> citas = citaDAO.listar();

            request.setAttribute("listaCitas", citas);
            request.setAttribute("totalCitas", citas.size());

            request.setAttribute("citasHoy", citas.size());
            request.setAttribute("citasPendientes", citas.size());
            request.setAttribute("citasMes", citas.size());
            request.setAttribute("totalPacientes", citas.size());

        } catch (Exception e) {
            request.setAttribute("error", "Error cargando dashboard");
        }

        request.getRequestDispatcher("/views/dashboard.jsp").forward(request, response);
    }
}