package co.sena.cimm.adso.saludboyaca.controller;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;

import co.sena.cimm.adso.saludboyaca.dao.PacienteDAOImpl;
import co.sena.cimm.adso.saludboyaca.dto.Paciente;
import co.sena.cimm.adso.saludboyaca.dto.Usuario;
import jakarta.servlet.annotation.WebServlet;

@WebServlet("/pacientes")
public class PacienteServlet extends HttpServlet {

    private PacienteDAOImpl dao;

    @Override
    public void init() throws ServletException {
        try {
            dao = new PacienteDAOImpl();
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        HttpSession session = req.getSession(true);
        String rol = (String) session.getAttribute("rol");
        try {

            List<Paciente> lista = dao.listar();
            req.setAttribute("pacientes", lista);

            req.getRequestDispatcher("/views/pacientes/lista.jsp")
                    .forward(req, res);

        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        try {

            String action = req.getParameter("action");

            if ("nuevo".equals(action)) {
                req.getRequestDispatcher("/views/pacientes/formulario.jsp").forward(req, res);
                return;
            }

            if ("editar".equals(action)) {

                int id = Integer.parseInt(req.getParameter("id"));
                Paciente p = dao.buscarPorId(id);

                req.setAttribute("paciente", p);

                req.getRequestDispatcher("/views/pacientes/formulario.jsp").forward(req, res);
                return;
            }

            if ("insertar".equals(action)) {

                Paciente p = new Paciente();

                p.setNombres(req.getParameter("nombres"));
                p.setApellidos(req.getParameter("apellidos"));
                p.setDocumento(req.getParameter("documento"));

                String fecha = req.getParameter("fechaNacimiento");
                if (fecha != null && !fecha.isEmpty()) {
                    p.setFechaNacimiento(java.sql.Date.valueOf(fecha));
                }

                p.setTelefono(req.getParameter("telefono"));
                p.setEmail(req.getParameter("email"));
                p.setEps(req.getParameter("eps"));
                p.setVeredaBarrio(req.getParameter("veredaBarrio"));

                dao.insertar(p);
                req.setAttribute("msg", "Se creo correctamente");
                req.getRequestDispatcher("/views/pacientes/lista.jsp").forward(req, res);
            }

            if ("actualizar".equals(action)) {

                Paciente p = new Paciente();

                p.setId(Integer.parseInt(req.getParameter("id")));
                p.setNombres(req.getParameter("nombres"));
                p.setApellidos(req.getParameter("apellidos"));
                p.setDocumento(req.getParameter("documento"));

                String fecha = req.getParameter("fechaNacimiento");
                if (fecha != null && !fecha.isEmpty()) {
                    p.setFechaNacimiento(java.sql.Date.valueOf(fecha));
                }

                p.setTelefono(req.getParameter("telefono"));
                p.setEmail(req.getParameter("email"));
                p.setEps(req.getParameter("eps"));
                p.setVeredaBarrio(req.getParameter("veredaBarrio"));

                dao.actualizar(p);
                req.setAttribute("msg", "Se actualizo correctamente");
                req.getRequestDispatcher("/views/pacientes/lista.jsp").forward(req, res);
            }

            if ("eliminar".equals(action)) {

                int id = Integer.parseInt(req.getParameter("id"));
                dao.eliminar(id);
                req.setAttribute("msg", "Se elimino correctamente");
                req.getRequestDispatcher("/views/pacientes/lista.jsp").forward(req, res);
            }

        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}
