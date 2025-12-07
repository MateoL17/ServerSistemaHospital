package resource;

import dao.PacienteDAO;
import model.Paciente;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("/pacientes")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PacienteResource {

    private final PacienteDAO pacienteDAO = new PacienteDAO();

    // GET /api/pacientes - Obtener TODOS los pacientes
    @GET
    public Response getAll() {
        List<Paciente> pacientes = pacienteDAO.getAll();
        return Response.ok(pacientes).build();
    }

    // GET /api/pacientes/{cedula} - Obtener paciente por cédula
    @GET
    @Path("/{cedula}")
    public Response getByCedula(@PathParam("cedula") String cedula) {
        Paciente paciente = pacienteDAO.getByCedula(cedula);
        if (paciente != null) {
            return Response.ok(paciente).build();
        }
        return Response.status(Response.Status.NOT_FOUND)
                .entity("{\"error\": \"Paciente no encontrado\"}")
                .build();
    }

    // POST /api/pacientes - Crear nuevo paciente
    @POST
    public Response create(Paciente paciente) {
        try {
            // Validar cédula
            if (!validarCedulaEcuatoriana(paciente.getCedula())) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("{\"error\": \"Cédula ecuatoriana inválida\"}")
                        .build();
            }

            // Validar campos obligatorios
            if (paciente.getNombre() == null || paciente.getNombre().trim().isEmpty() ||
                    paciente.getCorreo() == null || paciente.getCorreo().trim().isEmpty() ||
                    paciente.getCedula() == null || paciente.getCedula().trim().isEmpty()) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("{\"error\": \"Nombre, cédula y correo son obligatorios\"}")
                        .build();
            }

            // Validar edad
            if (paciente.getEdad() <= 0 || paciente.getEdad() > 120) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("{\"error\": \"Edad debe estar entre 1 y 120 años\"}")
                        .build();
            }

            // Verificar si cédula ya existe
            if (pacienteDAO.existeCedula(paciente.getCedula())) {
                return Response.status(Response.Status.CONFLICT)
                        .entity("{\"error\": \"La cédula ya está registrada\"}")
                        .build();
            }

            // Crear paciente
            boolean creado = pacienteDAO.create(paciente);
            if (creado) {
                Paciente pacienteCreado = pacienteDAO.getByCedula(paciente.getCedula());

                if (pacienteCreado != null) {
                    return Response.status(Response.Status.CREATED)
                            .entity(pacienteCreado)
                            .build();
                } else {
                    paciente.setActivo(true);
                    return Response.status(Response.Status.CREATED)
                            .entity(paciente)
                            .build();
                }
            } else {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                        .entity("{\"error\": \"Error al crear paciente en la base de datos\"}")
                        .build();
            }

        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"Error interno: " + e.getMessage() + "\"}")
                    .build();
        }
    }

    // PUT /api/pacientes/{cedula} - Actualizar paciente
    @PUT
    @Path("/{cedula}")
    public Response update(@PathParam("cedula") String cedula, Paciente paciente) {
        if (!cedula.equals(paciente.getCedula())) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\": \"La cédula no coincide\"}")
                    .build();
        }

        if (!pacienteDAO.existeCedula(cedula)) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"error\": \"Paciente no encontrado\"}")
                    .build();
        }

        boolean actualizado = pacienteDAO.update(paciente);
        if (actualizado) {
            return Response.ok(paciente).build();
        }
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("{\"error\": \"Error al actualizar paciente\"}")
                .build();
    }

    // PUT /api/pacientes/{cedula}/activar - Activar paciente
    @PUT
    @Path("/{cedula}/activar")
    public Response activar(@PathParam("cedula") String cedula) {
        boolean actualizado = pacienteDAO.toggleActivo(cedula, true);
        if (actualizado) {
            return Response.ok("{\"message\": \"Paciente activado\"}").build();
        }
        return Response.status(Response.Status.NOT_FOUND)
                .entity("{\"error\": \"Paciente no encontrado\"}")
                .build();
    }

    // PUT /api/pacientes/{cedula}/desactivar - Ahora será eliminación física
    @PUT
    @Path("/{cedula}/desactivar")
    public Response desactivar(@PathParam("cedula") String cedula) {
        boolean eliminado = pacienteDAO.delete(cedula); // Llama a delete para borrado físico
        if (eliminado) {
            return Response.ok("{\"message\": \"Paciente eliminado permanentemente\"}").build();
        }
        return Response.status(Response.Status.NOT_FOUND)
                .entity("{\"error\": \"Paciente no encontrado\"}")
                .build();
    }

    // DELETE /api/pacientes/{cedula} - Eliminar paciente (BORRADO FÍSICO)
    @DELETE
    @Path("/{cedula}")
    public Response delete(@PathParam("cedula") String cedula) {
        boolean eliminado = pacienteDAO.delete(cedula);
        if (eliminado) {
            return Response.ok("{\"message\": \"Paciente eliminado permanentemente\"}").build();
        }
        return Response.status(Response.Status.NOT_FOUND)
                .entity("{\"error\": \"Paciente no encontrado\"}")
                .build();
    }

    // Método para validar cédula ecuatoriana
    private boolean validarCedulaEcuatoriana(String cedula) {
        if (cedula == null || cedula.length() != 10) {
            return false;
        }

        if (!cedula.matches("\\d{10}")) {
            return false;
        }

        int[] coeficientes = {2, 1, 2, 1, 2, 1, 2, 1, 2};
        int verificador = Integer.parseInt(cedula.substring(9, 10));
        int suma = 0;

        for (int i = 0; i < 9; i++) {
            int valor = Integer.parseInt(cedula.substring(i, i + 1)) * coeficientes[i];
            suma += (valor > 9) ? valor - 9 : valor;
        }

        int digitoCalculado = (suma % 10 == 0) ? 0 : 10 - (suma % 10);

        return digitoCalculado == verificador;
    }
}
