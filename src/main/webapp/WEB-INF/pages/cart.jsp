<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="cart" type="com.es.phoneshop.model.product.cart.Cart" scope="request"/>
<tags:master pageTitle="Cart">
<form method="post" action="${pageContext.servletContext.contextPath}/cart">
 <c:if test="${not empty cart.items}">
  <c:if test="${not empty param.message}">
      <div class="success">
          ${param.message}
      </div>
  </c:if>
  <c:if test="${not empty errors}">
      <div class="error">
           There was an error updating cart.
      </div>
  </c:if>
  <table>
    <thead>
      <tr>
        <td>Image</td>
        <td>Description</td>
        <td class="quantity">Quantity</td>
        <td class="price">Price
        </td>
      </tr>
    </thead>
    <c:forEach var="item" items="${cart.items}">
      <tr>
        <td>
          <img class="product-tile" src="${item.product.imageUrl}">
        </td>
        </a>
        <td>
            <a href="${pageContext.servletContext.contextPath}/products/${item.product.id}">
                ${item.product.description}
            </a>
        </td>
        <td class="quantity">
            <fmt:formatNumber value="${item.quantity}" var="quantity"/>
            <c:set var="error" value="${errors[item.product.id]}"/>
            <input name="quantity" value="${not empty error ? paramValues['quantity'][status.index] : item.quantity}" class="quantity"/>
            <c:if test="${not empty error}">
                <div class="error">
                    ${errors[item.product.id]}
                </div>
            </c:if>
            <input name="productId" type="hidden" value="${item.product.id}"/>
        </td>
        <td class="price">
            <a href="${pageContext.servletContext.contextPath}/products/priceHistory/${item.product.id}">
                <fmt:formatNumber value="${item.product.price}" type="currency" currencySymbol="${item.product.currency.symbol}"/>
            </a>
        </td>
        <td>
            <button form="deleteCartItem"
                    formaction="${pageContext.servletContext.contextPath}/cart/deleteCartItem/${item.product.id}">Delete</button>
        </td>
      </tr>
    </c:forEach>
    <tr class="price">
        <td></td>
        <td></td>
        <td>Total cost</td>
        <td>
        <fmt:formatNumber value="${cart.totalCost}" type="currency" currencySymbol="${not empty cart.items ? cart.items.get(0).product.currency.symbol : ''}"/>
        </td>
    </tr>
  </table>
  <p>
  <button>Update</button>
  </p>
  </form>
  <form id="deleteCartItem" method="post"></form>
   </c:if>
    <c:if test="${empty cart.items}">
      <h1 id="cartIsEmpty">
        No items in cart.
      </h1>
    </c:if>
</tags:master>
