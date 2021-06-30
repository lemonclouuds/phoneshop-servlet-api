package com.es.phoneshop.web;

import com.es.phoneshop.model.product.ArrayListProductDao;
import com.es.phoneshop.model.product.ProductDao;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;

public class AdvancedSearchPageServlet extends HttpServlet {
    private ProductDao productDao;

    protected static final String CHECKOUT_JSP = "/WEB-INF/pages/advancedSearch.jsp";

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        productDao = ArrayListProductDao.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String descriptionStr = request.getParameter("description");
        String wordOptStr = request.getParameter("wordOptions");
        String minPriceStr = request.getParameter("minPrice");
        String maxPriceStr = request.getParameter("maxPrice");

        BigDecimal minPrice = null;
        BigDecimal maxPrice = null;

        try {
            if (!isPriceStrCorrect(minPriceStr)) {
                minPrice = new BigDecimal(Integer.parseInt(minPriceStr));
            }
            if (!isPriceStrCorrect(maxPriceStr)) {
                maxPrice = new BigDecimal(Integer.parseInt(maxPriceStr));
            }
        } catch (NumberFormatException e) {
            request.setAttribute("errors", "Price should be a number");
        }

        request.setAttribute("products", productDao.findProducts(descriptionStr, wordOptStr, minPrice, maxPrice));
        request.getRequestDispatcher(CHECKOUT_JSP).forward(request, response);
    }

    boolean isPriceStrCorrect(String price) {
        return price != null && !price.trim().isEmpty();
    }
}
