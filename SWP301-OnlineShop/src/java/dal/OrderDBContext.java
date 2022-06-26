/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Order;
import model.Product;
import model.Role;
import model.User;

/**
 *
 * @author hoan
 */
public class OrderDBContext extends DBContext {

    public ArrayList<Order> getUserOrders(int uid, String startDate, String endDate) {
        try {
            String sql = "WITH \n"
                    + "t as\n"
                    + "(SELECT [Order].id as OrderID, [Order].[date], [Order].totalPrice, [Product].[name] as ProductName, [Order].[status] as OrderStatus\n"
                    + "FROM [Order] inner join OrderDetail ON [Order].id = OrderDetail.orderId\n"
                    + "inner join Product ON OrderDetail.productId = Product.id\n"
                    + "WHERE [Order].userId = ?),\n"
                    + "b as\n"
                    + "(SELECT [Order].id as OrderID, COUNT(Product.id) as NumberOfProducts\n"
                    + "FROM [Order] inner join OrderDetail ON [Order].id = OrderDetail.orderId\n"
                    + "inner join Product ON OrderDetail.productId = Product.id\n"
                    + "WHERE [Order].userId = ? group by [Order].id),\n"
                    + "c as\n"
                    + "(\n"
                    + "SELECT  a.*\n"
                    + "FROM    (\n"
                    + "        SELECT  DISTINCT t.OrderID\n"
                    + "        FROM t\n"
                    + "        ) mo\n"
                    + "CROSS APPLY\n"
                    + "        (\n"
                    + "        SELECT  TOP 1 *\n"
                    + "        FROM    t mi\n"
                    + "        WHERE   mi.OrderID = mo.OrderID\n"
                    + "        ) a\n"
                    + "		)\n"
                    + "Select c.OrderID, c.[date], c.totalPrice, c.ProductName, c.OrderStatus, b.NumberOfProducts from c inner join b on c.OrderID = b.OrderID\n"
                    + "WHERE c.[date] between ? and ?";
            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setInt(1, uid);
            stm.setInt(2, uid);
            stm.setString(3, startDate);
            stm.setString(4, endDate);

            ResultSet rs = stm.executeQuery();
            ArrayList<Order> orders = new ArrayList<>();
            while (rs.next()) {
                Order o = new Order();
                o.setId(rs.getInt("OrderID"));
                o.setDate(rs.getDate("date"));
                o.setTotalcost(rs.getDouble("totalPrice"));
                o.setStatus(rs.getInt("OrderStatus"));
                o.setNumproducts(rs.getInt("NumberOfProducts"));

                ArrayList<Product> products = new ArrayList<>();
                Product p = new Product();
                p.setName(rs.getString("ProductName"));
                products.add(p);

                o.setProducts(products);
                orders.add(o);
            }
            return orders;
        } catch (SQLException ex) {
            Logger.getLogger(UserDBContext.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public ArrayList<Order> getOrders(String startDate, String endDate) {
        try {
            String sql = "WITH \n"
                    + "t as\n"
                    + "(SELECT [Order].id as OrderID,[User].fullname as CustomerName,[User].id as CustomerID, [Order].[date], [Order].totalPrice, [Product].[name] as ProductName, [Order].[status] as OrderStatus, [Order].sellerId\n"
                    + "FROM \n"
                    + "[User] inner join [Order] on [User].id = [Order].userId\n"
                    + "inner join OrderDetail ON [Order].id = OrderDetail.orderId\n"
                    + "inner join Product ON OrderDetail.productId = Product.id),\n"
                    + "b as\n"
                    + "(SELECT [Order].id as OrderID, COUNT(Product.id) as NumberOfProducts\n"
                    + "FROM [Order] inner join OrderDetail ON [Order].id = OrderDetail.orderId\n"
                    + "inner join Product ON OrderDetail.productId = Product.id\n"
                    + "group by [Order].id),\n"
                    + "c as\n"
                    + "(\n"
                    + "SELECT  a.*\n"
                    + "FROM    (\n"
                    + "        SELECT  DISTINCT t.OrderID\n"
                    + "        FROM t\n"
                    + "        ) mo\n"
                    + "CROSS APPLY\n"
                    + "        (\n"
                    + "        SELECT  TOP 1 *\n"
                    + "        FROM    t mi\n"
                    + "        WHERE   mi.OrderID = mo.OrderID\n"
                    + "        ) a\n"
                    + "		)\n"
                    + "Select c.OrderID, c.[date], c.totalPrice, c.CustomerID, c.CustomerName, c.ProductName, c.OrderStatus, b.NumberOfProducts, c.sellerId, [User].fullname as SellerName\n"
                    + "from c inner join b on c.OrderID = b.OrderID\n"
                    + "left outer join [User] on c.sellerId = [User].id\n"
                    + "\n"
                    + "WHERE c.[date] between ? and ?";
            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setString(1, startDate);
            stm.setString(2, endDate);

            ResultSet rs = stm.executeQuery();
            ArrayList<Order> orders = new ArrayList<>();
            while (rs.next()) {
                Order o = new Order();
                o.setId(rs.getInt("OrderID"));
                o.setDate(rs.getDate("date"));
                o.setTotalcost(rs.getDouble("totalPrice"));
                o.setStatus(rs.getInt("OrderStatus"));
                o.setNumproducts(rs.getInt("NumberOfProducts"));

                User buyer = new User();
                buyer.setId(rs.getInt("CustomerID"));
                buyer.setFullname(rs.getString("CustomerName"));

                User seller = new User();
                seller.setId(rs.getInt("sellerId"));
                seller.setFullname(rs.getString("SellerName"));

                o.setBuyer(buyer);
                o.setSale(seller);

                ArrayList<Product> products = new ArrayList<>();
                Product p = new Product();
                p.setName(rs.getString("ProductName"));
                products.add(p);

                o.setProducts(products);
                orders.add(o);
            }
            return orders;
        } catch (SQLException ex) {
            Logger.getLogger(UserDBContext.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public ArrayList<Order> getOrders(String startDate, String endDate, int saleid, int status) {
        try {
            String sql = "WITH \n"
                    + "t as\n"
                    + "(SELECT [Order].id as OrderID,[User].fullname as CustomerName,[User].id as CustomerID, [Order].[date], [Order].totalPrice, [Product].[name] as ProductName, [Order].[status] as OrderStatus, [Order].sellerId\n"
                    + "FROM \n"
                    + "[User] inner join [Order] on [User].id = [Order].userId\n"
                    + "inner join OrderDetail ON [Order].id = OrderDetail.orderId\n"
                    + "inner join Product ON OrderDetail.productId = Product.id),\n"
                    + "b as\n"
                    + "(SELECT [Order].id as OrderID, COUNT(Product.id) as NumberOfProducts\n"
                    + "FROM [Order] inner join OrderDetail ON [Order].id = OrderDetail.orderId\n"
                    + "inner join Product ON OrderDetail.productId = Product.id\n"
                    + "group by [Order].id),\n"
                    + "c as\n"
                    + "(\n"
                    + "SELECT  a.*\n"
                    + "FROM    (\n"
                    + "        SELECT  DISTINCT t.OrderID\n"
                    + "        FROM t\n"
                    + "        ) mo\n"
                    + "CROSS APPLY\n"
                    + "        (\n"
                    + "        SELECT  TOP 1 *\n"
                    + "        FROM    t mi\n"
                    + "        WHERE   mi.OrderID = mo.OrderID\n"
                    + "        ) a\n"
                    + "		)\n"
                    + "Select c.OrderID, c.[date], c.totalPrice, c.CustomerID, c.CustomerName, c.ProductName, c.OrderStatus, b.NumberOfProducts, c.sellerId, [User].fullname as SellerName\n"
                    + "from c inner join b on c.OrderID = b.OrderID\n"
                    + "left outer join [User] on c.sellerId = [User].id\n"
                    + "\n"
                    + "WHERE c.[date] between ? and ? and c.sellerId = ? and c.OrderStatus = ?";
            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setString(1, startDate);
            stm.setString(2, endDate);
            stm.setInt(3, saleid);
            stm.setInt(4, status);
            ResultSet rs = stm.executeQuery();
            ArrayList<Order> orders = new ArrayList<>();
            while (rs.next()) {
                Order o = new Order();
                o.setId(rs.getInt("OrderID"));
                o.setDate(rs.getDate("date"));
                o.setTotalcost(rs.getDouble("totalPrice"));
                o.setStatus(rs.getInt("OrderStatus"));
                o.setNumproducts(rs.getInt("NumberOfProducts"));

                User buyer = new User();
                buyer.setId(rs.getInt("CustomerID"));
                buyer.setFullname(rs.getString("CustomerName"));

                User seller = new User();
                seller.setId(rs.getInt("sellerId"));
                seller.setFullname(rs.getString("SellerName"));

                o.setBuyer(buyer);
                o.setSale(seller);

                ArrayList<Product> products = new ArrayList<>();
                Product p = new Product();
                p.setName(rs.getString("ProductName"));
                products.add(p);

                o.setProducts(products);
                orders.add(o);
            }
            return orders;
        } catch (SQLException ex) {
            Logger.getLogger(UserDBContext.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public ArrayList<Order> getOrders(String startDate, String endDate, int saleid) {
        try {
            String sql = "WITH \n"
                    + "t as\n"
                    + "(SELECT [Order].id as OrderID,[User].fullname as CustomerName,[User].id as CustomerID, [Order].[date], [Order].totalPrice, [Product].[name] as ProductName, [Order].[status] as OrderStatus, [Order].sellerId\n"
                    + "FROM \n"
                    + "[User] inner join [Order] on [User].id = [Order].userId\n"
                    + "inner join OrderDetail ON [Order].id = OrderDetail.orderId\n"
                    + "inner join Product ON OrderDetail.productId = Product.id),\n"
                    + "b as\n"
                    + "(SELECT [Order].id as OrderID, COUNT(Product.id) as NumberOfProducts\n"
                    + "FROM [Order] inner join OrderDetail ON [Order].id = OrderDetail.orderId\n"
                    + "inner join Product ON OrderDetail.productId = Product.id\n"
                    + "group by [Order].id),\n"
                    + "c as\n"
                    + "(\n"
                    + "SELECT  a.*\n"
                    + "FROM    (\n"
                    + "        SELECT  DISTINCT t.OrderID\n"
                    + "        FROM t\n"
                    + "        ) mo\n"
                    + "CROSS APPLY\n"
                    + "        (\n"
                    + "        SELECT  TOP 1 *\n"
                    + "        FROM    t mi\n"
                    + "        WHERE   mi.OrderID = mo.OrderID\n"
                    + "        ) a\n"
                    + "		)\n"
                    + "Select c.OrderID, c.[date], c.totalPrice, c.CustomerID, c.CustomerName, c.ProductName, c.OrderStatus, b.NumberOfProducts, c.sellerId, [User].fullname as SellerName\n"
                    + "from c inner join b on c.OrderID = b.OrderID\n"
                    + "left outer join [User] on c.sellerId = [User].id\n"
                    + "\n"
                    + "WHERE c.[date] between ? and ? and c.sellerId = ?";
            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setString(1, startDate);
            stm.setString(2, endDate);
            stm.setInt(3, saleid);;
            ResultSet rs = stm.executeQuery();
            ArrayList<Order> orders = new ArrayList<>();
            while (rs.next()) {
                Order o = new Order();
                o.setId(rs.getInt("OrderID"));
                o.setDate(rs.getDate("date"));
                o.setTotalcost(rs.getDouble("totalPrice"));
                o.setStatus(rs.getInt("OrderStatus"));
                o.setNumproducts(rs.getInt("NumberOfProducts"));

                User buyer = new User();
                buyer.setId(rs.getInt("CustomerID"));
                buyer.setFullname(rs.getString("CustomerName"));

                User seller = new User();
                seller.setId(rs.getInt("sellerId"));
                seller.setFullname(rs.getString("SellerName"));

                o.setBuyer(buyer);
                o.setSale(seller);

                ArrayList<Product> products = new ArrayList<>();
                Product p = new Product();
                p.setName(rs.getString("ProductName"));
                products.add(p);

                o.setProducts(products);
                orders.add(o);
            }
            return orders;
        } catch (SQLException ex) {
            Logger.getLogger(UserDBContext.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public ArrayList<Order> getOrdersByStatus(String startDate, String endDate, int status) {
        try {
            String sql = "WITH \n"
                    + "t as\n"
                    + "(SELECT [Order].id as OrderID,[User].fullname as CustomerName,[User].id as CustomerID, [Order].[date], [Order].totalPrice, [Product].[name] as ProductName, [Order].[status] as OrderStatus, [Order].sellerId\n"
                    + "FROM \n"
                    + "[User] inner join [Order] on [User].id = [Order].userId\n"
                    + "inner join OrderDetail ON [Order].id = OrderDetail.orderId\n"
                    + "inner join Product ON OrderDetail.productId = Product.id),\n"
                    + "b as\n"
                    + "(SELECT [Order].id as OrderID, COUNT(Product.id) as NumberOfProducts\n"
                    + "FROM [Order] inner join OrderDetail ON [Order].id = OrderDetail.orderId\n"
                    + "inner join Product ON OrderDetail.productId = Product.id\n"
                    + "group by [Order].id),\n"
                    + "c as\n"
                    + "(\n"
                    + "SELECT  a.*\n"
                    + "FROM    (\n"
                    + "        SELECT  DISTINCT t.OrderID\n"
                    + "        FROM t\n"
                    + "        ) mo\n"
                    + "CROSS APPLY\n"
                    + "        (\n"
                    + "        SELECT  TOP 1 *\n"
                    + "        FROM    t mi\n"
                    + "        WHERE   mi.OrderID = mo.OrderID\n"
                    + "        ) a\n"
                    + "		)\n"
                    + "Select c.OrderID, c.[date], c.totalPrice, c.CustomerID, c.CustomerName, c.ProductName, c.OrderStatus, b.NumberOfProducts, c.sellerId, [User].fullname as SellerName\n"
                    + "from c inner join b on c.OrderID = b.OrderID\n"
                    + "left outer join [User] on c.sellerId = [User].id\n"
                    + "\n"
                    + "WHERE c.[date] between ? and ? and c.OrderStatus = ?";
            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setString(1, startDate);
            stm.setString(2, endDate);
            stm.setInt(3, status);;
            ResultSet rs = stm.executeQuery();
            ArrayList<Order> orders = new ArrayList<>();
            while (rs.next()) {
                Order o = new Order();
                o.setId(rs.getInt("OrderID"));
                o.setDate(rs.getDate("date"));
                o.setTotalcost(rs.getDouble("totalPrice"));
                o.setStatus(rs.getInt("OrderStatus"));
                o.setNumproducts(rs.getInt("NumberOfProducts"));

                User buyer = new User();
                buyer.setId(rs.getInt("CustomerID"));
                buyer.setFullname(rs.getString("CustomerName"));

                User seller = new User();
                seller.setId(rs.getInt("sellerId"));
                seller.setFullname(rs.getString("SellerName"));

                o.setBuyer(buyer);
                o.setSale(seller);

                ArrayList<Product> products = new ArrayList<>();
                Product p = new Product();
                p.setName(rs.getString("ProductName"));
                products.add(p);

                o.setProducts(products);
                orders.add(o);
            }
            return orders;
        } catch (SQLException ex) {
            Logger.getLogger(UserDBContext.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public void updateOrderStatus(int orderid, int status) {
        try {
            String sql = "UPDATE [dbo].[Order]\n"
                    + "   SET [status] = ?\n"
                    + " WHERE [Order].id = ?";
            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setInt(1, status);
            stm.setInt(2, orderid);
            stm.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(UserDBContext.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
