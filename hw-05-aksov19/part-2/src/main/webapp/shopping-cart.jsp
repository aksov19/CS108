<%@ page import = "java.util.*" %>
<%@ page import = "hw05.Product" %>
<%@ page import = "hw05.ProductListDAO" %>
<%@ page import = "hw05.ShoppingCart" %>

<html>
    <head>
        <% ShoppingCart cart = (ShoppingCart) request.getSession().getAttribute("cart");
           HashMap<String, Integer> items = cart.getItems();
           ProductListDAO dao = (ProductListDAO) request.getServletContext().getAttribute("productDAO");
           double totalCost = 0;
         %>

        <title>Shopping Cart</title>
    </head>
    <body>
        <h1>Shopping Cart</h1>

        <form action="/Shopping-Cart" method="post">
            <input name="action" type="hidden" value="update" />

            <ul>
                <%
                    for ( String id : items.keySet() ){
                        Product p = dao.getProductById( id );

                        out.println(
                        "<li>" +
                            "<p>" +
                                "<input type=\"text\" value=" + items.get(id) + " name=" + id + " />" +
                                " " + p.getName() + ", $" + p.getPrice() +
                            "</p>" +
                        "</li>"
                        );

                        totalCost += p.getPrice() * items.get(id);
                    }
                %>
            </ul>

            <p>Total: $<%= totalCost %></p>

            <input type="submit" value="Update Cart" />
        </form>

        <a href="index.jsp">Continue Shopping</a>

    </body>
</html>
