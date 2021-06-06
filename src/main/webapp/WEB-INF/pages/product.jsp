<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="product" type="com.es.phoneshop.model.product.Product" scope="request"/>
<tags:master pageTitle="Product">
  <p>
    ${product.description}
  </p>
  <form>
      <input name="query" value="${param.query}">
      <button>Search</button>
    </form>
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
            <td>
              <fmt:formatNumber value="${product.price}" type="currency" currencySymbol="${product.currency.symbol}"/>
            </td>
          </tr>
          <tr>
            <td>stock</td>
            <td>
              ${product.stock}
            </td>
        </tr>
    </table>
</tags:master>