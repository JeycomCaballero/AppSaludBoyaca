<%@ page contentType="text/html;charset=UTF-8" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<fmt:setLocale value="${sessionScope.lang != null ? sessionScope.lang : 'es'}"/>
<fmt:setBundle basename="messages"/>

<%@ include file="/views/templates/header.jsp" %>

<div class="container mt-4">

    <h3 class="mb-4">
        <i class="fas fa-clock me-2"></i>
        <fmt:message key="horario.lista.titulo"/>
    </h3>
    <c:if test="${sessionScope.usuario.rol == 'RECEPCIONISTA'}">

        <div class="card shadow-sm mb-4">
            <div class="card-body">

                <form action="${pageContext.request.contextPath}/horarios" method="get"
                      class="row g-3 align-items-end">

                    <input type="hidden" name="action" value="listar">
                    <div class="col-md-6">
                        <label class="form-label">Nombre del médico</label>

                        <input type="text"
                               name="nombreMedico"
                               class="form-control"
                               placeholder="Ej: Carlos Pedraza"
                               required>
                    </div>

                    <div class="col-md-3">
                        <button class="btn btn-success w-100">
                            <i class="fas fa-search me-1"></i>
                            Buscar
                        </button>
                    </div>
                </form>

            </div>
        </div>
    </c:if>

    <!-- TABLA HORARIOS -->
    <div class="card shadow-sm">

        <div class="card-body">

            <div class="table-responsive">

                <table class="table table-hover align-middle">

                    <thead class="table-light">
                        <tr>
                            <th>ID</th>
                            <th><fmt:message key="horario.medico"/></th>
                            <th><fmt:message key="horario.dia"/></th>
                            <th><fmt:message key="horario.hora_inicio"/></th>
                            <th><fmt:message key="horario.hora_fin"/></th>
                            <th>Citas máx.</th>
                        </tr>
                    </thead>

                    <tbody>

                        <c:choose>

                            <c:when test="${not empty horarios}">

                                <c:forEach var="h" items="${horarios}">
                                    <tr>
                                        <td>${h.id}</td>

                                        <td>${h.nombre}</td>

                                        <td>
                                            <c:choose>
                                                <c:when test="${h.diaSemana == 1}">Lunes</c:when>
                                                <c:when test="${h.diaSemana == 2}">Martes</c:when>
                                                <c:when test="${h.diaSemana == 3}">Miércoles</c:when>
                                                <c:when test="${h.diaSemana == 4}">Jueves</c:when>
                                                <c:when test="${h.diaSemana == 5}">Viernes</c:when>
                                                <c:when test="${h.diaSemana == 6}">Sábado</c:when>
                                                <c:otherwise>Domingo</c:otherwise>
                                            </c:choose>
                                        </td>

                                        <td>${h.horaInicio}</td>
                                        <td>${h.horaFin}</td>

                                        <td>${h.maxCitas}</td>

                                    </tr>
                                </c:forEach>

                            </c:when>

                            <c:otherwise>
                                <tr>
                                    <td colspan="6" class="text-center text-muted">
                                        No hay horarios para mostrar
                                    </td>
                                </tr>
                            </c:otherwise>

                        </c:choose>

                    </tbody>

                </table>

            </div>

        </div>

    </div>

</div>

<%@ include file="/views/templates/footer.jsp" %>