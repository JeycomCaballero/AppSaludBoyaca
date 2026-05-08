package co.sena.cimm.adso.saludboyaca.dao;

import co.sena.cimm.adso.saludboyaca.dto.Usuario;
import co.sena.cimm.adso.saludboyaca.model.Conexion;

import java.sql.*;
import java.util.*;

public class UsuarioDAOImpl implements UsuarioDAO {

    @Override
    public Usuario validarLogin(String username, String password) throws Exception {

        String sql = "SELECT * FROM usuarios WHERE username=? AND password=? AND activo=1";

        try (Connection conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);
            ps.setString(2, password);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    Usuario u = new Usuario();

                    u.setId(rs.getInt("id"));
                    u.setNombres(rs.getString("nombres"));
                    u.setApellidos(rs.getString("apellidos"));
                    u.setEmail(rs.getString("email"));
                    u.setUsername(rs.getString("username"));
                    u.setRol(rs.getString("rol"));
                    u.setEspecialidad(rs.getString("especialidad"));
                    u.setLangPref(rs.getString("lang_preferido"));

                    return u;
                }
            }
        }

        return null;
    }

    @Override
    public Usuario buscarPorId(int id) throws Exception {

        String sql = "SELECT * FROM usuarios WHERE id=?";

        try (Connection conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    Usuario u = new Usuario();

                    u.setId(rs.getInt("id"));
                    u.setNombres(rs.getString("nombres"));
                    u.setApellidos(rs.getString("apellidos"));
                    u.setEmail(rs.getString("email"));
                    u.setUsername(rs.getString("username"));
                    u.setRol(rs.getString("rol"));
                    u.setEspecialidad(rs.getString("especialidad"));
                    u.setLangPref(rs.getString("lang_preferido"));

                    return u;
                }
            }
        }

        return null;
    }

    @Override
    public List<Usuario> listarTodos() throws Exception {

        List<Usuario> lista = new ArrayList<>();
        String sql = "SELECT * FROM usuarios";

        try (Connection conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Usuario u = new Usuario();

                u.setId(rs.getInt("id"));
                u.setNombres(rs.getString("nombres"));
                u.setApellidos(rs.getString("apellidos"));
                u.setEmail(rs.getString("email"));
                u.setUsername(rs.getString("username"));
                u.setRol(rs.getString("rol"));
                u.setEspecialidad(rs.getString("especialidad"));
                u.setLangPref(rs.getString("lang_preferido"));

                lista.add(u);
            }
        }

        return lista;
    }
}