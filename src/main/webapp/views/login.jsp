<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<c:if test="${empty sessionScope.lang}">
    <c:set var="sessionScope.lang" value="es" />
</c:if>

<fmt:setLocale value="${sessionScope.lang}" />
<fmt:setBundle basename="messages" />

<!DOCTYPE html>

<html lang="${sessionScope.lang}">
    <head>
        <meta charset="UTF-8">
        <title><fmt:message key="login.titulo"/> - SaludBoyacá</title>

        <meta name="viewport" content="width=device-width, initial-scale=1">

        <!-- Bootstrap -->
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">

        <!-- FontAwesome -->
        <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css" rel="stylesheet">

        <!-- CSS propio -->
        <link href="${pageContext.request.contextPath}/resources/css/loginStyle.css" rel="stylesheet">

    </head>

    <body class="login-page">

        <!-- 🌐 Idioma -->
        <div class="language-box">
            <div class="dropdown">
                <button class="btn btn-light btn-sm dropdown-toggle" data-bs-toggle="dropdown">
                    🌐
                </button>
                <ul class="dropdown-menu dropdown-menu-end">
                    <li><a class="dropdown-item" href="?lang=es">🇨🇴 Español</a></li>
                    <li><a class="dropdown-item" href="?lang=en">🇺🇸 English</a></li>
                    <li><a class="dropdown-item" href="?lang=it">🇮🇹 Italiano</a></li>
                </ul>
            </div>
        </div>

        <!-- LOGIN -->
        <div class="login-container">

            <div class="login-card">

                <!-- HEADER -->
                <div class="login-header">
                    <i class="fas fa-heartbeat"></i>
                    <h4>SaludBoyacá</h4>
                    <small><fmt:message key="app.institucion"/></small>
                </div>

                <!-- BODY -->
                <div class="p-4">

                    <c:if test="${not empty error}">
                        <div class="alert alert-danger text-center">
                            ${error}
                        </div>
                    </c:if>

                    <form action="${pageContext.request.contextPath}/login" method="post">

                        <div class="mb-3">
                            <label><fmt:message key="login.usuario"/></label>
                            <div class="input-group">
                                <span class="input-group-text">
                                    <i class="fas fa-user"></i>
                                </span>
                                <input type="text" name="username" class="form-control" required>
                            </div>
                        </div>

                        <div class="mb-3">
                            <label><fmt:message key="login.contrasena"/></label>
                            <div class="input-group">
                                <span class="input-group-text">
                                    <i class="fas fa-lock"></i>
                                </span>
                                <input type="password" name="password" class="form-control" required>
                            </div>
                        </div>

                        <button class="btn btn-login w-100" type="submit" id="btnLogin">
                            <i class="fas fa-sign-in-alt me-2"></i>
                            <fmt:message key="login.ingresar"/>
                        </button>

                    </form>

                    <div class="text-center mt-3">
                        <a href="consulta-cita" class="small">
                            🔎 <fmt:message key="nav.consulta"/>
                        </a>
                    </div>

                </div>

            </div>

        </div>

        <div class="footer-login">
            © 2026 SaludBoyacá
        </div>
        <div id="loadingOverlay" class="loading-overlay d-none">
            <div class="loading-box text-center">
                <div class="spinner-border text-primary mb-3"></div>
                <h5>Enviando código...</h5>
                <small>Por favor espera</small>
            </div>
        </div>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>

        <script>
            <%
                String msg = (String) request.getAttribute("msg");

                if (msg != null) {
            %>
            Swal.fire({
                icon: 'error',
                title: "<%= msg%>",
                confirmButtonColor: '#1c5fa8',
                background: '#1e293b',
                color: 'white',
                timer: 2000,
                showConfirmButton: false
            });
            <% }%>
        </script>

        <script>
            const form = document.querySelector("form");
            const overlay = document.getElementById("loadingOverlay");
            const btn = document.getElementById("btnLogin");

            form.addEventListener("submit", function () {
                overlay.classList.remove("d-none");
                btn.disabled = true;
            });
        </script>

    </body>
</html>