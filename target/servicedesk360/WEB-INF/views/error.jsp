<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Error | ServiceDesk 360</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/estilos.css">
</head>
<body>
<main class="contenedor-formulario">
    <h1>No fue posible completar la operación</h1>
    <div class="alerta error">
        <c:out value="${mensajeError}" />
    </div>
    <div class="acciones-formulario">
        <a href="${pageContext.request.contextPath}/panel" class="boton">Volver al panel</a>
    </div>
</main>
</body>
</html>