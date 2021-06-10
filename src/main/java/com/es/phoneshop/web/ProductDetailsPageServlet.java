package com.es.phoneshop.web;

import com.es.phoneshop.model.product.*;
import com.es.phoneshop.model.product.cart.Cart;
import com.es.phoneshop.model.product.cart.CartService;
import com.es.phoneshop.model.product.cart.DefaultCartService;
import com.es.phoneshop.model.product.cart.OutOfStockException;
import com.es.phoneshop.model.product.recentlyViewed.DefaultRecentlyViewedService;
import com.es.phoneshop.model.product.recentlyViewed.RecentlyViewedList;
import com.es.phoneshop.model.product.recentlyViewed.RecentlyViewedService;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;

public class ProductDetailsPageServlet extends HttpServlet {
    private ProductDao productDao;
    private CartService cartService;
    private RecentlyViewedService recentlyViewedService;
    private final String ERROR = "error";

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        productDao = ArrayListProductDao.getInstance();
        cartService = DefaultCartService.getInstance();
        recentlyViewedService = DefaultRecentlyViewedService.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            Long productId = parseProductId(request);
            RecentlyViewedList recentlyViewedList = recentlyViewedService.getRecentlyViewedList(request);
            recentlyViewedService.add(recentlyViewedList, productId);

            request.setAttribute("product", productDao.getProduct(productId));
            request.setAttribute("cart", cartService.getCart(request));
            request.setAttribute("viewHistory", recentlyViewedService.getRecentlyViewedList(request));
        } catch (ProductNotFoundException | NumberFormatException ex) {
            request.setAttribute("message", "Product not found.");
            response.sendError(404);
        }
        request.getRequestDispatcher("/WEB-INF/pages/product.jsp").forward(request, response);
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

        Cart cart = cartService.getCart(request);
        try {
            cartService.add(cart, productId, quantity);
        } catch (OutOfStockException e) {
            request.setAttribute(ERROR, "Out of stock, available " + e.getStockAvailable());
            doGet(request, response);
            return;
        }
        response.sendRedirect(request.getContextPath() + "/products/" + productId + "?message=Product added to cart");
    }

    private Long parseProductId(HttpServletRequest request) {
        String productInfo = request.getPathInfo().substring(1);
        return Long.valueOf(productInfo);
    }

    public void setProductDao(ProductDao productDao){
        this.productDao = productDao;
    }
}
