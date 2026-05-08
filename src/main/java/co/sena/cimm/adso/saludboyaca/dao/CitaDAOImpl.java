package co.sena.cimm.adso.saludboyaca.dao;

import co.sena.cimm.adso.saludboyaca.dto.*;
import co.sena.cimm.adso.saludboyaca.model.Conexion;

import java.sql.*;
import java.util.*;

public class CitaDAOImpl implements CitaDAO {

    @Override
    public void insertar(Cita c) throws Exception {

        String sql = "INSERT INTO citas "
                + "(id_paciente, id_medico, id_especialidad, fecha_cita, hora_cita, motivo, estado) "
                + "VALUES (?,?,?,?,?,?,?)";

        try (Connection conn = Conexion.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, c.getIdPaciente());
            ps.setInt(2, c.getIdMedico());
            ps.setInt(3, c.getIdEspecialidad());
            ps.setDate(4, new java.sql.Date(c.getFechaCita().getTime()));
            ps.setString(5, c.getHoraCita());
            ps.setString(6, c.getMotivo());
            ps.setString(7, c.getEstado());

            ps.executeUpdate();
        }
    }

    @Override
    public List<Cita> listarCitasHoy() {

        List<Cita> lista = new ArrayList<>();

        String sql = "SELECT c.*, "
                + "p.nombres AS p_nombre, p.apellidos AS p_apellido, "
                + "m.nombres AS m_nombre, m.apellidos AS m_apellido, "
                + "e.nombre AS e_nombre "
                + "FROM citas c "
                + "JOIN pacientes p ON c.id_paciente = p.id "
                + "JOIN usuarios m ON c.id_medico = m.id "
                + "JOIN especialidades e ON c.id_especialidad = e.id "
                + "WHERE DATE(c.fecha_cita) = CURDATE() "
                + "ORDER BY c.hora_cita";

        try (Connection con = Conexion.getConnection(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                Cita c = new Cita();

                c.setId(rs.getInt("id"));
                c.setFechaCita(rs.getDate("fecha_cita"));
                c.setHoraCita(rs.getString("hora_cita"));
                c.setEstado(rs.getString("estado"));

                Paciente p = new Paciente();
                p.setNombres(rs.getString("p_nombre"));
                p.setApellidos(rs.getString("p_apellido"));

                Usuario m = new Usuario();
                m.setNombres(rs.getString("m_nombre"));
                m.setApellidos(rs.getString("m_apellido"));

                Especialidad e = new Especialidad();
                e.setNombre(rs.getString("e_nombre"));

                c.setPaciente(p);
                c.setMedico(m);
                c.setEspecialidad(e);

                lista.add(c);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }

    @Override
    public void actualizar(Cita c) throws Exception {

        String sql = "UPDATE citas SET "
                + "id_paciente=?, id_medico=?, id_especialidad=?, fecha_cita=?, hora_cita=?, motivo=?, estado=? "
                + "WHERE id=?";

        try (Connection conn = Conexion.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, c.getIdPaciente());
            ps.setInt(2, c.getIdMedico());
            ps.setInt(3, c.getIdEspecialidad());
            ps.setDate(4, new java.sql.Date(c.getFechaCita().getTime()));
            ps.setString(5, c.getHoraCita());
            ps.setString(6, c.getMotivo());
            ps.setString(7, c.getEstado());
            ps.setInt(8, c.getId());

            ps.executeUpdate();
        }
    }

    @Override
    public void eliminar(int id) throws Exception {

        String sql = "DELETE FROM citas WHERE id=?";

        try (Connection conn = Conexion.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();
            System.out.println("Ejecutando DELETE con ID: " + id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void cambiarEstado(int id, String estado) throws Exception {

        String sql = "UPDATE citas SET estado=? WHERE id=?";

        try (Connection conn = Conexion.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, estado);
            ps.setInt(2, id);

            ps.executeUpdate();
        }
    }

    @Override
    public Cita buscarPorId(int id) throws Exception {

        Cita c = null;

        String sql
                = "SELECT c.*, "
                + "p.nombres AS p_nombre, p.apellidos AS p_apellido, "
                + "m.nombres AS m_nombre, m.apellidos AS m_apellido, "
                + "e.nombre AS e_nombre "
                + "FROM citas c "
                + "JOIN pacientes p ON c.id_paciente = p.id "
                + "JOIN usuarios m ON c.id_medico = m.id "
                + "JOIN especialidades e ON c.id_especialidad = e.id "
                + "WHERE c.id=?";

        try (Connection conn = Conexion.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {

                    c = new Cita();

                    c.setId(rs.getInt("id"));
                    c.setIdPaciente(rs.getInt("id_paciente"));
                    c.setIdMedico(rs.getInt("id_medico"));
                    c.setIdEspecialidad(rs.getInt("id_especialidad"));

                    c.setFechaCita(rs.getDate("fecha_cita"));
                    c.setHoraCita(rs.getString("hora_cita"));
                    c.setMotivo(rs.getString("motivo"));
                    c.setEstado(rs.getString("estado"));

                    Paciente p = new Paciente();
                    p.setId(rs.getInt("id_paciente"));
                    p.setNombres(rs.getString("p_nombre"));
                    p.setApellidos(rs.getString("p_apellido"));

                    Usuario m = new Usuario();
                    m.setNombres(rs.getString("m_nombre"));
                    m.setApellidos(rs.getString("m_apellido"));

                    Especialidad e = new Especialidad();
                    e.setNombre(rs.getString("e_nombre"));

                    c.setPaciente(p);
                    c.setMedico(m);
                    c.setEspecialidad(e);
                }
            }
        }

        return c;
    }

    @Override
    public List<Cita> listar() throws Exception {

        List<Cita> lista = new ArrayList<>();

        String sql
                = "SELECT c.*, "
                + "p.nombres AS p_nombre, p.apellidos AS p_apellido, "
                + "m.nombres AS m_nombre, m.apellidos AS m_apellido, "
                + "e.nombre AS e_nombre "
                + "FROM citas c "
                + "JOIN pacientes p ON c.id_paciente = p.id "
                + "JOIN usuarios m ON c.id_medico = m.id "
                + "JOIN especialidades e ON c.id_especialidad = e.id "
                + "ORDER BY c.fecha_cita DESC";

        try (Connection conn = Conexion.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                Cita c = new Cita();

                c.setId(rs.getInt("id"));

                c.setIdPaciente(rs.getInt("id_paciente"));
                c.setIdMedico(rs.getInt("id_medico"));
                c.setIdEspecialidad(rs.getInt("id_especialidad"));

                c.setFechaCita(rs.getDate("fecha_cita"));
                c.setHoraCita(rs.getString("hora_cita"));
                c.setEstado(rs.getString("estado"));
                c.setMotivo(rs.getString("motivo"));

                Paciente p = new Paciente();
                p.setNombres(rs.getString("p_nombre"));
                p.setApellidos(rs.getString("p_apellido"));

                Usuario m = new Usuario();
                m.setNombres(rs.getString("m_nombre"));
                m.setApellidos(rs.getString("m_apellido"));

                Especialidad e = new Especialidad();
                e.setNombre(rs.getString("e_nombre"));
                c.setPaciente(p);
                c.setMedico(m);
                c.setEspecialidad(e);

                lista.add(c);
            }
        }

        return lista;
    }

    @Override
    public List<Cita> listarPorMedico(String nombre) throws Exception {
        List<Cita> lista = new ArrayList<>();

        String sql
                = "SELECT c.*, "
                + "p.nombres AS p_nombre, p.apellidos AS p_apellido, "
                + "m.nombres AS m_nombre, m.apellidos AS m_apellido, "
                + "e.nombre AS e_nombre "
                + "FROM citas c "
                + "JOIN pacientes p ON c.id_paciente = p.id "
                + "JOIN usuarios m ON c.id_medico = m.id "
                + "JOIN especialidades e ON c.id_especialidad = e.id "
                + "WHERE CONCAT(m.nombres, ' ', m.apellidos) LIKE ? "
                + "ORDER BY c.fecha_cita DESC";

        try (Connection conn = Conexion.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, "%" + nombre + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {

                    Cita c = new Cita();

                    c.setId(rs.getInt("id"));
                    c.setFechaCita(rs.getDate("fecha_cita"));
                    c.setHoraCita(rs.getString("hora_cita"));
                    c.setEstado(rs.getString("estado"));
                    c.setMotivo(rs.getString("motivo"));

                    Paciente p = new Paciente();
                    p.setNombres(rs.getString("p_nombre"));
                    p.setApellidos(rs.getString("p_apellido"));

                    Usuario m = new Usuario();
                    m.setNombres(rs.getString("m_nombre"));
                    m.setApellidos(rs.getString("m_apellido"));

                    Especialidad e = new Especialidad();
                    e.setNombre(rs.getString("e_nombre"));
                    c.setPaciente(p);
                    c.setMedico(m);
                    c.setEspecialidad(e);

                    lista.add(c);
                }
            }

        }

        return lista;
    }

    @Override
    public List<Paciente> listarPacientes() throws Exception {

        List<Paciente> lista = new ArrayList<>();

        String sql = "SELECT id, nombres, apellidos FROM pacientes";

        try (Connection conn = Conexion.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                Paciente p = new Paciente();
                p.setId(rs.getInt("id"));
                p.setNombres(rs.getString("nombres"));
                p.setApellidos(rs.getString("apellidos"));

                lista.add(p);
            }
        }

        return lista;
    }

    @Override
    public List<Usuario> listarMedicos() throws Exception {

        List<Usuario> lista = new ArrayList<>();

        String sql = "SELECT id, nombres, apellidos FROM usuarios WHERE rol='MEDICO'";

        try (Connection conn = Conexion.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                Usuario u = new Usuario();
                u.setId(rs.getInt("id"));
                u.setNombres(rs.getString("nombres"));
                u.setApellidos(rs.getString("apellidos"));

                lista.add(u);
            }
        }

        return lista;
    }

    @Override
    public List<Cita> buscarPorDocumento(String documento) throws Exception {

        List<Cita> lista = new ArrayList<>();

        String sql
                = "SELECT c.*, "
                + "p.nombres AS p_nombre, p.apellidos AS p_apellido, "
                + "m.nombres AS m_nombre, m.apellidos AS m_apellido, "
                + "e.nombre AS e_nombre "
                + "FROM citas c "
                + "JOIN pacientes p ON c.id_paciente = p.id "
                + "JOIN usuarios m ON c.id_medico = m.id "
                + "JOIN especialidades e ON c.id_especialidad = e.id "
                + "WHERE p.documento = ? "
                + "ORDER BY c.fecha_cita DESC";

        try (Connection conn = Conexion.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, documento);

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {

                    Cita c = new Cita();

                    c.setId(rs.getInt("id"));
                    c.setFechaCita(rs.getDate("fecha_cita"));
                    c.setHoraCita(rs.getString("hora_cita"));
                    c.setEstado(rs.getString("estado"));
                    c.setMotivo(rs.getString("motivo"));

                    Paciente p = new Paciente();
                    p.setNombres(rs.getString("p_nombre"));
                    p.setApellidos(rs.getString("p_apellido"));

                    Usuario m = new Usuario();
                    m.setNombres(rs.getString("m_nombre"));
                    m.setApellidos(rs.getString("m_apellido"));

                    Especialidad e = new Especialidad();
                    e.setNombre(rs.getString("e_nombre"));

                    c.setPaciente(p);
                    c.setMedico(m);
                    c.setEspecialidad(e);

                    lista.add(c);
                }
            }
        }

        return lista;
    }

    @Override
    public List<Especialidad> listarEspecialidades() throws Exception {

        List<Especialidad> lista = new ArrayList<>();

        String sql = "SELECT id, nombre FROM especialidades";

        try (Connection conn = Conexion.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                Especialidad e = new Especialidad();
                e.setId(rs.getInt("id"));
                e.setNombre(rs.getString("nombre"));

                lista.add(e);
            }
        }

        return lista;
    }
}
