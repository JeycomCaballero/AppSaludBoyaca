<%@ page contentType="text/html;charset=UTF-8" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<fmt:setLocale value="${sessionScope.lang != null ? sessionScope.lang : 'es'}"/>
<fmt:setBundle basename="messages"/>

<%@ include file="/views/templates/header.jsp" %>

<h3 class="mb-4">
    <i class="fas fa-calendar-check me-2"></i>
    <fmt:message key="cita.lista.titulo"/>
</h3>

<!-- BOTÓN NUEVO -->
<c:if test="${sessionScope.usuario.rol == 'RECEPCIONISTA'}">
    <form action="${pageContext.request.contextPath}/citas" method="post">
        <button type="submit" name="action" value="nuevo" class="btn btn-success">
            <i class="fas fa-plus me-1"></i>
            <fmt:message key="cita.nueva"/>
        </button>
    </form>
</c:if>
<!-- DESCARGAR PDF -->
<form action="${pageContext.request.contextPath}/citas" method="post" class="mt-2">
    <button type="submit"
            name="action"
            value="pdfHoy"
            class="btn btn-danger">

        <i class="fas fa-file-pdf me-1"></i>
        Descargar Citas HOY
    </button>
</form>

<div class="card shadow-sm">

    <div class="card-body">

        <div class="table-responsive">

            <table class="table table-hover align-middle">

                <thead class="table-light">
                    <tr>
                        <th>ID</th>
                        <th><fmt:message key="tabla.paciente"/></th>
                        <th><fmt:message key="cita.fecha"/></th>
                        <th><fmt:message key="tabla.hora"/></th>
                        <th><fmt:message key="tabla.especialidad"/></th>
                        <th><fmt:message key="tabla.estado"/></th>
                        <th class="text-center"><fmt:message key="acciones"/></th>
                    </tr>
                </thead>

                <tbody>

                    <c:forEach var="cita" items="${listaCitas}">
                        <tr>

                            <td>${cita.id}</td>

                            <td>${cita.paciente.nombres} ${cita.paciente.apellidos}</td>

                            <td>
                                <fmt:formatDate value="${cita.fechaCita}" pattern="dd/MM/yyyy"/>
                            </td>

                            <td>${cita.horaCita}</td>

                            <td>${cita.especialidad.nombre}</td>

                            <td>
                                <span class="badge 
                                      ${cita.estado == 'PROGRAMADA' ? 'bg-primary' :
                                        cita.estado == 'CONFIRMADA' ? 'bg-info' :
                                        cita.estado == 'ATENDIDA' ? 'bg-success' :
                                        cita.estado == 'CANCELADA' ? 'bg-danger' :
                                        'bg-secondary'}">

                                    <c:choose>
                                        <c:when test="${cita.estado == 'PROGRAMADA'}">
                                            <fmt:message key="estado.programada"/>
                                        </c:when>
                                        <c:when test="${cita.estado == 'CONFIRMADA'}">
                                            <fmt:message key="estado.confirmada"/>
                                        </c:when>
                                        <c:when test="${cita.estado == 'ATENDIDA'}">
                                            <fmt:message key="estado.atendida"/>
                                        </c:when>
                                        <c:when test="${cita.estado == 'CANCELADA'}">
                                            <fmt:message key="estado.cancelada"/>
                                        </c:when>
                                        <c:otherwise>
                                            ${cita.estado}
                                        </c:otherwise>
                                    </c:choose>

                                </span>
                            </td>

                            <td class="text-center d-flex justify-content-center gap-2">

                                <!-- VER -->
                                <form action="${pageContext.request.contextPath}/citas" method="post">
                                    <input type="hidden" name="id" value="${cita.id}">
                                    <button type="submit" name="action" value="ver" class="btn btn-info btn-sm">
                                        <i class="fas fa-eye"></i>
                                    </button>
                                </form>

                                <!-- RECEPCIONISTA -->
                                <c:if test="${sessionScope.usuario.rol == 'RECEPCIONISTA'}">

                                    <form action="${pageContext.request.contextPath}/citas" method="post">
                                        <input type="hidden" name="id" value="${cita.id}">
                                        <button type="submit" name="action" value="editar" class="btn btn-warning btn-sm">
                                            <i class="fas fa-edit"></i>
                                        </button>
                                    </form>



                                </c:if>

                                <!-- MÉDICO -->
                                <c:if test="${sessionScope.usuario.rol == 'MEDICO'}">

                                    <!-- SI ESTÁ PROGRAMADA -->
                                    <c:if test="${cita.estado == 'PROGRAMADA'}">

                                        <!-- CONFIRMAR -->
                                        <form action="${pageContext.request.contextPath}/citas" method="post">
                                            <input type="hidden" name="id" value="${cita.id}">
                                            <input type="hidden" name="estado" value="CONFIRMADA">

                                            <button type="submit"
                                                    name="action"
                                                    value="estado"
                                                    class="btn btn-primary btn-sm">

                                                <fmt:message key="estado.confirmar"/>
                                            </button>
                                        </form>

                                        <!-- CANCELAR -->
                                        <form action="${pageContext.request.contextPath}/citas" method="post">
                                            <input type="hidden" name="id" value="${cita.id}">
                                            <input type="hidden" name="estado" value="CANCELADA">

                                            <button type="submit"
                                                    name="action"
                                                    value="estado"
                                                    class="btn btn-secondary btn-sm">

                                                <fmt:message key="estado.cancelar"/>
                                            </button>
                                        </form>

                                    </c:if>

                                    <!-- SI ESTÁ CONFIRMADA -->
                                    <c:if test="${cita.estado == 'CONFIRMADA'}">

                                        <!-- MARCAR ATENDIDA -->
                                        <form action="${pageContext.request.contextPath}/citas" method="post">
                                            <input type="hidden" name="id" value="${cita.id}">
                                            <input type="hidden" name="estado" value="ATENDIDA">

                                            <button type="submit"
                                                    name="action"
                                                    value="estado"
                                                    class="btn btn-success btn-sm">

                                                <fmt:message key="estado.atendida"/>
                                            </button>
                                        </form>

                                        <!-- CANCELAR -->
                                        <form action="${pageContext.request.contextPath}/citas" method="post">
                                            <input type="hidden" name="id" value="${cita.id}">
                                            <input type="hidden" name="estado" value="CANCELADA">

                                            <button type="submit"
                                                    name="action"
                                                    value="estado"
                                                    class="btn btn-secondary btn-sm">

                                                <fmt:message key="estado.cancelar"/>
                                            </button>
                                        </form>

                                    </c:if>

                                    <!-- SI ESTÁ ATENDIDA -->
                                    <c:if test="${cita.estado == 'ATENDIDA'}">

                                        <span class="badge bg-success">
                                            <fmt:message key="estado.atendida"/>
                                        </span>

                                    </c:if>

                                    <!-- SI ESTÁ CANCELADA -->
                                    <c:if test="${cita.estado == 'CANCELADA'}">

                                        <span class="badge bg-danger">
                                            <fmt:message key="estado.cancelada"/>
                                        </span>

                                    </c:if>

                                </c:if>

                            </td>

                        </tr>
                    </c:forEach>

                </tbody>

            </table>

        </div>

    </div>

