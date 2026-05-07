<%@ page contentType="text/html;charset=UTF-8" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<fmt:setLocale value="${sessionScope.lang != null ? sessionScope.lang : 'es'}"/>
<fmt:setBundle basename="messages"/>

<%@ include file="/views/templates/header.jsp" %>

<h3 class="mb-4">
    <i class="fas fa-calendar-plus me-2"></i>
    <fmt:message key="cita.form.titulo"/>
</h3>

<div class="row justify-content-center">
    <div class="col-md-6">

        <div class="card shadow-sm">
            <div class="card-body">

                <form action="${pageContext.request.contextPath}/citas" method="post">

                    <input type="hidden" name="action"
                           value="${empty cita ? 'insertar' : 'actualizar'}"/>

                    <input type="hidden" name="id" value="${cita.id}"/>

                    <!-- PACIENTE -->
                    <div class="mb-3">
                        <label class="form-label">
                            <fmt:message key="cita.paciente"/>
                        </label>

                        <c:choose>

                            <c:when test="${not empty cita}">
                                <input type="hidden" name="paciente" value="${cita.paciente.id}"/>

                                <input type="text" class="form-control"
                                       value="${cita.paciente.nombres} ${cita.paciente.apellidos}"
                                       readonly>
                            </c:when>

                            <c:otherwise>
                                <select name="paciente" class="form-control" required>
                                    <c:forEach var="p" items="${pacientes}">
                                        <option value="${p.id}">
                                            ${p.nombres} ${p.apellidos}
                                        </option>
                                    </c:forEach>
                                </select>
                            </c:otherwise>

                        </c:choose>
                    </div>

                    <!-- MEDICO -->
                    <div class="mb-3">
                        <label class="form-label">
                            <fmt:message key="horario.medico"/>
                        </label>
                        <select name="medico" class="form-control" required>
                            <c:forEach var="m" items="${medicos}">
                                <option value="${m.id}"
                                        <c:if test="${cita.medico.id == m.id}">selected</c:if>>
                                    ${m.nombres} ${m.apellidos}
                                </option>
                            </c:forEach>
                        </select>
                    </div>

                    <!-- ESPECIALIDAD -->
                    <div class="mb-3">
                        <label class="form-label">
                            <fmt:message key="cita.especialidad"/>
                        </label>
                        <select name="especialidad" class="form-control" required>
                            <c:forEach var="e" items="${especialidades}">
                                <option value="${e.id}"
                                        <c:if test="${cita.especialidad.id == e.id}">selected</c:if>>
                                    ${e.nombre}
                                </option>
                            </c:forEach>
                        </select>
                    </div>

                    <!-- FECHA -->
                    <div class="mb-3">
                        <label class="form-label">
                            <fmt:message key="cita.fecha"/>
                        </label>
                        <input type="date" name="fecha" class="form-control"
                               value="${cita.fechaCita}" required>
                    </div>

                    <!-- HORA -->
                    <div class="mb-3">
                        <label class="form-label">
                            <fmt:message key="tabla.hora"/>
                        </label>
                        <input type="time" name="hora" class="form-control"
                               value="${cita.horaCita}" required>
                    </div>

                    <!-- MOTIVO -->
                    <div class="mb-3">
                        <label class="form-label">
                            <fmt:message key="cita.motivo"/>
                        </label>
                        <input type="text" name="motivo" class="form-control"
                               value="${cita.motivo}">
                    </div>

                    <!-- ESTADO -->
                    <div class="mb-3">
                        <label class="form-label">
                            <fmt:message key="cita.estado"/>
                        </label>
                        <select name="estado" class="form-control">

                            <option value="PROGRAMADA"
                                <c:if test="${cita.estado == 'PROGRAMADA'}">selected</c:if>>
                                <fmt:message key="estado.programada"/>
                            </option>

                            <option value="ATENDIDA"
                                <c:if test="${cita.estado == 'ATENDIDA'}">selected</c:if>>
                                <fmt:message key="estado.atendida"/>
                            </option>

                            <option value="CANCELADA"
                                <c:if test="${cita.estado == 'CANCELADA'}">selected</c:if>>
                                <fmt:message key="estado.cancelada"/>
                            </option>

                        </select>
                    </div>

                    <!-- BOTÓN -->
                    <button type="submit" class="btn btn-success w-100">
                        <fmt:message key="guardar"/>
                    </button>

                </form>

            </div>
        </div>

    </div>
</div>

<%@ include file="/views/templates/footer.jsp" %>