package co.sena.cimm.adso.saludboyaca.dao;

import co.sena.cimm.adso.saludboyaca.dto.Especialidad;
import java.util.List;

public interface EspecialidadDAO {

    List<Especialidad> listarTodas() throws Exception;
}
