<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="product" type="com.es.phoneshop.model.product.Product" scope="request"/>
<tags:master pageTitle="Product">
  <p>
    Cart: ${cart}
  </p>
  <c:if test="${not empty message}">
    <div class="success">
        ${message}
    </div>
  </c:if>
  <c:if test="${not empty error}">
    <div class="error">
          There was an error adding to cart.
    </div>
    </c:if>
  <p>
      ${product.description}
  </p>
  <p>
    Cart
  </p>
  <form method="post">
    <table>
        <tr>
          <td>Image</td>
          <td>
            <img src="${product.imageUrl}">
          </td>
        </tr>
          <tr>
            <td>code</td>
            <td>
              ${product.code}
            </td>
          </tr>
          <tr>
            <td>price</td>
            <td class="price">
              <fmt:formatNumber value="${product.price}" type="currency" currencySymbol="${product.currency.symbol}"/>
            </td>
          </tr>
          <tr>
            <td>stock</td>
            <td>
              ${product.stock}
            </td>
          </tr>
          <tr>
            <td>stock</td>
            <td class="quantity">
                <input name="quantity" value="${not empty error ? param.quantity : 1}">
                <c:if test="${not empty error}">
                    <div class="error">
                    ${error}
                    </div>
                </c:if>
            </td>
          </tr>
    </table>
    <p>
        <button>Add to cart</button>
    </p>
  </form>
  <p>
  Recently viewed products
  </p>
    <tags:recentlyViewedProducts lastViewedProducts="${recentlyViewed.lastViewed}"/>
</tags:master>
