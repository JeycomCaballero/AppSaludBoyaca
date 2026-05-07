package co.sena.cimm.adso.saludboyaca.dto;

import java.io.Serializable;
import java.sql.Time;

public class Horario implements Serializable {

    private int id;
    private String nombre;
    private int idMedico;
    private int diaSemana;
    private Time horaInicio;
    private Time horaFin;
    private int maxCitas;

    public Horario() {
    }

    
    public Horario(int id, String nombre, int idMedico, int diaSemana, Time horaInicio, Time horaFin, int maxCitas) {
        this.id = id;
        this.nombre = nombre;
        this.idMedico = idMedico;
        this.diaSemana = diaSemana;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
        this.maxCitas = maxCitas;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getIdMedico() {
        return idMedico;
    }

    public void setIdMedico(int idMedico) {
        this.idMedico = idMedico;
    }

    public int getDiaSemana() {
        return diaSemana;
    }

    public void setDiaSemana(int diaSemana) {
        this.diaSemana = diaSemana;
    }

    public Time getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(Time horaInicio) {
        this.horaInicio = horaInicio;
    }

    public Time getHoraFin() {
        return horaFin;
    }

    public void setHoraFin(Time horaFin) {
        this.horaFin = horaFin;
    }

    public int getMaxCitas() {
        return maxCitas;
    }

    public void setMaxCitas(int maxCitas) {
        this.maxCitas = maxCitas;
    }

   
}
