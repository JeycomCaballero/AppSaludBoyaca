package co.sena.cimm.adso.saludboyaca.dto;

import java.io.Serializable;
import java.util.Date;

public class OTPToken implements Serializable {

    private int id;
    private int idUsuario;
    private String codigo;
    private Date fechaGen;
    private Date expiraEn;
    private boolean usado;

    public OTPToken() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public Date getFechaGen() {
        return fechaGen;
    }

    public void setFechaGen(Date fechaGen) {
        this.fechaGen = fechaGen;
    }

    public Date getExpiraEn() {
        return expiraEn;
    }

    public void setExpiraEn(Date expiraEn) {
        this.expiraEn = expiraEn;
    }

    public boolean isUsado() {
        return usado;
    }

    public void setUsado(boolean usado) {
        this.usado = usado;
    }
}
