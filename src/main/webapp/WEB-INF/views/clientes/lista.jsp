<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Mantenimiento de Clientes - ServiceDesk 360</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/estilos.css">
</head>
<body>
    <div class="contenedor">
        <h1>Catálogo de Clientes</h1>
        <p>
            <a href="${pageContext.request.contextPath}/clientes?accion=nuevo" class="boton">Nuevo cliente</a>
            <a href="${pageContext.request.contextPath}/panel" class="boton-secundario">Volver al panel</a>
        </p>

        <c:if test="${not empty sessionScope.flash}">
            <p class="alerta-exito">${sessionScope.flash}</p>
            <c:remove var="flash" scope="session"/>
        </c:if>

        <c:if test="${not empty sessionScope.flashError}">
            <p class="alerta-error">${sessionScope.flashError}</p>
            <c:remove var="flashError" scope="session"/>
        </c:if>

        <table class="tabla-datos">
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Nombre</th>
                    <th>Correo</th>
                    <th>Estado</th>
                    <th>Acciones</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="c" items="${clientes}">
                    <tr>
                        <td>${c.idCliente}</td>
                        <td>${c.nombre}</td>
                        <td>${c.correo}</td>
                        <td>
                            <span class="${c.activo ? 'estado-activo' : 'estado-inactivo'}">
                                ${c.activo ? 'Activo' : 'Inactivo'}
                            </span>
                        </td>
                        <td>
                            <a href="${pageContext.request.contextPath}/clientes?accion=editar&id=${c.idCliente}">Editar</a> |
                            
                            <form method="post" action="${pageContext.request.contextPath}/clientes" style="display:inline">
                                <input type="hidden" name="accion" value="estado">
                                <input type="hidden" name="id" value="${c.idCliente}">
                                <input type="hidden" name="activo" value="${!c.activo}">
                                <button type="submit" style="cursor:pointer; background:none; border:none; color:blue; text-decoration:underline;">
                                    ${c.activo ? 'Desactivar' : 'Activar'}
                                </button>
                            </form> |

                            <form method="post" action="${pageContext.request.contextPath}/clientes" style="display:inline" onsubmit="return confirm('¿Confirma eliminación física? Si tiene equipos o tickets asociados, fallará por integridad.');">
                                <input type="hidden" name="accion" value="eliminar">
                                <input type="hidden" name="id" value="${c.idCliente}">
                                <button type="submit" style="cursor:pointer; background:none; border:none; color:red; text-decoration:underline;">
                                    Eliminar
                                </button>
                            </form>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>
</body>
</html>