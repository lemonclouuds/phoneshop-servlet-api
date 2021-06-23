package com.es.phoneshop.web;

import com.es.phoneshop.model.product.cart.Cart;
import com.es.phoneshop.model.product.cart.CartService;
import com.es.phoneshop.model.product.cart.DefaultCartService;
import com.es.phoneshop.model.product.order.DefaultOrderService;
import com.es.phoneshop.model.product.order.Order;
import com.es.phoneshop.model.product.order.OrderService;
import com.es.phoneshop.model.product.order.PaymentMethod;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class CheckoutPageServlet extends HttpServlet {
    private CartService cartService;
    private OrderService orderService;

    protected static final String CHECKOUT_JSP = "/WEB-INF/pages/checkout.jsp";
    protected static final String DATE_PARSING_PATTERN = "yyyy-MM-dd";
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        cartService = DefaultCartService.getInstance();
        orderService = DefaultOrderService.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Cart cart = cartService.getCart(request.getSession());
        request.setAttribute("order", orderService.getOrder(cart));
        request.setAttribute("paymentMethods", orderService.getPaymentMethods());
        request.getRequestDispatcher(CHECKOUT_JSP).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Cart cart = cartService.getCart(request.getSession());
        Order order =  orderService.getOrder(cart);

        Map<String, String> errors =  new HashMap<>();

        setRequiredParameter(request, "firstName", errors, order::setFirstName);
        setRequiredParameter(request, "lastName", errors, order::setLastName);
        setPhone(request, errors, order);
        setDeliveryDate(request, errors, DATE_PARSING_PATTERN, order);
        setRequiredParameter(request, "deliveryAddress", errors, order::setDeliveryAddress);
        setPaymentMethod(request, errors, order);

        handleErrors(request, response, errors, order);
    }

    private void handleErrors(HttpServletRequest request, HttpServletResponse response, Map<String, String> errors,
                              Order order) throws IOException, ServletException {
        if (errors.isEmpty()) {
            orderService.placeOrder(order);
            cartService.clearCart(cartService.getCart(request.getSession()));
            response.sendRedirect(request.getContextPath() + "/order/overview/" + order.getSecureId());
        } else {
            request.setAttribute("errors", errors);
            request.setAttribute("order", order);
            request.setAttribute("paymentMethods", orderService.getPaymentMethods());
            request.getRequestDispatcher(CHECKOUT_JSP).forward(request, response);
        }
    }

    public void setCartService(CartService cartService){
        this.cartService = cartService;
    }

    private void setRequiredParameter(HttpServletRequest request, String parameter, Map<String, String> errors,
                                      Consumer<String> consumer) {
        String value = request.getParameter(parameter);
        if (value == null || value.isEmpty()) {
            errors.put(parameter, "Value is required");
        } else {
            consumer.accept(value);
        }
    }

    private void setPhone(HttpServletRequest request, Map<String, String> errors, Order order) {
        String value = request.getParameter("phone");
        if (value == null || value.isEmpty()) {
            errors.put("phone", "Value is required");
        } else {
            if (!orderService.isPhoneCorrect(value)) {
                errors.put("phone", "Phone number incorrect");
            }
            order.setPhone(value);
        }
    }

    private void setPaymentMethod(HttpServletRequest request, Map<String, String> errors, Order order) {
        String value = request.getParameter("paymentMethod");
        if (value == null || value.isEmpty()) {
            errors.put("paymentMethod", "Value is required");
        } else {
            order.setPaymentMethod(PaymentMethod.valueOf(value));
        }
    }

    private void setDeliveryDate(HttpServletRequest request, Map<String, String> errors, String pattern, Order order) {
        String value = request.getParameter("deliveryDate");
        if (value == null || value.isEmpty()) {
            errors.put("deliveryDate", "Value is required");
        } else {
            LocalDate requestedDate = LocalDate.parse(value, DateTimeFormatter.ofPattern(pattern));
            if (requestedDate.isBefore(LocalDate.now())) {
                errors.put("deliveryDate", "Delivery date incorrect");
            } else {
                order.setDeliveryDate(LocalDate.parse(value, DateTimeFormatter.ofPattern(pattern)));
            }
        }
    }
}
