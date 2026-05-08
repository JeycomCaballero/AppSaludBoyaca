package co.sena.cimm.adso.saludboyaca.dao;

public interface OTPTokenDAO {

    void insertar(int idUsuario, String codigo, java.util.Date expira) throws Exception;

    boolean validar(int idUsuario, String codigo) throws Exception;

    void marcarUsado(int idUsuario, String codigo) throws Exception;
}
