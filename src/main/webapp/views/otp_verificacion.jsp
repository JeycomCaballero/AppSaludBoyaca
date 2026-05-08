<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<c:if test="${empty sessionScope.lang}">
    <c:set var="lang" value="es" scope="session" />
</c:if>

<fmt:setLocale value="${sessionScope.lang}" />
<fmt:setBundle basename="messages"/>

<!DOCTYPE html>

<html lang="${sessionScope.lang}">
    <head>
        <meta charset="UTF-8">
        <title><fmt:message key="otp.titulo"/></title>

        <meta name="viewport" content="width=device-width, initial-scale=1">

        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
        <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css" rel="stylesheet">
        <link href="${pageContext.request.contextPath}/resources/css/OTPStyle.css" rel="stylesheet">

    </head>

    <body class="otp-page">

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

        <!-- CONTENIDO -->

        <div class="otp-container">

            ```
            <div class="otp-card">

                <!-- HEADER -->
                <div class="otp-header">
                    <i class="fas fa-shield-alt fa-3x mb-2"></i>
                    <h4><fmt:message key="otp.titulo"/></h4>
                    <small class="text-white-50">
                        <fmt:message key="otp.subtitulo"/>
                    </small>
                </div>

                <!-- BODY -->
                <div class="p-4">

                    <c:if test="${not empty error}">
                        <div class="alert alert-danger text-center">
                            ${error}
                        </div>
                    </c:if>

                    <p class="text-center text-muted">
                        <fmt:message key="otp.instruccion"/>
                    </p>

                    <p class="text-center small text-secondary">
                        Código enviado a:
                        <strong>${emailEnmascarado}</strong>
                    </p>

                    <!-- TIMER -->
                    <div class="text-center mb-3">
                        <span class="badge bg-warning text-dark timer-badge">
                            <fmt:message key="otp.expira"/>:
                            <span id="timer">05:00</span>
                        </span>
                    </div>

                    <!-- FORM -->
                    <form id="otpForm" action="${pageContext.request.contextPath}/otp" method="post">

                        <div class="mb-3 text-center">
                            <label class="form-label fw-bold">
                                <fmt:message key="otp.campo"/>
                            </label>

                            <input type="text"
                                   name="otp"
                                   class="form-control otp-input text-center"
                                   maxlength="6"
                                   pattern="[0-9]{6}"
                                   placeholder="000000"
                                   required>
                        </div>

                        <button type="submit" name="action" value="verificar" class="btn btn-otp w-100">
                            <i class="fas fa-check-circle me-2"></i>
                            <fmt:message key="otp.verificar"/>
                        </button>
                    </form>

                    <!-- REENVIAR -->
                    <form action="${pageContext.request.contextPath}/otp" method="post">
                        <button type="submit" name="action" value="reenviar" class="btn btn-otp w-100 mt-3">
                            <fmt:message key="otp.reenviar"/>
                        </button>
                    </form>

                    <div class="text-center mt-3">
                        <a href="${pageContext.request.contextPath}/login" class="small text-decoration-none">
                            ← <fmt:message key="app.volver.inicio"/>
                        </a>
                    </div>

                </div>

            </div>

        </div>

        <div class="footer-otp">
            © 2026 SaludBoyacá
        </div>

        <script>
            const DURACION = 300;
            const startTime = ${sessionScope.otpStartTime};

            const ahora = new Date().getTime();
            let tiempo = Math.floor((DURACION * 1000 - (ahora - startTime)) / 1000);

            if (tiempo < 0)
                tiempo = 0;

            const timer = document.getElementById("timer");
            const form = document.getElementById("otpForm");

            function actualizarTimer() {

                let min = Math.floor(tiempo / 60);
                let seg = tiempo % 60;

                timer.textContent =
                        String(min).padStart(2, '0') + ":" +
                        String(seg).padStart(2, '0');

                if (tiempo <= 0) {
                    timer.textContent = "<fmt:message key='otp.expirado'/>";

                    timer.parentElement.classList.remove("bg-warning");
                    timer.parentElement.classList.add("bg-danger");

                    form.querySelector("button").disabled = true;
                    return;
                }

                tiempo--;
            }

            setInterval(actualizarTimer, 1000);
        </script>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
        <script>

            <%
                String msg = (String) request.getAttribute("msg");

                if (msg == "Se inicio sesion correctamente" && msg != null) {%>
            Swal.fire({
                icon: 'success',
                title: '<%= msg%>',
                confirmButtonColor: '#1c5fa8',
                background: '#1e293b',
                color: 'white',
                timer: 1500,
                showConfirmButton: false
            }).then(() => {
                window.location.href = "dashboard";
            });
            <% }%>
        </script>

    </body>
</html>
