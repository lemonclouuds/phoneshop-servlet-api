<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="order" type="com.es.phoneshop.model.product.order.Order" scope="request"/>
<tags:master pageTitle="Order Overview">
<c:if test="${not empty order.items}">
 <h1>
    Order overview
 </h1>
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
    <c:forEach var="item" items="${order.items}">
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
            ${item.quantity}
        </td>
        <td class="price">
            <a href="${pageContext.servletContext.contextPath}/products/priceHistory/${item.product.id}">
                <fmt:formatNumber value="${item.product.price}" type="currency" currencySymbol="${item.product.currency.symbol}"/>
            </a>
        </td>
      </tr>
    </c:forEach>
    <tr class="price">
        <td></td>
        <td></td>
        <td>Subtotal:</td>
        <td>
        <fmt:formatNumber value="${order.subtotal}" type="currency" currencySymbol="${not empty order.items ? order.items.get(0).product.currency.symbol : ''}"/>
        </td>
    </tr>
    <tr class="price">
        <td></td>
        <td></td>
        <td>Delivery cost:</td>
        <td>
            <fmt:formatNumber value="${order.deliveryCost}" type="currency" currencySymbol="${not empty order.items ? order.items.get(0).product.currency.symbol : ''}"/>
        </td>
        </tr>
    <tr class="price">
        <td></td>
        <td></td>
        <td>Total cost:</td>
        <td>
            <fmt:formatNumber value="${order.totalCost}" type="currency" currencySymbol="${not empty order.items ? order.items.get(0).product.currency.symbol : ''}"/>
        </td>
    </tr>
  </table>
  <h2>Your details</h2>
  <table class="customerDetails">
    <tags:orderOverview name="firstName" label="Firs tname" order="${order}"/>
    <tags:orderOverview name="lastName" label="Last name" order="${order}"/>
    <tags:orderOverview name="phone" label="Phone" order="${order}"/>
    <tags:orderOverview name="deliveryDate" label="Delivery date" order="${order}"/>
    <tags:orderOverview name="deliveryAddress" label="Delivery address" order="${order}"/>
    <tr>
        <td>Payment method</td>
        <td>
            ${order.paymentMethod}
        </td>
    </tr>
  </table>
   </c:if>
    <c:if test="${empty order.items}">
      <h1 id="cartIsEmpty">
        No items in order.
      </h1>
    </c:if>
</tags:master>
