package co.sena.cimm.adso.saludboyaca.dao;

import co.sena.cimm.adso.saludboyaca.dto.Especialidad;
import co.sena.cimm.adso.saludboyaca.model.Conexion;

import java.sql.*;
import java.util.*;

public class EspecialidadDAOImpl implements EspecialidadDAO {

    @Override
    public List<Especialidad> listarTodas() throws Exception {

        List<Especialidad> lista = new ArrayList<>();
        String sql = "SELECT * FROM especialidades";

        try (Connection conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Especialidad e = new Especialidad();
                e.setId(rs.getInt("id"));
                e.setNombre(rs.getString("nombre"));
                e.setDescripcion(rs.getString("descripcion"));
                lista.add(e);
            }
        }

        return lista;
    }
}