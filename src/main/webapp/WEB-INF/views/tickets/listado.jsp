<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Listado de Tickets | ServiceDesk 360</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/estilos.css">
</head>
<body>
<main class="contenedor">
    <h1>Tickets de soporte</h1>

    <c:if test="${not empty mensajeExito}">
        <div class="alerta exito">
            <c:out value="${mensajeExito}" />
        </div>
    </c:if>

    <div class="acciones-formulario" style="margin-bottom: 20px;">
        <a href="${pageContext.request.contextPath}/tickets/nuevo" class="boton">Abrir nuevo ticket</a>
        <a href="${pageContext.request.contextPath}/panel">Volver al panel</a>
    </div>

    <table style="width: 100%; border-collapse: collapse; text-align: left;">
        <thead>
            <tr style="background-color: var(--rojo-itca); color: white;">
                <th style="padding: 10px; border: 1px solid #ccc;">ID</th>
                <th style="padding: 10px; border: 1px solid #ccc;">Título</th>
                <th style="padding: 10px; border: 1px solid #ccc;">Solicitante</th>
                <th style="padding: 10px; border: 1px solid #ccc;">Prioridad</th>
                <th style="padding: 10px; border: 1px solid #ccc;">Estado</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="ticket" items="${tickets}">
                <tr style="background: white;">
                    <td style="padding: 10px; border: 1px solid #ccc;"><c:out value="${ticket.id}" /></td>
                    <td style="padding: 10px; border: 1px solid #ccc;"><c:out value="${ticket.titulo}" /></td>
                    <td style="padding: 10px; border: 1px solid #ccc;"><c:out value="${ticket.solicitante.nombreCompleto}" /></td>
                    <td style="padding: 10px; border: 1px solid #ccc;"><c:out value="${ticket.prioridad}" /></td>
                    <td style="padding: 10px; border: 1px solid #ccc;"><c:out value="${ticket.estado}" /></td>
                </tr>
            </c:forEach>
            <c:if test="${empty tickets}">
                <tr>
                    <td colspan="5" style="padding: 15px; text-align: center; border: 1px solid #ccc;">No hay tickets registrados actualmente.</td>
                </tr>
            </c:if>
        </tbody>
    </table>
</main>
</body>
</html>