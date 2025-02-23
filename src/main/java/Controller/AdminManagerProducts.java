/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controller;

import DAO.CategoryDAO;
import DAO.OrdersDAO;
import DAO.ProductDAO;
import DAO.userDAO;
import Model.Category;
import Model.Orders;
import Model.Product;
import Model.User;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import java.io.File;
import java.util.List;

@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 2, // 2MB
        maxFileSize = 1024 * 1024 * 10, // 10MB
        maxRequestSize = 1024 * 1024 * 50 // 50MB
)
/**
 *
 * @author thaiv
 */
public class AdminManagerProducts extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try ( PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet AdminManagerProducts</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet AdminManagerProducts at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = (String) request.getParameter("action");
        if (action.equals("product")) {
            ProductDAO pDao = new ProductDAO();
            List<Product> productList = pDao.getProductList();
            request.setAttribute("productList", productList);
            request.getRequestDispatcher("ManageProductForAdmin.jsp").forward(request, response);
        } else if (action.equals("BackToAdminDashboard")) {
            request.getSession().removeAttribute("totalAmount");
            request.getSession().removeAttribute("countOrders");
            request.getSession().removeAttribute("count");
            request.getRequestDispatcher("adminDashboard.jsp").forward(request, response);
        } else if (action.equals("home")) {
            request.getRequestDispatcher("ViewProductListController").forward(request, response);
        } else if (action.equals("productForDashBoard")) {
            ProductDAO pDao = new ProductDAO();
            List<Product> productList = pDao.getProductList();
            request.setAttribute("productList", productList);
            request.getRequestDispatcher("adminDashboard.jsp?view=productTable").forward(request, response);
        } else if (action.equals("ordersForDashBoard")) {
            OrdersDAO ordersDao = new OrdersDAO();
            List<Orders> OrdersList = ordersDao.getAllOrders();
            request.setAttribute("OrdersList", OrdersList);
            request.getRequestDispatcher("adminDashboard.jsp?view=orderTable").forward(request, response);
        } else if (action.equals("count")) {
            ProductDAO productDao = new ProductDAO();
            List<Product> productList = productDao.getProductList();
            OrdersDAO ordersDao = new OrdersDAO();
            List<Orders> OrdersList = ordersDao.getAllOrders();
            OrdersDAO OrdersDAO = new OrdersDAO();
            userDAO userDao = new userDAO();
            List<User> userList = userDao.getAllUser();
            Double totalAmount = OrdersDAO.getTotalAmountOfDeliveredOrders();
            int countOrders = OrdersList.size();
            int count = productList.size();
            int countUser = userList.size();
            request.getSession().setAttribute("totalAmount", totalAmount);
            request.getSession().setAttribute("countOrders", countOrders);
            request.getSession().setAttribute("count", count);
            request.getSession().setAttribute("countUser", countUser);
            request.setAttribute("count", count);
            request.getRequestDispatcher("adminDashboard.jsp").forward(request, response);
        } else if (action.equals("order")) {
            OrdersDAO orderDao = new OrdersDAO();
            List<Orders> orderList = orderDao.getAllOrders();
            request.setAttribute("orderList", orderList);
            request.getRequestDispatcher("ManagerOrdersForAdmin.jsp").forward(request, response);
        } else if (action.equals("delete")) {
            ProductDAO productDao = new ProductDAO();
            String id_raw = request.getParameter("id");
            try {
                int id = Integer.parseInt(id_raw);
                productDao.deleteProduct(id);
                request.getRequestDispatcher("AdminManagerProducts?action=product").forward(request, response);
            } catch (NumberFormatException e) {
            }
        } else if (action.equals("addProduct")) {
            CategoryDAO categoryDAO = new CategoryDAO();
            List<Category> categoryList = categoryDAO.getAllCategory();
            request.setAttribute("categoryList", categoryList);
            request.getRequestDispatcher("ManageProductForAdminAddProductPage.jsp").forward(request, response);
        } else if (action.equals("editProduct")) {
            String productID_raw = request.getParameter("id");
            try {
                int productID = Integer.parseInt(productID_raw);
                ProductDAO productDao = new ProductDAO();
                Product product = productDao.getProductByID(productID);
                CategoryDAO categoryDAO = new CategoryDAO();
                List<Category> categoryList = categoryDAO.getAllCategory();
                request.setAttribute("product", product);
                request.setAttribute("categoryList", categoryList);
                request.getRequestDispatcher("ManageProductForAdminEditProductPage.jsp").forward(request, response);
            } catch (Exception e) {
            }
        } else if (action.equals("viewDelete")) {
            ProductDAO productDao = new ProductDAO();
            List<Product> productList = productDao.getDisabledProducts();
            request.setAttribute("productList", productList);
            request.getRequestDispatcher("ManageProductForAdminDeletedProductPage.jsp").forward(request, response);
        } else if (action.equals("restore")) {
            String productID_raw = request.getParameter("id");
            try {
                int productID = Integer.parseInt(productID_raw);
                ProductDAO productDao = new ProductDAO();
                productDao.restoreProduct(productID);
                List<Product> productList = productDao.getDisabledProducts();
                request.setAttribute("productList", productList);
                request.getRequestDispatcher("ManageProductForAdminDeletedProductPage.jsp").forward(request, response);
            } catch (Exception e) {
            }
        }

    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        try {
            if ("addProduct".equals(action)) {
                // Lấy dữ liệu từ form
                String productName = request.getParameter("productName");
                String priceStr = request.getParameter("price");
                String description = request.getParameter("description");
                String stockStr = request.getParameter("stock");
                String categoryIDStr = request.getParameter("categoryID");
                String size = request.getParameter("size");
                String ageRange = request.getParameter("ageRange");
                String origin = request.getParameter("origin");
                String weightStr = request.getParameter("weight");
                String isActiveStr = request.getParameter("isActive");
                // Xử lý file upload
                Part filePart = request.getPart("image");
                String fileName = filePart.getSubmittedFileName();
                String uploadPath = getServletContext().getRealPath("") + File.separator + "uploads";
                File uploadDir = new File(uploadPath);
                if (!uploadDir.exists()) {
                    uploadDir.mkdir();
                }
                String imagePath = "uploads" + File.separator + fileName;
                filePart.write(uploadPath + File.separator + fileName);

                // Convert và validate dữ liệu
                double price = (priceStr != null && !priceStr.trim().isEmpty()) ? Double.parseDouble(priceStr) : 0.0;
                int stock = (stockStr != null && !stockStr.trim().isEmpty()) ? Integer.parseInt(stockStr) : 0;
                int categoryID = (categoryIDStr != null && !categoryIDStr.trim().isEmpty()) ? Integer.parseInt(categoryIDStr) : 0;
                double weight = (weightStr != null && !weightStr.trim().isEmpty()) ? Double.parseDouble(weightStr) : 0.0;
                boolean isActive = (isActiveStr != null && !isActiveStr.trim().isEmpty()) ? Boolean.parseBoolean(isActiveStr) : false;

                // Tạo đối tượng Product
                ProductDAO productDao = new ProductDAO();
                Product product = new Product(productName, description, price, stock, imagePath, categoryID, isActive, size, ageRange, origin, weight);
                productDao.addProduct(product);

                response.sendRedirect("AdminManagerProducts?action=product");

            } else if ("editProduct".equals(action)) {
                // Lấy dữ liệu từ form
                String productIDStr = request.getParameter("productID");
                String productName = request.getParameter("productName");
                String priceStr = request.getParameter("price");
                String description = request.getParameter("description");
                String stockStr = request.getParameter("stock");
                String categoryIDStr = request.getParameter("categoryID");
                String size = request.getParameter("size");
                String ageRange = request.getParameter("ageRange");
                String origin = request.getParameter("origin");
                String weightStr = request.getParameter("weight");
                String isActiveStr = request.getParameter("isActive");

                // Xử lý file upload (nếu có ảnh mới)
                Part filePart = request.getPart("image");
                String imagePath;
                if (filePart != null && filePart.getSize() > 0) { // Nếu có file mới upload
                    String fileName = filePart.getSubmittedFileName();
                    String uploadPath = getServletContext().getRealPath("") + File.separator + "uploads";
                    File uploadDir = new File(uploadPath);
                    if (!uploadDir.exists()) {
                        uploadDir.mkdir();
                    }
                    imagePath = "uploads" + File.separator + fileName;
                    filePart.write(uploadPath + File.separator + fileName);
                } else { // Giữ ảnh cũ nếu không upload ảnh mới
                    imagePath = request.getParameter("existingImage"); // Giả sử form gửi hidden field chứa đường dẫn ảnh cũ
                }

                // Convert và validate dữ liệu
                int productID = Integer.parseInt(productIDStr);
                double price = (priceStr != null && !priceStr.trim().isEmpty()) ? Double.parseDouble(priceStr) : 0.0;
                int stock = (stockStr != null && !stockStr.trim().isEmpty()) ? Integer.parseInt(stockStr) : 0;
                int categoryID = (categoryIDStr != null && !categoryIDStr.trim().isEmpty()) ? Integer.parseInt(categoryIDStr) : 0;
                double weight = (weightStr != null && !weightStr.trim().isEmpty()) ? Double.parseDouble(weightStr) : 0.0;
                boolean isActive = (isActiveStr != null && !isActiveStr.trim().isEmpty()) ? Boolean.parseBoolean(isActiveStr) : false;

                // Tạo đối tượng Product
                ProductDAO productDao = new ProductDAO();
                Product product = new Product(productID, productName, description, price, stock, imagePath, categoryID, null, isActive, size, ageRange, origin, weight);
                productDao.updateProduct(product);

                response.sendRedirect("AdminManagerProducts?action=product");
            }
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Invalid input format. Please check your data.");
            request.getRequestDispatcher("ManageProductForAdminAddProductPage.jsp").forward(request, response);
        } catch (Exception e) {
            request.setAttribute("error", "An error occurred: " + e.getMessage());
            request.getRequestDispatcher("ManageProductForAdminAddProductPage.jsp").forward(request, response);
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
