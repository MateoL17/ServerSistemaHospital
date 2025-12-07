package model;

import java.io.Serializable;
import java.time.LocalDateTime;

/*
 * Author: Mateo Lasso
 * Fecha: 7-12-2025
 * Versión: 1.0
 * Descripción: Esta clase denominada Paciente representa la entidad Paciente
 *              en el sistema hospitalario con todos sus atributos y operaciones básicas.
 * */

public class Paciente implements Serializable {
    private String cedula;
    private String nombre;
    private String correo;
    private int edad;
    private String direccion;
    private boolean activo;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;

    /*
     * Constructor vacío para cumplir con el estándar JavaBeans
     * */
    public Paciente() {
    }

    /*
     * Constructor con parámetros principales para crear un nuevo paciente
     * @param cedula Parámetro que define el número de cédula del paciente
     * @param nombre Parámetro que define el nombre completo del paciente
     * @param correo Parámetro que define el correo electrónico del paciente
     * @param edad Parámetro que define la edad del paciente
     * @param direccion Parámetro que define la dirección del paciente
     * */
    public Paciente(String cedula, String nombre, String correo, int edad, String direccion) {
        this.cedula = cedula;
        this.nombre = nombre;
        this.correo = correo;
        this.edad = edad;
        this.direccion = direccion;
        this.activo = true;
        this.fechaCreacion = LocalDateTime.now();
        this.fechaActualizacion = LocalDateTime.now();
    }

    // Getters y Setters

    /*
     * Método que obtiene la cédula del paciente
     * @return Número de cédula del paciente
     * */
    public String getCedula() {
        return cedula;
    }

    /*
     * Método que establece la cédula del paciente
     * @param cedula Parámetro que define el nuevo número de cédula
     * */
    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    /*
     * Método que obtiene el nombre del paciente
     * @return Nombre completo del paciente
     * */
    public String getNombre() {
        return nombre;
    }

    /*
     * Método que establece el nombre del paciente
     * @param nombre Parámetro que define el nuevo nombre del paciente
     * */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /*
     * Método que obtiene el correo del paciente
     * @return Correo electrónico del paciente
     * */
    public String getCorreo() {
        return correo;
    }

    /*
     * Método que establece el correo del paciente
     * @param correo Parámetro que define el nuevo correo electrónico
     * */
    public void setCorreo(String correo) {
        this.correo = correo;
    }

    /*
     * Método que obtiene la edad del paciente
     * @return Edad del paciente
     * */
    public int getEdad() {
        return edad;
    }

    /*
     * Método que establece la edad del paciente
     * @param edad Parámetro que define la nueva edad del paciente
     * */
    public void setEdad(int edad) {
        this.edad = edad;
    }

    /*
     * Método que obtiene la dirección del paciente
     * @return Dirección del paciente
     * */
    public String getDireccion() {
        return direccion;
    }

    /*
     * Método que establece la dirección del paciente
     * @param direccion Parámetro que define la nueva dirección del paciente
     * */
    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    /*
     * Método que verifica si el paciente está activo
     * @return true si el paciente está activo, false si está inactivo
     * */
    public boolean isActivo() {
        return activo;
    }

    /*
     * Método que obtiene el estado activo del paciente (alternativa para frameworks)
     * @return Estado activo del paciente
     * */
    public boolean getActivo() {
        return activo;
    }

    /*
     * Método que establece el estado activo del paciente
     * @param activo Parámetro que define el nuevo estado del paciente
     * */
    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    /*
     * Método que obtiene la fecha de creación del registro
     * @return Fecha y hora de creación del paciente
     * */
    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    /*
     * Método que establece la fecha de creación del registro
     * @param fechaCreacion Parámetro que define la fecha de creación
     * */
    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    /*
     * Método que obtiene la fecha de última actualización
     * @return Fecha y hora de la última actualización
     * */
    public LocalDateTime getFechaActualizacion() {
        return fechaActualizacion;
    }

    /*
     * Método que establece la fecha de última actualización
     * @param fechaActualizacion Parámetro que define la fecha de actualización
     * */
    public void setFechaActualizacion(LocalDateTime fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }

    /*
     * Método que devuelve una representación en String del paciente
     * @return Cadena de texto con la información básica del paciente
     * */
    @Override
    public String toString() {
        return "Paciente{" +
                "cedula='" + cedula + '\'' +
                ", nombre='" + nombre + '\'' +
                ", correo='" + correo + '\'' +
                ", edad=" + edad +
                ", direccion='" + direccion + '\'' +
                ", activo=" + activo +
                '}';
    }
}
