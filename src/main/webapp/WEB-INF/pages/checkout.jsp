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
     <tags:orderFormRow name="firstName" label="First name" order="${order}" errors="${errors}"/>
     <tags:orderFormRow name="lastName" label="Last name" order="${order}" errors="${errors}"/>
     <tags:orderFormRow name="phone" label="Phone" order="${order}" errors="${errors}"/>
     <tags:orderFormRow name="deliveryDate" label="Delivery date" order="${order}" errors="${errors}"/>
     <tags:orderFormRow name="deliveryAddress" label="Delivery address" order="${order}" errors="${errors}"/>
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
