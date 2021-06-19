package com.es.phoneshop.web;

import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.product.cart.Cart;
import com.es.phoneshop.model.product.cart.CartService;
import com.es.phoneshop.model.product.cart.OutOfStockException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Locale;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CartPageServletTest {
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private RequestDispatcher requestDispatcher;
    @Mock
    private ServletConfig config;
    @Mock
    private CartService cartService;


    private CartPageServlet servlet = new CartPageServlet();

    @Before
    public void setup() throws ServletException {
        servlet.init(config);
        servlet.setCartService(cartService);

        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
        when(request.getPathInfo()).thenReturn("\\1");
        when(cartService.getCart(request.getSession())).thenReturn(new Cart());
        when(request.getLocale()).thenReturn(new Locale("en_US"));
    }

    @Test
    public void testDoGet() throws ServletException, IOException {
        servlet.doGet(request, response);

        verify(requestDispatcher).forward(request, response);
        verify(request).setAttribute(eq("cart"), any());
    }

    @Test
    public void testDoPost() throws ServletException, IOException {
        when(request.getParameterValues("quantity")).thenReturn(new String[]{"1"});
        when(request.getParameterValues("productId")).thenReturn(new String[]{"1"});

        servlet.doPost(request, response);

        verify(response).sendRedirect(request.getContextPath() + "/cart?message=Cart updated successfully.");
    }

    @Test
    public void testDoPostParseException() throws ServletException, IOException {
        when(request.getParameterValues("quantity")).thenReturn(new String[]{"q"});
        when(request.getParameterValues("productId")).thenReturn(new String[]{"1"});

        servlet.doPost(request, response);

        verify(request).setAttribute(eq("errors"), any());
    }

    @Test
    public void testDoPostOutOfStockException() throws ServletException, IOException, OutOfStockException {
        int stockRequested = 5;
        int stockAvailable = 0;

        when(request.getParameterValues("quantity")).thenReturn(new String[]{"1"});
        when(request.getParameterValues("productId")).thenReturn(new String[]{"1"});
        doThrow(new OutOfStockException(new Product(), stockRequested, stockAvailable)).when(cartService).updateCart(any(), anyLong(), anyInt());

        servlet.doPost(request, response);

        verify(request).setAttribute(eq("errors"), any());

    }
}

