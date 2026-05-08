<%@ include file="templates/header.jsp" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<fmt:setLocale value="${sessionScope.lang != null ? sessionScope.lang : 'es'}"/>
<fmt:setBundle basename="messages"/>

<h3 class="mb-4">
    <fmt:message key="dashboard.bienvenida">
        <fmt:param value="${sessionScope.usuario.nombres}" />
    </fmt:message>
</h3>

<!-- TARJETAS -->
<div class="row g-3">

    <div class="col-md-3">
        <div class="card dashboard-card border-start border-primary border-4">
            <div class="card-body">
                <i class="fas fa-calendar-day float-end text-primary opacity-50"></i>
                <h5>${citasHoy}</h5>
                <small><fmt:message key="dashboard.citas.hoy"/></small>
            </div>
        </div>
    </div>

    <div class="col-md-3">
        <div class="card dashboard-card border-start border-warning border-4">
            <div class="card-body">
                <i class="fas fa-clock float-end text-warning opacity-50"></i>
                <h5>${citasPendientes}</h5>
                <small><fmt:message key="dashboard.citas.pendientes"/></small>
            </div>
        </div>
    </div>

    <div class="col-md-3">
        <div class="card dashboard-card border-start border-success border-4">
            <div class="card-body">
                <i class="fas fa-calendar-alt float-end text-success opacity-50"></i>
                <h5>${citasMes}</h5>
                <small><fmt:message key="dashboard.citas.mes"/></small>
            </div>
        </div>
    </div>

    <div class="col-md-3">
        <div class="card dashboard-card border-start border-info border-4">
            <div class="card-body">
                <i class="fas fa-users float-end text-info opacity-50"></i>
                <h5>${totalPacientes}</h5>
                <small><fmt:message key="dashboard.pacientes.total"/></small>
            </div>
        </div>
    </div>

</div>

<!-- TABLA -->
<div class="card mt-4 shadow-sm">

    <div class="card-header bg-white fw-bold">
        <i class="fas fa-list me-2"></i>
        <fmt:message key="dashboard.proximas.citas"/>
    </div>

    <div class="p-2">
        Total citas: ${listaCitas.size()}
    </div>

    <div class="table-responsive">
        <table class="table table-hover mb-0">

            <thead class="table-light">
                <tr>
                    <th><fmt:message key="tabla.hora"/></th>
                    <th><fmt:message key="tabla.paciente"/></th>
                    <th><fmt:message key="tabla.especialidad"/></th>
                    <th><fmt:message key="tabla.estado"/></th>
                </tr>
            </thead>

            <tbody>
                <c:choose>

                    <c:when test="${not empty listaCitas}">
                        <c:forEach var="c" items="${listaCitas}">
                            <tr>
                                <td>${c.horaCita}</td>
                                <td>${c.paciente.nombres} ${c.paciente.apellidos}</td>
                                <td>${c.especialidad.nombre}</td>
                                <td>
                                    <span class="badge 
                                          ${c.estado == 'PENDIENTE' ? 'bg-warning text-dark' : 
                                            c.estado == 'CONFIRMADA' ? 'bg-success' : 
                                            'bg-secondary'}">
                                              ${c.estado}
                                          </span>
                                    </td>
                                </tr>
                            </c:forEach>
                        </c:when>

                        <c:otherwise>
                            <tr>
                                <td colspan="4" class="text-center text-muted">
                                    No hay citas registradas
                                </td>
                            </tr>
                        </c:otherwise>

                    </c:choose>
                </tbody>

            </table>
        </div>

    </div>

    <%@ include file="templates/footer.jsp" %>
    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
    <script>

        <%
            String msg = (String) request.getAttribute("msg");

            if (msg == "Se cerro sesión correctamente" && msg != null) {%>
        Swal.fire({
            icon: 'success',
            title: '<%= msg%>',
            confirmButtonColor: '#1c5fa8',
            background: '#1e293b',
            color: 'white',
            timer: 1500,
            showConfirmButton: false
        }).then(() => {
            window.location.href = "login";
        });
        <% }%>
    </script>
