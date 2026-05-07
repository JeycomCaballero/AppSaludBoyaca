package co.sena.cimm.adso.saludboyaca.controller;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.WebServlet;

import java.io.IOException;
import java.util.List;

import co.sena.cimm.adso.saludboyaca.dao.CitaDAOImpl;
import co.sena.cimm.adso.saludboyaca.dao.HorarioDAOImpl;
import co.sena.cimm.adso.saludboyaca.dto.Cita;
import co.sena.cimm.adso.saludboyaca.dto.Usuario;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;

import co.sena.cimm.adso.saludboyaca.util.PDFGenerator;

@WebServlet("/citas")
public class CitaServlet extends HttpServlet {

    private CitaDAOImpl dao;
    private HorarioDAOImpl horarioDAO = new HorarioDAOImpl();

    @Override
    public void init() throws ServletException {
        try {
            dao = new CitaDAOImpl();
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        HttpSession session = req.getSession(true);
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        String rol = (String) session.getAttribute("rol");
        System.out.println("DATOS:" + usuario.getNombreCompleto());
        List<Cita> lista;

        try {
            if (rol.equals("MEDICO")) {
                lista = dao.listarPorMedico(usuario.getNombreCompleto());
            } else {
                lista = dao.listar();
            }
            req.setAttribute("listaCitas", lista);

            req.getRequestDispatcher("/views/citas/lista.jsp").forward(req, res);
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        try {

            String action = req.getParameter("action");

            System.out.println("ACTION RECIBIDA: " + action);

            if ("nuevo".equals(action)) {

                cargarCombos(req);

                req.getRequestDispatcher("/views/citas/formulario.jsp").forward(req, res);
                return;
            }

            if ("editar".equals(action)) {

                int id = Integer.parseInt(req.getParameter("id"));

                Cita c = dao.buscarPorId(id);

                req.setAttribute("cita", c);
                cargarCombos(req);

                req.getRequestDispatcher("/views/citas/formulario.jsp")
                        .forward(req, res);
                return;
            }

            if ("ver".equals(action)) {

                int id = Integer.parseInt(req.getParameter("id"));

                Cita c = dao.buscarPorId(id);

                req.setAttribute("cita", c);

                req.getRequestDispatcher("/views/citas/detalle.jsp")
                        .forward(req, res);
                return;
            }

            if ("insertar".equals(action)) {

                Cita c = new Cita();

                int idPaciente = Integer.parseInt(req.getParameter("paciente"));
                int idMedico = Integer.parseInt(req.getParameter("medico"));
                int idEspecialidad = Integer.parseInt(req.getParameter("especialidad"));

                java.util.Date fecha = java.sql.Date.valueOf(req.getParameter("fecha"));
                String hora = req.getParameter("hora");

                HorarioDAOImpl horarioDAO = new HorarioDAOImpl();
                boolean disponible = horarioDAO.validarDisponibilidad(idMedico, fecha, hora);

                if (!disponible) {
                    req.setAttribute("msg", "No se puede agendar la cita");

                    List<Cita> lista = dao.listar();
                    req.setAttribute("listaCitas", lista);

                    req.getRequestDispatcher("/views/citas/lista.jsp").forward(req, res);
                    return;
                }

                c.setIdPaciente(idPaciente);
                c.setIdMedico(idMedico);
                c.setIdEspecialidad(idEspecialidad);
                c.setFechaCita(fecha);
                c.setHoraCita(hora);
                c.setMotivo(req.getParameter("motivo"));
                c.setEstado("PROGRAMADA");
                dao.insertar(c);

                List<Cita> lista = dao.listar();
                req.setAttribute("listaCitas", lista);

                req.setAttribute("msg", "Se creo correctamente");
                req.getRequestDispatcher("/views/citas/lista.jsp").forward(req, res);
                return;
            }
            if("pdfHoy".equals(action)){
                generarPDFHoy(req, res);
                return;
            }

            if ("actualizar".equals(action)) {

                Cita c = new Cita();

                c.setId(Integer.parseInt(req.getParameter("id")));
                c.setIdPaciente(Integer.parseInt(req.getParameter("paciente")));
                c.setIdMedico(Integer.parseInt(req.getParameter("medico")));
                c.setIdEspecialidad(Integer.parseInt(req.getParameter("especialidad")));

                c.setFechaCita(java.sql.Date.valueOf(req.getParameter("fecha")));
                c.setHoraCita(req.getParameter("hora"));
                c.setMotivo(req.getParameter("motivo"));
                c.setEstado(req.getParameter("estado"));

                dao.actualizar(c);

                req.setAttribute("msg", "Se actualizo correctamente");
                req.getRequestDispatcher("/views/citas/lista.jsp").forward(req, res);
                return;
            }

            if ("eliminar".equals(action)) {

                int id = Integer.parseInt(req.getParameter("id"));
                System.out.println("ID A ELIMINAR: " + id);
                dao.eliminar(id);

                List<Cita> lista = dao.listar();
                req.setAttribute("listaCitas", lista);

                req.setAttribute("msg", "Se elimino correctamente");
                req.getRequestDispatcher("/views/citas/lista.jsp").forward(req, res);
                return;
            }

            if ("estado".equals(action)) {

                int id = Integer.parseInt(req.getParameter("id"));
                String estado = req.getParameter("estado");

                dao.cambiarEstado(id, estado);

                req.setAttribute("msg", "Se cambio correctamente");
                req.getRequestDispatcher("/views/citas/lista.jsp").forward(req, res);
            }

        } catch (Exception e) {
            e.printStackTrace();

            throw new ServletException(e);
        }
    }

    private void cargarCombos(HttpServletRequest req) throws Exception {

        req.setAttribute("pacientes", dao.listarPacientes());
        req.setAttribute("medicos", dao.listarMedicos());
        req.setAttribute("especialidades", dao.listarEspecialidades());
    }

    private void generarPDFHoy(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        try {

            CitaDAOImpl dao = new CitaDAOImpl();

            List<Cita> citasHoy = dao.listarCitasHoy();

            byte[] pdf = PDFGenerator.generarListaCitasHoy(citasHoy);

            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition",
                    "attachment; filename=citas_hoy.pdf");

            OutputStream os = response.getOutputStream();
            os.write(pdf);
            os.flush();
            os.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
