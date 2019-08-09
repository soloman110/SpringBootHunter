<%@ page pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <h1>Customer List</h1>
    <c:if test="${customers == null}">
        <div>
            Table is empty.
        </div>
    </c:if>
    <c:if test="${customers != null}">
        <div>
            <table>
                <thead>
                    <tr>
                        <th>Name</th>
                        <th>Age</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach items="${customers}" var="c">
                        <tr>
                            <td>${c.name}</td>
                            <td>${c.age}</td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
    </c:if>
