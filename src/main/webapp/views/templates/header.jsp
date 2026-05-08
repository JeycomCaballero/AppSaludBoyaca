<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<fmt:setLocale value="${sessionScope.lang != null ? sessionScope.lang : 'es'}"/>
<fmt:setBundle basename="messages"/>

<!DOCTYPE html>
<html lang="${sessionScope.lang}">
    <head>
        <meta charset="UTF-8">
        <title>SaludBoyacá</title>

        <meta name="viewport" content="width=device-width, initial-scale=1">

        <!-- Bootstrap -->
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">

        <!-- Icons -->
        <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css" rel="stylesheet">

        <!-- CSS -->
        <link href="${pageContext.request.contextPath}/resources/css/headerStyle.css" rel="stylesheet">
    </head>

    <body class="dashboard-page">

        <div class="layout">

            <!-- SIDEBAR -->
            <aside class="sidebar">
                <h2 class="logo">❤️ SaludBoyacá</h2>

                <nav>
                    <a href="${pageContext.request.contextPath}/dashboard" class="nav-item">
                        <i class="fas fa-home"></i> Dashboard
                    </a>

                    <a href="${pageContext.request.contextPath}/pacientes" class="nav-item">
                        <i class="fas fa-user-injured"></i> Pacientes
                    </a>

                    <a href="${pageContext.request.contextPath}/citas" class="nav-item">
                        <i class="fas fa-calendar-check"></i> Citas
                    </a>

                    <a href="${pageContext.request.contextPath}/horarios" class="nav-item">
                        <i class="fas fa-clock"></i> Horarios
                    </a>

                    <a href="${pageContext.request.contextPath}/consulta-cita" class="nav-item">
                        <i class="fas fa-search"></i> Consulta
                    </a>
                </nav>

                <div class="logout">
                    <a href="${pageContext.request.contextPath}/logout">
                        <i class="fas fa-sign-out-alt"></i> <fmt:message key="logout"/>
                    </a>
                </div>
            </aside>

            <!-- CONTENIDO -->
            <main class="main-content">

                <!-- TOPBAR -->
                <header class="topbar">
                    <div>
                        👨‍⚕️ ${sessionScope.usuario.nombres}
                    </div>

                    <div class="dropdown">
                        <button class="btn btn-light btn-sm dropdown-toggle" data-bs-toggle="dropdown">
                            🌐
                        </button>

                        <ul class="dropdown-menu dropdown-menu-end">
                            <li>
                                <a class="dropdown-item" href="?lang=es">
                                    🇨🇴 Español
                                </a>
                            </li>

                            <li>
                                <a class="dropdown-item" href="?lang=en">
                                    🇺🇸 English
                                </a>
                            </li>

                            <li>
                                <a class="dropdown-item" href="?lang=it">
                                    🇮🇹 Italiano
                                </a>
                            </li>
                        </ul>
                    </div>
                </header>

                <div class="content">