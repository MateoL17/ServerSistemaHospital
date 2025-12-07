package dao;

import model.Paciente;
import javax.naming.InitialContext;
import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/*
 * Author: Mateo Lasso
 * Fecha: 7-12-2025
 * Versión: 1.0
 * Descripción: Esta clase denominada PacienteDAO es un objeto de acceso a datos (DAO) que maneja
 *              las operaciones CRUD para la entidad Paciente en la base de datos MySQL.
 * */

public class PacienteDAO {

    /*
     * Método que obtiene una conexión a la base de datos usando DataSource
     * @return Objeto Connection para interactuar con la base de datos
     * @throws SQLException Si ocurre un error al obtener la conexión
     * */
    private Connection getConnection() throws SQLException {
        try {
            InitialContext ctx = new InitialContext();
            DataSource ds = (DataSource) ctx.lookup("java:/MySqlDS");
            return ds.getConnection();
        } catch (Exception e) {
            throw new SQLException("No se pudo obtener conexión de DataSource", e);
        }
    }

    /*
     * Método que obtiene todos los pacientes de la base de datos
     * @return Lista de objetos Paciente con todos los registros encontrados
     * */
    public List<Paciente> getAll() {
        List<Paciente> pacientes = new ArrayList<>();
        String sql = "SELECT cedula, nombre, correo, edad, direccion, activo FROM paciente";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Paciente paciente = new Paciente();
                paciente.setCedula(rs.getString("cedula"));
                paciente.setNombre(rs.getString("nombre"));
                paciente.setCorreo(rs.getString("correo"));
                paciente.setEdad(rs.getInt("edad"));
                paciente.setDireccion(rs.getString("direccion"));
                paciente.setActivo(rs.getBoolean("activo"));
                pacientes.add(paciente);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return pacientes;
    }

    /*
     * Método que obtiene un paciente específico por su número de cédula
     * @param cedula Parámetro que define el número de cédula del paciente a buscar
     * @return Objeto Paciente si se encuentra, null si no existe
     * */
    public Paciente getByCedula(String cedula) {
        String sql = "SELECT cedula, nombre, correo, edad, direccion, activo FROM paciente WHERE cedula = ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, cedula);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Paciente paciente = new Paciente();
                    paciente.setCedula(rs.getString("cedula"));
                    paciente.setNombre(rs.getString("nombre"));
                    paciente.setCorreo(rs.getString("correo"));
                    paciente.setEdad(rs.getInt("edad"));
                    paciente.setDireccion(rs.getString("direccion"));
                    paciente.setActivo(rs.getBoolean("activo"));
                    return paciente;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /*
     * Método que crea un nuevo paciente en la base de datos
     * @param paciente Parámetro que define el objeto Paciente a insertar
     * @return true si el paciente fue creado exitosamente, false en caso contrario
     * */
    public boolean create(Paciente paciente) {
        String sql = "INSERT INTO paciente (cedula, nombre, correo, edad, direccion, activo) VALUES (?, ?, ?, ?, ?, TRUE)";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, paciente.getCedula());
            pstmt.setString(2, paciente.getNombre());
            pstmt.setString(3, paciente.getCorreo());
            pstmt.setInt(4, paciente.getEdad());
            pstmt.setString(5, paciente.getDireccion());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /*
     * Método que actualiza la información de un paciente existente
     * @param paciente Parámetro que define el objeto Paciente con los datos actualizados
     * @return true si el paciente fue actualizado exitosamente, false en caso contrario
     * */
    public boolean update(Paciente paciente) {
        String sql = "UPDATE paciente SET nombre = ?, correo = ?, edad = ?, direccion = ?, activo = ? WHERE cedula = ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, paciente.getNombre());
            pstmt.setString(2, paciente.getCorreo());
            pstmt.setInt(3, paciente.getEdad());
            pstmt.setString(4, paciente.getDireccion());
            pstmt.setBoolean(5, paciente.getActivo());
            pstmt.setString(6, paciente.getCedula());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /*
     * Método que elimina permanentemente un paciente de la base de datos
     * @param cedula Parámetro que define el número de cédula del paciente a eliminar
     * @return true si el paciente fue eliminado exitosamente, false en caso contrario
     * */
    public boolean delete(String cedula) {
        String sql = "DELETE FROM paciente WHERE cedula = ?"; // DELETE permanente

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, cedula);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /*
     * Método que activa o desactiva el estado de un paciente
     * @param cedula Parámetro que define el número de cédula del paciente
     * @param activo Parámetro que define el nuevo estado del paciente (true=activo, false=inactivo)
     * @return true si el estado fue actualizado exitosamente, false en caso contrario
     * */
    public boolean toggleActivo(String cedula, boolean activo) {
        String sql = "UPDATE paciente SET activo = ? WHERE cedula = ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setBoolean(1, activo);
            pstmt.setString(2, cedula);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /*
     * Método que verifica si un número de cédula ya existe en la base de datos
     * @param cedula Parámetro que define el número de cédula a verificar
     * @return true si la cédula ya existe, false si no existe
     * */
    public boolean existeCedula(String cedula) {
        String sql = "SELECT COUNT(*) FROM paciente WHERE cedula = ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, cedula);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
