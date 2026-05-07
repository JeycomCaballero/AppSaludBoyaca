<%@ page contentType="text/html;charset=UTF-8" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<fmt:setLocale value="${sessionScope.lang != null ? sessionScope.lang : 'es'}"/>
<fmt:setBundle basename="messages"/>

<%@ include file="/views/templates/header.jsp" %>

<h3 class="mb-4">
    <i class="fas fa-user-plus me-2"></i>
    <fmt:message key="paciente.form.titulo"/>
</h3>

<div class="row justify-content-center">
    <div class="col-md-6">

        <div class="card shadow-sm">
            <div class="card-body">

                <form action="${pageContext.request.contextPath}/pacientes" method="post">

                    <input type="hidden" name="action"
                           value="${empty paciente ? 'insertar' : 'actualizar'}"/>

                    <input type="hidden" name="id" value="${paciente.id}"/>

                    <!-- NOMBRES -->
                    <div class="mb-3">
                        <label class="form-label">
                            <fmt:message key="paciente.nombre"/>
                        </label>
                        <input type="text" name="nombres" class="form-control"
                               value="${paciente.nombres}" required>
                    </div>

                    <!-- APELLIDOS -->
                    <div class="mb-3">
                        <label class="form-label">
                            <fmt:message key="paciente.apellido"/>
                        </label>
                        <input type="text" name="apellidos" class="form-control"
                               value="${paciente.apellidos}" required>
                    </div>

                    <!-- DOCUMENTO -->
                    <div class="mb-3">
                        <label class="form-label">
                            <fmt:message key="paciente.documento"/>
                        </label>
                        <input type="text" name="documento" class="form-control"
                               value="${paciente.documento}" required>
                    </div>

                    <!-- FECHA NACIMIENTO -->
                    <div class="mb-3">
                        <label class="form-label">
                            <fmt:message key="paciente.fecha"/>
                        </label>

                        <fmt:formatDate value="${paciente.fechaNacimiento}"
                                        pattern="yyyy-MM-dd"
                                        var="fecha"/>

                        <input type="date" name="fechaNacimiento"
                               class="form-control"
                               value="${fecha}" required>
                    </div>

                    <!-- TELEFONO -->
                    <div class="mb-3">
                        <label class="form-label">Teléfono</label>
                        <input type="text" name="telefono" class="form-control"
                               value="${paciente.telefono}">
                    </div>

                    <!-- EMAIL -->
                    <div class="mb-3">
                        <label class="form-label">Email</label>
                        <input type="email" name="email" class="form-control"
                               value="${paciente.email}">
                    </div>

                    <!-- EPS -->
                    <div class="mb-3">
                        <label class="form-label">EPS</label>
                        <input type="text" name="eps" class="form-control"
                               value="${paciente.eps}">
                    </div>

                    <!-- VEREDA / BARRIO -->
                    <div class="mb-3">
                        <label class="form-label">Vereda / Barrio</label>
                        <input type="text" name="veredaBarrio" class="form-control"
                               value="${paciente.veredaBarrio}">
                    </div>

                    <!-- BOTONES -->
                    <div class="d-flex gap-2">

                        <button type="submit" class="btn btn-success w-100">
                            <i class="fas fa-save me-2"></i>
                            <fmt:message key="guardar"/>
                        </button>

                        <a href="${pageContext.request.contextPath}/pacientes"
                           class="btn btn-secondary w-100">
                            <fmt:message key="volver"/>
                        </a>

                    </div>

                </form>

            </div>
        </div>

    </div>
</div>

<%@ include file="/views/templates/footer.jsp" %>