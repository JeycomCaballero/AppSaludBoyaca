package co.sena.cimm.adso.saludboyaca.dao;

import co.sena.cimm.adso.saludboyaca.dto.Paciente;
import co.sena.cimm.adso.saludboyaca.model.Conexion;

import java.sql.*;
import java.util.*;

public class PacienteDAOImpl implements PacienteDAO {

   
    @Override
    public void insertar(Paciente p) throws Exception {

        String sql = "INSERT INTO pacientes (nombres, apellidos, documento, fecha_nacimiento, telefono, email, eps, vereda_barrio) VALUES (?,?,?,?,?,?,?,?)";

        try (Connection conn = Conexion.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, p.getNombres());
            ps.setString(2, p.getApellidos());
            ps.setString(3, p.getDocumento());
            ps.setDate(4, new java.sql.Date(p.getFechaNacimiento().getTime()));
            ps.setString(5, p.getTelefono());
            ps.setString(6, p.getEmail());
            ps.setString(7, p.getEps());
            ps.setString(8, p.getVeredaBarrio());

            ps.executeUpdate();
        }
    }

   
    @Override
    public void actualizar(Paciente p) throws Exception {

        String sql = "UPDATE pacientes SET nombres=?, apellidos=?, documento=?, fecha_nacimiento=?, telefono=?, email=?, eps=?, vereda_barrio=? WHERE id=?";

        try (Connection conn = Conexion.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, p.getNombres());
            ps.setString(2, p.getApellidos());
            ps.setString(3, p.getDocumento());
            ps.setDate(4, new java.sql.Date(p.getFechaNacimiento().getTime()));
            ps.setString(5, p.getTelefono());
            ps.setString(6, p.getEmail());
            ps.setString(7, p.getEps());
            ps.setString(8, p.getVeredaBarrio());
            ps.setInt(9, p.getId());

            ps.executeUpdate();
        }
    }

    @Override
    public void eliminar(int id) throws Exception {

        String sql = "DELETE FROM pacientes WHERE id=?";

        try (Connection conn = Conexion.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();
        }catch(SQLException e){
            e.printStackTrace();
            
        }
    }

  
    @Override
    public List<Paciente> listar() throws Exception {

        List<Paciente> lista = new ArrayList<>();
        String sql = "SELECT * FROM pacientes";

        try (Connection conn = Conexion.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                Paciente p = new Paciente();

                p.setId(rs.getInt("id"));
                p.setNombres(rs.getString("nombres"));
                p.setApellidos(rs.getString("apellidos"));
                p.setDocumento(rs.getString("documento"));
                p.setFechaNacimiento(rs.getDate("fecha_nacimiento"));
                p.setTelefono(rs.getString("telefono"));
                p.setEmail(rs.getString("email"));
                p.setEps(rs.getString("eps"));
                p.setVeredaBarrio(rs.getString("vereda_barrio"));

                lista.add(p);
            }
        }

        return lista;
    }

   
    @Override
    public Paciente buscarPorDocumento(String doc) throws Exception {

        String sql = "SELECT * FROM pacientes WHERE documento=?";

        try (Connection conn = Conexion.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, doc);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {

                    Paciente p = new Paciente();

                    p.setId(rs.getInt("id"));
                    p.setNombres(rs.getString("nombres"));
                    p.setApellidos(rs.getString("apellidos"));
                    p.setDocumento(rs.getString("documento"));
                    p.setFechaNacimiento(rs.getDate("fecha_nacimiento"));
                    p.setTelefono(rs.getString("telefono"));
                    p.setEmail(rs.getString("email"));
                    p.setEps(rs.getString("eps"));
                    p.setVeredaBarrio(rs.getString("vereda_barrio"));

                    return p;
                }
            }
        }

        return null;
    }

  
    @Override
    public Paciente buscarPorId(int id) throws Exception {

        String sql = "SELECT * FROM pacientes WHERE id=?";

        try (Connection conn = Conexion.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {

                    Paciente p = new Paciente();

                    p.setId(rs.getInt("id"));
                    p.setNombres(rs.getString("nombres"));
                    p.setApellidos(rs.getString("apellidos"));
                    p.setDocumento(rs.getString("documento"));
                    p.setFechaNacimiento(rs.getDate("fecha_nacimiento"));
                    p.setTelefono(rs.getString("telefono"));
                    p.setEmail(rs.getString("email"));
                    p.setEps(rs.getString("eps"));
                    p.setVeredaBarrio(rs.getString("vereda_barrio"));

                    return p;
                }
            }
        }

        return null;
    }
}
