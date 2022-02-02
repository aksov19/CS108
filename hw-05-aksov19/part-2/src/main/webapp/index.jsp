<%@ page import = "java.util.*" %>
<%@ page import = "hw05.Product" %>
<%@ page import = "hw05.ProductListDAO" %>

<html>
    <head>
        <title>Student Store</title>
    </head>
    <body>
        <h1>Student Store</h1>

        <p>Items available:</p>

        <ul>
            <%
                ProductListDAO dao = (ProductListDAO) request.getServletContext().getAttribute("productDAO");
                List<Product> products = dao.getProducts();
                for(Product p : products){
                    out.println("<li><a href = \"/product-page.jsp?id=" + p.getId() + "\">" + p.getName() + "</a></li>");
                }
            %>
        </ul>

    </body>
</html>
