package co.sena.cimm.adso.saludboyaca.dao;

import co.sena.cimm.adso.saludboyaca.dto.Paciente;
import java.util.List;

public interface PacienteDAO {

    void insertar(Paciente p) throws Exception;

    void actualizar(Paciente p) throws Exception;

    void eliminar(int id) throws Exception;

    List<Paciente> listar() throws Exception;

    Paciente buscarPorDocumento(String documento) throws Exception;

    Paciente buscarPorId(int id) throws Exception;
}
