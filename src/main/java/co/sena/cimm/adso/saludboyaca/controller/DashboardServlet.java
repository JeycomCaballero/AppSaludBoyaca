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
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

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
            int citasHoy = 0;
            int citasPendientes = 0;
            int citasMes = 0;

            Set<Integer> pacientes = new HashSet<>();

            LocalDate hoy = LocalDate.now();

            for (Cita c : citas) {

                if ("CONFIRMADA".equalsIgnoreCase(c.getEstado())) {
                    citasPendientes++;
                }

                pacientes.add(c.getIdPaciente());

                LocalDate fecha = new java.sql.Date(c.getFechaCita().getTime()).toLocalDate();

                if (fecha.equals(hoy)) {
                    citasHoy++;
                }

                if (fecha.getMonthValue() == hoy.getMonthValue()
                        && fecha.getYear() == hoy.getYear()) {

                    citasMes++;
                }
            }

            request.setAttribute("listaCitas", citas);
            request.setAttribute("totalCitas", citas.size());

            request.setAttribute("citasHoy", citasHoy);
            request.setAttribute("citasPendientes", citasPendientes);
            request.setAttribute("citasMes", citasMes);
            request.setAttribute("totalPacientes", pacientes.size());

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Error cargando dashboard");
        }

        request.getRequestDispatcher("/views/dashboard.jsp").forward(request, response);
    }
}
