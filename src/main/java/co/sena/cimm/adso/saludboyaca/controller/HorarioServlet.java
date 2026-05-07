package co.sena.cimm.adso.saludboyaca.controller;

import co.sena.cimm.adso.saludboyaca.dao.HorarioDAO;
import co.sena.cimm.adso.saludboyaca.dao.HorarioDAOImpl;
import co.sena.cimm.adso.saludboyaca.dto.Horario;
import co.sena.cimm.adso.saludboyaca.dto.Usuario;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.WebServlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/horarios")
public class HorarioServlet extends HttpServlet {

    private HorarioDAO dao;

    @Override
    public void init() {
        dao = new HorarioDAOImpl();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        String action = req.getParameter("action");

        try {

            if (action == null || action.equals("listar")) {
                listar(req, res);

            } else if (action.equals("horas")) {
                horas(req, res);
            }

        } catch (Exception e) {
            req.setAttribute("error", "Error cargando horarios");
            req.getRequestDispatcher("/views/horarios/lista.jsp").forward(req, res);
        }
    }

    private void listar(HttpServletRequest req, HttpServletResponse res)
            throws Exception {
        HttpSession session = req.getSession(true);
        String rol = (String) session.getAttribute("rol");
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        String nombreMedico = req.getParameter("nombreMedico");
        System.out.println("Datos: " + usuario.getNombreCompleto());

        List<Horario> lista;

        if (rol.equals("MEDICO")) {
            lista = dao.listarPorNombreMedico(usuario.getNombreCompleto());
        } else {
            lista = dao.listarTodos();
        }

        req.setAttribute("horarios", lista);
        req.getRequestDispatcher("/views/horarios/lista.jsp").forward(req, res);
    }

    private void horas(HttpServletRequest req, HttpServletResponse res)
            throws Exception {

        int idMedico = Integer.parseInt(req.getParameter("idMedico"));
        java.sql.Date fecha = java.sql.Date.valueOf(req.getParameter("fecha"));

        List<String> horas = dao.horasDisponibles(idMedico, fecha);

        req.setAttribute("horas", horas);
        req.getRequestDispatcher("/views/horarios/horas.jsp").forward(req, res);
    }
}
