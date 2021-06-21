<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="order" type="com.es.phoneshop.model.product.order.Order" scope="request"/>
<tags:master pageTitle="Checkout">
<c:if test="${not empty order.items}">
   <c:if test="${not empty param.message}">
       <div class="success">
           ${param.message}
       </div>
   </c:if>
   <c:if test="${not empty errors}">
       <div class="error">
            There was an error while placing an order.
       </div>
   </c:if>
<form method="post" action="${pageContext.servletContext.contextPath}/checkout">
 <p>
 </p>
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
    <tr>
        <td>First name<span style="color: red">*</span></td>
        <td>
            <c:set var="error" value="${errors['firstName']}"/>
            <input name="firstName" value="${not empty error ? param['firstName'] : order.firstName}">
            <c:if test="${not empty error}">
                <div class="error">
                    ${error}
                </div>
            </c:if>
        </td>
    </tr>
    <tr>
        <td>Last name<span style="color: red">*</span></td>
        <td>
        <c:set var="error" value="${errors['lastName']}"/>
        <input name="lastName" value="${not empty error ? param['lastName'] : order.lastName}">
        <c:if test="${not empty error}">
            <div class="error">
                  ${error}
            </div>
        </c:if>
        </td>
    </tr>
    <tr>
        <td>Phone<span style="color: red">*</span></td>
        <td>
        <c:set var="error" value="${errors['phone']}"/>
        <input name="phone" value="${not empty error ? param['phone'] : order.phone}">
        <c:if test="${not empty error}">
            <div class="error">
                  ${error}
            </div>
        </c:if>
        </td>
    </tr>
    <tr>
        <td>Delivery date<span style="color: red">*</span></td>
        <td>
        <c:set var="error" value="${errors['deliveryDate']}"/>
        <input name="deliveryDate" value="${not empty error ? param['deliveryDate'] : order.deliveryDate}">
        <c:if test="${not empty error}">
            <div class="error">
                  ${error}
            </div>
        </c:if>
        </td>
    </tr>
    <tr>
        <td>Delivery address<span style="color: red">*</span></td>
        <td>
        <c:set var="error" value="${errors['deliveryAddress']}"/>
        <input name="deliveryAddress" value="${not empty error ? param['deliveryAddress'] : order.deliveryAddress}">
        <c:if test="${not empty error}">
            <div class="error">
                  ${error}
            </div>
        </c:if>
        </td>
    </tr>
    <tr>
        <td>Payment method<span style="color: red">*</span></td>
        <td>
        <c:set var="error" value="${errors['paymentMethod']}"/>
        <select name="paymentMethod">
            <option></option>
            <c:forEach var="paymentMethod" items="${paymentMethods}">
                <option>${paymentMethod}</option>
            </c:forEach>
        </select>
        <c:if test="${not empty error}">
            <div class="error">
                  ${error}
            </div>
        </c:if>
        </td>
    </tr>
  </table>
  <p>
    <button>Place order</button>
  </p>
  </form>
   </c:if>
    <c:if test="${empty order.items}">
      <h1 id="cartIsEmpty">
        No items in order.
      </h1>
    </c:if>
</tags:master>
