package co.sena.cimm.adso.saludboyaca.dao;

import co.sena.cimm.adso.saludboyaca.dto.Horario;
import java.util.List;

public interface HorarioDAO {

    List<Horario> listarPorNombreMedico(String nombreMedico) throws Exception;

    List<String> horasDisponibles(int idMedico, java.util.Date fecha) throws Exception;
    List<Horario> listarTodos() throws Exception;
    boolean validarDisponibilidad(int idMedico, java.util.Date fecha, String hora) throws Exception;
}
