package filter;

import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import jakarta.ws.rs.ext.Provider;
import java.io.IOException;

/*
 * Author: Mateo Lasso
 * Fecha: 7-12-2025
 * Versión: 1.0
 * Descripción: Esta clase denominada CorsFilter es un filtro de respuesta que configura
 *              las políticas CORS (Cross-Origin Resource Sharing) para permitir solicitudes
 *              desde diferentes dominios en desarrollo.
 * */

@Provider
public class CorsFilter implements ContainerResponseFilter {

    /*
     * Método que filtra las respuestas HTTP para agregar headers CORS
     * @param requestContext Parámetro que define el contexto de la solicitud HTTP
     * @param responseContext Parámetro que define el contexto de la respuesta HTTP
     * @throws IOException Si ocurre un error de entrada/salida durante el filtrado
     * */
    @Override
    public void filter(ContainerRequestContext requestContext,
                       ContainerResponseContext responseContext) throws IOException {

        // Permitir todos los orígenes en desarrollo
        responseContext.getHeaders().add("Access-Control-Allow-Origin", "*");

        // Métodos permitidos
        responseContext.getHeaders().add("Access-Control-Allow-Methods",
                "GET, POST, PUT, DELETE, OPTIONS, HEAD");

        // Headers permitidos
        responseContext.getHeaders().add("Access-Control-Allow-Headers",
                "Origin, X-Requested-With, Content-Type, Accept, Authorization");

        // Permitir credenciales
        responseContext.getHeaders().add("Access-Control-Allow-Credentials", "true");

        // Cache preflight requests
        responseContext.getHeaders().add("Access-Control-Max-Age", "86400");
    }
}
