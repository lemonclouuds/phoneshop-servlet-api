package com.es.phoneshop.web;

import com.es.phoneshop.model.product.ArrayListProductDao;
import com.es.phoneshop.model.product.ProductDao;
import com.es.phoneshop.model.product.SortField;
import com.es.phoneshop.model.product.SortOrder;
import com.es.phoneshop.model.product.cart.Cart;
import com.es.phoneshop.model.product.cart.CartService;
import com.es.phoneshop.model.product.cart.DefaultCartService;
import com.es.phoneshop.model.product.cart.OutOfStockException;
import com.es.phoneshop.model.product.recentlyViewed.DefaultRecentlyViewedService;
import com.es.phoneshop.model.product.recentlyViewed.RecentlyViewedService;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Optional;

public class ProductListPageServlet extends HttpServlet {
    private ProductDao productDao;
    private RecentlyViewedService recentlyViewedService;
    private CartService cartService;

    private final String ERROR = "error";

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        productDao = ArrayListProductDao.getInstance();
        recentlyViewedService = DefaultRecentlyViewedService.getInstance();
        cartService = DefaultCartService.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String query = request.getParameter("query");
        String sortField = request.getParameter("sort");
        String sortOrder = request.getParameter("order");
        request.setAttribute("products", productDao.findProducts(
                                                        query,
                                                        Optional.ofNullable(sortField).map(name -> SortField.valueOf(name.toUpperCase())).orElse(null),
                                                        Optional.ofNullable(sortOrder).map(name1 -> SortOrder.valueOf(name1.toUpperCase())).orElse(null)));
        request.setAttribute("recentlyViewed", recentlyViewedService.getRecentlyViewedList(request));
        request.getRequestDispatcher("/WEB-INF/pages/productList.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Long productId = parseProductId(request);
        String quantityString = request.getParameter("quantity");
        int quantity;
        try {
            NumberFormat format = NumberFormat.getInstance(request.getLocale());
            quantity = format.parse(quantityString).intValue();
        } catch (ParseException e) {
            request.setAttribute(ERROR, "Not a number");
            doGet(request, response);
            return;
        }

        Cart cart = cartService.getCart(request.getSession());
        try {
            cartService.addProductToCart(cart, productId, quantity);
        } catch (OutOfStockException e) {
            request.setAttribute(ERROR, "Out of stock, available " + e.getStockAvailable());
            doGet(request, response);
            return;
        } catch (IllegalArgumentException e) {
            request.setAttribute(ERROR, e.getMessage());
            doGet(request, response);
            return;
        }
        response.sendRedirect(request.getContextPath() + "/products?message=Product added to cart&productId" + productId);
    }

    private Long parseProductId(HttpServletRequest request) {
        String productInfo = request.getPathInfo().substring(1);
        return Long.valueOf(productInfo);
    }
}