</div>

<%@ include file="/views/templates/footer.jsp" %>
<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
<script>

    <%
        String msg = (String) request.getAttribute("msg");

        if (msg == "Se creo correctamente" && msg != null) {%>
    Swal.fire({
        icon: 'success',
        title: '<%= msg%>',
        confirmButtonColor: '#1c5fa8',
        background: '#1e293b',
        color: 'white',
        timer: 1500,
        showConfirmButton: false
    }).then(() => {
        window.location.href = "citas";
    });
    <% } %>

    <%
        if (msg == "Se actualizo correctamente" && msg != null) {%>
    Swal.fire({
        icon: 'success',
        title: '<%= msg%>',
        confirmButtonColor: '#1c5fa8',
        background: '#1e293b',
        color: 'white',
        timer: 1500,
        showConfirmButton: false
    }).then(() => {
        window.location.href = "citas";
    });
    <% } %>
    <%
        if (msg == "Se elimino correctamente" && msg != null) {%>
    Swal.fire({
        icon: 'success',
        title: '<%= msg%>',
        confirmButtonColor: '#1c5fa8',
        background: '#1e293b',
        color: 'white',
        timer: 1500,
        showConfirmButton: false
    }).then(() => {
        window.location.href = "citas";
    });
    <% } %>
    <%
        if (msg == "Se cambio correctamente" && msg != null) {%>
    Swal.fire({
        icon: 'success',
        title: '<%= msg%>',
        confirmButtonColor: '#1c5fa8',
        background: '#1e293b',
        color: 'white',
        timer: 1500,
        showConfirmButton: false
    }).then(() => {
        window.location.href = "citas";
    });
    <% } %>
    <%
        if (msg == "No se puede agendar la cita" && msg != null) {%>
    Swal.fire({
        icon: 'error',
        title: '<%= msg%>',
        confirmButtonColor: '#1c5fa8',
        background: '#1e293b',
        color: 'white',
        timer: 1500,
        showConfirmButton: false
    }).then(() => {
        window.location.href = "citas";
    });
    <% }%>
</script>