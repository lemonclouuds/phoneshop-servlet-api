<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="products" type="java.util.ArrayList" scope="request"/>
<tags:master pageTitle="Advanced search">
  <h2>
    Advanced search
  </h2>
  <form method="get" action="${pageContext.servletContext.contextPath}/advancedSearch">
    <div> Description
        <input name="description" value="${not empty param.description ? param.description : ""}">
        <select name="wordOptions">
            <option name="allWords">All words</option>
            <option name="anyWord">Any word</option>
        </select>
    </div>
    <div> Min price
        <input name="minPrice" value="${not empty param.minPrice ? param.minPrice : ""}">
    </div>
    <div> Max price
        <input name="maxPrice" value="${not empty param.maxPrice ? param.maxPrice : ""}">
    </div>
    <input type="submit" value="Search">
  </form>
  <c:if test="${not empty products}">
  <table>
    <thead>
      <tr>
        <td>Image</td>
        <td>Description</td>
        <td class="price">Price</td>
      </tr>
    </thead>
    <c:forEach var="product" items="${products}">
      <tr>
        <td>
          <img class="product-tile" src="${product.imageUrl}">
        </td>
        </a>
        <td>
            <a href="${pageContext.servletContext.contextPath}/products/${product.id}">
                ${product.description}
            </a>
        </td>
        <td class="price">
            <fmt:formatNumber value="${product.price}" type="currency" currencySymbol="${product.currency.symbol}"/>
        </td>
        </form>
      </tr>
    </c:forEach>
  </table>
 </c:if>
</tags:master>