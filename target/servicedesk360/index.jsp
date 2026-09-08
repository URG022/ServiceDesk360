<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>ServiceDesk 360</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/estilos.css">
</head>
<body>
    <header class="encabezado">
        <h1>Service Desk 360</h1>
        <p>Plataforma empresarial de gestión de soporte técnico</p>
    </header>
    <main class="contenedor">
        <h2>Primera aplicación web desplegada</h2>
        <p>Esta página confirma que el proyecto JSP fue publicado correctamente en Apache Tomcat.</p>
        
        <h3>Objetivos de la semana 1</h3>
        <ul>
            <li>Configurar el entorno Java Web.</li>
            <li>Reconocer la arquitectura cliente-servidor.</li>
            <li>Desplegar una aplicación inicial.</li>
            <li>Documentar el proyecto integrador.</li>
        </ul>
        
        <a class="boton" href="${pageContext.request.contextPath}/diagnostico">
            Ver diagnóstico del sistema
        </a>
    </main>
</body>
</html>