<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>


<c:if test="${param.lang != null}">
    <c:set var="sessionScope.lang" value="${param.lang}" scope="session"/>
</c:if>

<c:if test="${empty sessionScope.lang}">
    <c:set var="sessionScope.lang" value="es" scope="session"/>
</c:if>

<fmt:setLocale value="${sessionScope.lang}" />
<fmt:setBundle basename="messages" />

<!DOCTYPE html>
<html lang="${sessionScope.lang}">
    <head>
        <meta charset="UTF-8">
        <title><fmt:message key="citas.titulo"/></title>

        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/css/bootstrap.min.css" rel="stylesheet">
        <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" rel="stylesheet">

        <style>
            .consulta-page {
                background: linear-gradient(135deg, #1A5276, #39A900);
                min-height: 100vh;
                display: flex;
                flex-direction: column;
            }
            .consulta-page .language-box {
                position: absolute;
                top: 15px;
                right: 20px;
            }

            .consulta-container {
                flex: 1;
                display: flex;
                justify-content: center;
                align-items: center;
                padding: 20px;
                position: relative;
            }

            .consulta-card {
                width: 100%;
                max-width: 850px;
                border-radius: 18px;
                overflow: hidden;
                background: #fff;
                box-shadow: 0 12px 30px rgba(0,0,0,0.15);
            }

            .consulta-header {
                background: #1A5276;
                color: white;
                text-align: center;
                padding: 25px;
            }

            .captcha-img {
                border-radius: 10px;
                border: 1px solid #ddd;
                max-width: 200px;
            }

            .btn-consulta {
                background: #39A900;
                border: none;
                border-radius: 10px;
                color: white;
            }

            .language-box {
                position: fixed;
                top: 15px;
                right: 20px;
                z-index: 9999;
            }

            .footer-consulta {
                text-align: center;
                color: white;
                font-size: 13px;
                margin-bottom: 10px;
            }
        </style>
    </head>

    <body class="consulta-page">

        <!-- 🌐 IDIOMA -->
        <div class="language-box">
            <div class="dropdown">
                <button class="btn btn-light btn-sm dropdown-toggle" style="z-index: 2;" data-bs-toggle="dropdown">
                    🌐
                </button>
                <ul class="dropdown-menu dropdown-menu-end">
                    <li><a class="dropdown-item" href="?lang=es">🇨🇴 Español</a></li>
                    <li><a class="dropdown-item" href="?lang=en">🇺🇸 English</a></li>
                    <li><a class="dropdown-item" href="?lang=it">🇮🇹 Italiano</a></li>
                </ul>
            </div>
        </div>

        <div class="consulta-container">

            <div class="consulta-card">

                <!-- HEADER -->
                <div class="consulta-header">
                    <i class="fa-solid fa-calendar-check fa-2x mb-2"></i>
                    <h4><fmt:message key="citas.titulo"/></h4>
                    <small><fmt:message key="citas.subtitulo"/></small>
                </div>

                <div class="p-4">

                    <!-- ERROR -->
                    <c:if test="${not empty error}">
                        <div class="alert alert-danger text-center">
                            ${error}
                        </div>
                    </c:if>

                    <!-- FORM -->
                    <c:if test="${empty registros}">
                        <form action="${pageContext.request.contextPath}/consulta-cita" method="post">

                            <div class="mb-3">
                                <label class="form-label fw-bold">
                                    <fmt:message key="citas.documento"/>
                                </label>
                                <input type="text" name="documento" class="form-control" required>
                            </div>

                            <div class="mb-3 text-center">

                                <c:if test="${not empty captchaImage}">
                                    <img src="${captchaImage}" class="captcha-img mb-2"/>
                                </c:if>

                                <input type="text"
                                       name="captcha"
                                       class="form-control text-center"
                                       placeholder="<fmt:message key='citas.captcha'/>"
                                       required>
                            </div>

                            <button class="btn btn-consulta w-100">
                                <i class="fa-solid fa-magnifying-glass me-2"></i>
                                <fmt:message key="citas.consultar"/>
                            </button>

                        </form>
                    </c:if>

                    <!-- RESULTADOS -->
                    <c:if test="${not empty registros}">

                        <hr>

                        <h5 class="mb-3 text-success">
                            <fmt:message key="citas.resultados"/>
                        </h5>

                        <div class="table-responsive">

                            <table class="table table-hover align-middle">

                                <thead class="table-light">
                                    <tr>
                                        <th><fmt:message key="citas.fecha"/></th>
                                        <th><fmt:message key="citas.hora"/></th>
                                        <th><fmt:message key="citas.medico"/></th>
                                        <th><fmt:message key="citas.especialidad"/></th>
                                        <th><fmt:message key="citas.estado"/></th>
                                        <th><fmt:message key="citas.pdf"/></th>
                                    </tr>
                                </thead>

                                <tbody>
                                    <c:forEach var="c" items="${registros}">
                                        <tr>
                                            <td>${c.fechaCita}</td>
                                            <td>${c.horaCita}</td>
                                            <td>${c.medico.nombres}</td>
                                            <td>${c.especialidad.nombre}</td>
                                            <td>
                                                <span class="badge bg-info text-dark">
                                                    ${c.estado}
                                                </span>
                                            </td>
                                            <td>
                                                <form action="${pageContext.request.contextPath}/consulta-cita" method="get">
                                                    <input type="hidden" name="id" value="${c.id}">

                                                    <button type="submit" name="accion" value="descargar" class="btn btn-danger btn-sm">
                                                        <i class="fa-solid fa-file-pdf me-1"></i>
                                                        <fmt:message key="citas.descargar"/>
                                                    </button>
                                                </form>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </tbody>

                            </table>

                        </div>

                    </c:if>
                    <div class="text-center mt-3">
                        <a href="${pageContext.request.contextPath}/login"
                           class="btn btn-light btn-sm border">
                            <i class="fa-solid fa-arrow-left me-1"></i>
                            <fmt:message key="app.volver.inicio"/>
                        </a>
                    </div>

                </div>
            </div>

        </div>

        <div class="footer-consulta">
            © 2026 SaludBoyacá
        </div>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.bundle.min.js"></script>

    </body>
</html>