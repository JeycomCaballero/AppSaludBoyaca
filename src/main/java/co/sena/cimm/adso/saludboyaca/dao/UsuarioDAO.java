package co.sena.cimm.adso.saludboyaca.dao;

import co.sena.cimm.adso.saludboyaca.dto.Usuario;
import java.util.List;

public interface UsuarioDAO {

    Usuario validarLogin(String username, String password) throws Exception;

    Usuario buscarPorId(int id) throws Exception;

    List<Usuario> listarTodos() throws Exception;
}