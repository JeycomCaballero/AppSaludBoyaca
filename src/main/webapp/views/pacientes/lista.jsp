<%@ page contentType="text/html;charset=UTF-8" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<fmt:setLocale value="${sessionScope.lang != null ? sessionScope.lang : 'es'}"/>
<fmt:setBundle basename="messages"/>

<%@ include file="/views/templates/header.jsp" %>

<h3 class="mb-4">
    <i class="fas fa-hospital-user me-2"></i>
    <fmt:message key="paciente.lista.titulo"/>
</h3>
<c:if test="${sessionScope.usuario.rol == 'RECEPCIONISTA'}">
<!-- BOTÓN NUEVO -->
<form action="${pageContext.request.contextPath}/pacientes" method="post">
    <button type="submit" name="action" value="nuevo" class="btn btn-success mb-3">
        <i class="fas fa-plus me-1"></i>
        <fmt:message key="paciente.nuevo"/>
    </button>
</form>
</c:if>
<div class="card shadow-sm">
    <div class="card-body">

        <div class="table-responsive">

            <table class="table table-hover align-middle">

                <thead class="table-light">
                    <tr>
                        <th>ID</th>
                        <th><fmt:message key="paciente.nombre"/></th>
                        <th><fmt:message key="paciente.documento"/></th>
                        <th><fmt:message key="paciente.fecha"/></th>
                        <th class="text-center">Acciones</th>
                    </tr>
                </thead>

                <tbody>

                    <c:forEach var="p" items="${pacientes}">
                        <tr>
                            <td>${p.id}</td>
                            <td>${p.nombres} ${p.apellidos}</td>
                            <td>${p.documento}</td>

                            <td>
                                <fmt:formatDate value="${p.fechaNacimiento}" pattern="dd/MM/yyyy"/>
                            </td>

                            <td class="text-center d-flex justify-content-center gap-2">
                                <c:if test="${sessionScope.usuario.rol == 'RECEPCIONISTA'}">
                                    <!-- EDITAR -->
                                    <form action="${pageContext.request.contextPath}/pacientes" method="post">
                                        <input type="hidden" name="id" value="${p.id}">
                                        <button type="submit" name="action" value="editar"
                                                class="btn btn-warning btn-sm">
                                            <i class="fas fa-edit"></i>
                                        </button>
                                    </form>
                                </c:if>
                                <c:if test="${sessionScope.usuario.rol == 'RECEPCIONISTA'}">
                                    <!-- ELIMINAR -->
                                    <form action="${pageContext.request.contextPath}/pacientes" method="post"
                                          onsubmit="return confirm('<fmt:message key="paciente.confirmar"/>');">

                                        <input type="hidden" name="id" value="${p.id}">

                                        <button type="submit" name="action" value="eliminar"
                                                class="btn btn-danger btn-sm">
                                            <i class="fas fa-trash"></i>
                                        </button>
                                    </form>
                                </c:if>
                            </td>
                        </tr>
                    </c:forEach>

                    <c:if test="${empty pacientes}">
                        <tr>
                            <td colspan="5" class="text-center text-muted">
                                <fmt:message key="dashboard.no.citas"/>
                            </td>
                        </tr>
                    </c:if>

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
                                      window.location.href = "pacientes";
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
                                      window.location.href = "pacientes";
                                  });
    <% } %>
    <%
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
                                      window.location.href = "pacientes";
                                  });
    <% }%>

</script>