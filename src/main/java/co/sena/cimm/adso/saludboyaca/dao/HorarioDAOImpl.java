package co.sena.cimm.adso.saludboyaca.dao;

import co.sena.cimm.adso.saludboyaca.dto.Horario;
import co.sena.cimm.adso.saludboyaca.model.Conexion;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class HorarioDAOImpl implements HorarioDAO {

    @Override
    public List<Horario> listarPorNombreMedico(String nombreMedico) throws Exception {

        List<Horario> lista = new ArrayList<>();

        String sql
                = "SELECT h.*,u.nombres "
                + "FROM horarios h "
                + "INNER JOIN usuarios u ON h.id_medico = u.id "
                + "WHERE CONCAT(u.nombres, ' ', u.apellidos) LIKE ?";

        try (Connection conn = Conexion.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, "%" + nombreMedico + "%");

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {

                    Horario h = new Horario();
                    h.setId(rs.getInt("id"));
                    h.setIdMedico(rs.getInt("id_medico"));
                    h.setDiaSemana(rs.getInt("dia_semana"));
                    h.setHoraInicio(rs.getTime("hora_inicio"));
                    h.setHoraFin(rs.getTime("hora_fin"));
                    h.setMaxCitas(rs.getInt("max_citas"));
                    h.setNombre(rs.getString("nombres"));

                    lista.add(h);
                }
            }
        }

        return lista;
    }

    @Override
    public List<Horario> listarTodos() throws Exception {

        List<Horario> lista = new ArrayList<>();

        String sql
                = "SELECT h.*, u.nombres, u.apellidos "
                + "FROM horarios h "
                + "INNER JOIN usuarios u ON h.id_medico = u.id";

        try (Connection conn = Conexion.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                Horario h = new Horario();

                h.setId(rs.getInt("id"));
                h.setIdMedico(rs.getInt("id_medico"));
                h.setDiaSemana(rs.getInt("dia_semana"));
                h.setHoraInicio(rs.getTime("hora_inicio"));
                h.setHoraFin(rs.getTime("hora_fin"));
                h.setMaxCitas(rs.getInt("max_citas"));
                h.setNombre(rs.getString("nombres"));

                lista.add(h);
            }
        }

        return lista;
    }
    @Override
    public boolean validarDisponibilidad(int idMedico, java.util.Date fecha, String hora) throws Exception {

       
        Calendar hoy = Calendar.getInstance();
        Calendar fechaCita = Calendar.getInstance();

        hoy.set(Calendar.HOUR_OF_DAY, 0);
        hoy.set(Calendar.MINUTE, 0);
        hoy.set(Calendar.SECOND, 0);
        hoy.set(Calendar.MILLISECOND, 0);

        fechaCita.setTime(fecha);
        fechaCita.set(Calendar.HOUR_OF_DAY, 0);
        fechaCita.set(Calendar.MINUTE, 0);
        fechaCita.set(Calendar.SECOND, 0);
        fechaCita.set(Calendar.MILLISECOND, 0);

        if (!fechaCita.after(hoy)) {
            return false; 
        }

        int diaSemana = fechaCita.get(Calendar.DAY_OF_WEEK);
        if (diaSemana == Calendar.SUNDAY) {
            diaSemana = 7;
        } else {
            diaSemana = diaSemana - 1;
        }

        try (Connection conn = Conexion.getConnection()) {

            
            String sqlHorario = "SELECT hora_inicio, hora_fin, max_citas FROM horarios WHERE id_medico=? AND dia_semana=?";
            PreparedStatement psHorario = conn.prepareStatement(sqlHorario);
            psHorario.setInt(1, idMedico);
            psHorario.setInt(2, diaSemana);

            ResultSet rsHorario = psHorario.executeQuery();

            if (!rsHorario.next()) {
                return false; 
            }

            Time inicio = rsHorario.getTime("hora_inicio");
            Time fin = rsHorario.getTime("hora_fin");
            int maxCitas = rsHorario.getInt("max_citas");

            Time horaCita = Time.valueOf(hora + ":00");

            if (horaCita.before(inicio) || !horaCita.before(fin)) {
                return false;
            }

            String sqlCount = "SELECT COUNT(*) FROM citas WHERE id_medico=? AND fecha_cita=? AND hora_cita=? AND estado != 'CANCELADA'";
            PreparedStatement psCount = conn.prepareStatement(sqlCount);

            psCount.setInt(1, idMedico);
            psCount.setDate(2, new java.sql.Date(fecha.getTime()));
            psCount.setTime(3, horaCita);

            ResultSet rsCount = psCount.executeQuery();

            if (rsCount.next()) {
                int cantidad = rsCount.getInt(1);
                if (cantidad >= maxCitas) {
                    return false;
                }
            }
        }

        return true; 
    }

    @Override
    public List<String> horasDisponibles(int idMedico, java.util.Date fecha) throws Exception {

        List<String> horas = new ArrayList<>();

        if (fecha == null) {
            return horas;
        }

        Calendar cal = Calendar.getInstance();
        cal.setTime(fecha);

        int diaSemana = cal.get(Calendar.DAY_OF_WEEK);

        if (diaSemana == Calendar.SUNDAY) {
            diaSemana = 7;
        } else {
            diaSemana = diaSemana - 1;
        }

        String sqlHorarios
                = "SELECT hora_inicio, hora_fin FROM horarios WHERE id_medico=? AND dia_semana=?";

        String sqlCitas
                = "SELECT hora_cita FROM citas WHERE id_medico=? AND fecha_cita=?";

        try (Connection conn = Conexion.getConnection()) {

            Set<String> ocupadas = new HashSet<>();

            try (PreparedStatement ps = conn.prepareStatement(sqlCitas)) {

                ps.setInt(1, idMedico);
                ps.setDate(2, new java.sql.Date(fecha.getTime()));

                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        ocupadas.add(rs.getString("hora_cita").substring(0, 5));
                    }
                }
            }

            try (PreparedStatement ps = conn.prepareStatement(sqlHorarios)) {

                ps.setInt(1, idMedico);
                ps.setInt(2, diaSemana);

                try (ResultSet rs = ps.executeQuery()) {

                    while (rs.next()) {

                        Time inicio = rs.getTime("hora_inicio");
                        Time fin = rs.getTime("hora_fin");

                        Calendar c = Calendar.getInstance();
                        c.setTime(inicio);

                        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");

                        while (c.getTime().before(fin)) {

                            String hora = sdf.format(c.getTime());

                            if (!ocupadas.contains(hora)) {
                                horas.add(hora);
                            }

                            c.add(Calendar.MINUTE, 30);
                        }
                    }
                }
            }
        }

        return horas;
    }
}
