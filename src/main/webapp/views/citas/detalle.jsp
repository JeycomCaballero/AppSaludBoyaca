<%@ page contentType="text/html;charset=UTF-8" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<fmt:setLocale value="${sessionScope.lang != null ? sessionScope.lang : 'es'}"/>
<fmt:setBundle basename="messages"/>

<%@ include file="/views/templates/header.jsp" %>

<div class="container mt-4">

    <h3 class="mb-4">
        <i class="fas fa-file-medical me-2"></i>
        <fmt:message key="cita.detalle.titulo"/>
    </h3>

    <div class="card shadow-sm border-0">

        <div class="card-body">

            <!-- PACIENTE -->
            <h4 class="mb-3">
                <i class="fas fa-user-injured me-2 text-primary"></i>
                ${cita.paciente.nombres} ${cita.paciente.apellidos}
            </h4>

            <hr>

            <div class="row">

                <div class="col-md-6 mb-2">
                    <strong><fmt:message key="cita.fecha"/>:</strong>
                    <fmt:formatDate value="${cita.fechaCita}" pattern="dd/MM/yyyy"/>
                </div>

                <div class="col-md-6 mb-2">
                    <strong><fmt:message key="tabla.hora"/>:</strong> ${cita.horaCita}
                </div>

                <div class="col-md-6 mb-2">
                    <strong><fmt:message key="cita.especialidad"/>:</strong> ${cita.especialidad.nombre}
                </div>

                <div class="col-md-6 mb-2">
                    <strong><fmt:message key="horario.medico"/>:</strong>
                    ${cita.medico.nombres} ${cita.medico.apellidos}
                </div>

                <div class="col-md-12 mb-2">
                    <strong><fmt:message key="tabla.estado"/>:</strong>

                    <c:choose>
                        <c:when test="${cita.estado == 'PROGRAMADA'}">
                            <span class="badge bg-primary">
                                <fmt:message key="estado.programada"/>
                            </span>
                        </c:when>
                        <c:when test="${cita.estado == 'ATENDIDA'}">
                            <span class="badge bg-success">
                                <fmt:message key="estado.atendida"/>
                            </span>
                        </c:when>
                        <c:when test="${cita.estado == 'CANCELADA'}">
                            <span class="badge bg-danger">
                                <fmt:message key="estado.cancelada"/>
                            </span>
                        </c:when>
                        <c:otherwise>
                            <span class="badge bg-secondary">${cita.estado}</span>
                        </c:otherwise>
                    </c:choose>

                </div>

                <div class="col-md-12 mb-3">
                    <strong><fmt:message key="cita.motivo"/>:</strong> ${cita.motivo}
                </div>

            </div>

            <hr>

            <!-- BOTONES -->
            <div class="d-flex gap-2">

                <!-- EDITAR -->
                <form action="${pageContext.request.contextPath}/citas" method="post">
                    <input type="hidden" name="id" value="${cita.id}">
                    <button type="submit" name="action" value="editar"
                            class="btn btn-warning">
                        <i class="fas fa-edit me-1"></i>
                        <fmt:message key="editar"/>
                    </button>
                </form>

                <!-- VOLVER -->
                <a href="${pageContext.request.contextPath}/citas"
                   class="btn btn-secondary">
                    <fmt:message key="volver"/>
                </a>

            </div>

        </div>
    </div>

</div>

<%@ include file="/views/templates/footer.jsp" %>