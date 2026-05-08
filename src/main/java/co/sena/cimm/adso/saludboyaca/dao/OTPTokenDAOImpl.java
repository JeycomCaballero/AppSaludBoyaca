package co.sena.cimm.adso.saludboyaca.dao;

import co.sena.cimm.adso.saludboyaca.model.Conexion;
import java.sql.*;

public class OTPTokenDAOImpl implements OTPTokenDAO {

    @Override
    public void insertar(int idUsuario, String codigo, java.util.Date expira) throws Exception {

        String sql = "INSERT INTO otp_tokens (id_usuario, codigo, expira_en) VALUES (?,?,?)";

        try (Connection conn = Conexion.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idUsuario);
            ps.setString(2, codigo);
            ps.setTimestamp(3, new Timestamp(expira.getTime()));

            ps.executeUpdate();
        }
    }

    @Override
    public boolean validar(int idUsuario, String codigo) throws Exception {

        String sql = "SELECT id FROM otp_tokens WHERE id_usuario=? AND codigo=? AND usado=0 AND expira_en > ?";

        try (Connection conn = Conexion.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idUsuario);
            ps.setString(2, codigo);
            ps.setTimestamp(3, new Timestamp(System.currentTimeMillis()));

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    @Override
    public void marcarUsado(int idUsuario, String codigo) throws Exception {

        String sql;

        if ("ALL".equals(codigo)) {
            sql = "UPDATE otp_tokens SET usado=1 WHERE id_usuario=?";
        } else {
            sql = "UPDATE otp_tokens SET usado=1 WHERE id_usuario=? AND codigo=?";
        }

        try (Connection conn = Conexion.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idUsuario);

            if (!"ALL".equals(codigo)) {
                ps.setString(2, codigo);
            }

            ps.executeUpdate();
        }
    }
}
