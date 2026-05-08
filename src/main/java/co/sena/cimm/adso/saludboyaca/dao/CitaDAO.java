package co.sena.cimm.adso.saludboyaca.dao;

import co.sena.cimm.adso.saludboyaca.dto.*;
import java.util.List;

public interface CitaDAO {

    void insertar(Cita c) throws Exception;

    void actualizar(Cita c) throws Exception;

    void eliminar(int id) throws Exception;

    void cambiarEstado(int id, String estado) throws Exception;

    Cita buscarPorId(int id) throws Exception;

    List<Cita> listar() throws Exception;
    List<Cita> listarPorMedico(String nombre) throws Exception;

    List<Paciente> listarPacientes() throws Exception;

    List<Usuario> listarMedicos() throws Exception;

    List<Especialidad> listarEspecialidades() throws Exception;
    List<Cita> buscarPorDocumento(String documento) throws Exception;
    List<Cita> listarCitasHoy();
}