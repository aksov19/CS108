<%@ page import = "java.util.*" %>
<%@ page import = "hw05.Product" %>
<%@ page import = "hw05.ProductListDAO" %>

<html>
    <head>
        <%  ProductListDAO dao = (ProductListDAO) request.getServletContext().getAttribute("productDAO");
            Product p = dao.getProductById( (String) request.getParameter("id") ); %>

        <title><%= p.getName() %></title>
    </head>
    <body>
        <h1><%= p.getName() %></h1>

        <img src=<%= p.getImageFile() %> alt="bruh">
        <p><%= "$" + p.getPrice() %></p>

        <form action="/Shopping-Cart" method="post">
            <input name="productId" type="hidden" value= <%= p.getId() %> />
            <input name="action" type="hidden" value="add" />
            <input type="submit" value="Add to Cart" />
        </form>

    </body>
</html>
