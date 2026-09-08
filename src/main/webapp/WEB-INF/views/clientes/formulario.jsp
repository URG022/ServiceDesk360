<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>${empty cliente.idCliente ? 'Nuevo' : 'Editar'} Cliente</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/estilos.css">
</head>
<body>
    <div class="contenedor">
        <h1>${empty cliente.idCliente ? 'Registrar nuevo cliente' : 'Editar cliente existente'}</h1>

        <c:if test="${not empty error}">
            <p class="alerta-error">${error}</p>
        </c:if>

        <form method="post" action="${pageContext.request.contextPath}/clientes">
            <input type="hidden" name="accion" value="guardar">
            <input type="hidden" name="idCliente" value="${cliente.idCliente}">

            <div class="campo">
                <label for="nombre">Nombre completo:</label><br>
                <input type="text" id="nombre" name="nombre" value="${cliente.nombre}" required maxlength="120">
            </div>

            <div class="campo" style="margin-top:10px;">
                <label for="correo">Correo electrónico:</label><br>
                <input type="email" id="correo" name="correo" value="${cliente.correo}" required maxlength="160">
            </div>

            <div style="margin-top:15px;">
                <button type="submit" class="boton">Guardar</button>
                <a href="${pageContext.request.contextPath}/clientes" class="boton-secundario">Cancelar</a>
            </div>
        </form>
    </div>
</body>
</html>