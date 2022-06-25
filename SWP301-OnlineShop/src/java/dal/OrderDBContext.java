/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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

    public int addOrder(Product[] productsOrder, long total, int idCustomer,
            String email, String shipFullName, String shipAddress, String shipPhone,
            String shipNote, int idPayment, int idSeller) {
        int idOrder = 0;
        try {
            connection.setAutoCommit(false);
            String sqlInsertShip = "INSERT INTO [dbo].[ShipInfo]\n"
                    + "           ([fullname]\n"
                    + "           ,[address]\n"
                    + "           ,[phone]\n"
                    + "           ,[email])\n"
                    + "     VALUES\n"
                    + "           (?\n"
                    + "           ,?\n"
                    + "           ,?\n"
                    + "           ,?)";
            PreparedStatement stmInsertShip = connection.prepareStatement(sqlInsertShip);
            stmInsertShip.setString(1, shipFullName);
            stmInsertShip.setString(2, shipAddress);
            stmInsertShip.setString(3, shipPhone);
            stmInsertShip.setString(4, email);
            stmInsertShip.executeUpdate();

            String getIdShipInfo = "SELECT @@IDENTITY AS idShip";
            PreparedStatement stmGetIdShip = connection.prepareStatement(getIdShipInfo);
            ResultSet rs = stmGetIdShip.executeQuery();
            int idShip = 0;
            while (rs.next()) {
                idShip = rs.getInt(1);
            }

            String sqlInsertOrder = "INSERT INTO [dbo].[Order]\n"
                    + "           ([userId]\n"
                    + "           ,[totalPrice]\n"
                    + "           ,[note]\n"
                    + "           ,[status]\n"
                    + "           ,[date]\n"
                    + "           ,[idShip]\n"
                    + "           ,[payment]\n"
                    + "           ,[sellerid])\n"
                    + "     VALUES\n"
                    + "           (?\n"
                    + "           ,?\n"
                    + "           ,?\n"
                    + "           ,?\n"
                    + "           ,?\n"
                    + "           ,?\n"
                    + "           ,?\n"
                    + "           ,?)";
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("uuuu/MM/dd");
            LocalDate localDate = LocalDate.now();
            Date dateNow = Date.valueOf(dtf.format(localDate).replaceAll("/", "-"));
            PreparedStatement stmInsertOrder = connection.prepareStatement(sqlInsertOrder);
            stmInsertOrder.setInt(1, idCustomer);
            stmInsertOrder.setFloat(2, total);
            stmInsertOrder.setString(3, shipNote);
            stmInsertOrder.setInt(4, 1);
            stmInsertOrder.setDate(5, dateNow);
            stmInsertOrder.setInt(6, idShip);
            stmInsertOrder.setInt(7, idPayment);
            stmInsertOrder.setInt(8, idSeller);
            stmInsertOrder.executeUpdate();

            String getIdOrder = "SELECT @@IDENTITY AS id";
            PreparedStatement stmGetIdOrder = connection.prepareStatement(getIdOrder);
            ResultSet rs2 = stmGetIdOrder.executeQuery();

            while (rs2.next()) {
                idOrder = rs2.getInt(1);
            }
            String sqlInsertProductOrder = "INSERT INTO [dbo].[OrderDetail]\n"
                    + "           ([orderId]\n"
                    + "           ,[productId]\n"
                    + "           ,[discount]\n"
                    + "           ,[quantity]\n"
                    + "           ,[price])\n"
                    + "     VALUES\n"
                    + "           (?\n"
                    + "           ,?\n"
                    + "           ,?\n"
                    + "           ,?\n"
                    + "           ,?)";
            for (Product product : productsOrder) {
                PreparedStatement stmInsertProductsOrder = connection.prepareStatement(sqlInsertProductOrder);
                stmInsertProductsOrder.setInt(1, idOrder);
                stmInsertProductsOrder.setInt(2, product.getId());
                stmInsertProductsOrder.setInt(3, product.getDiscount());
                stmInsertProductsOrder.setLong(4, product.getQuantity());
                stmInsertProductsOrder.setLong(5, product.getPrice());
                stmInsertProductsOrder.executeUpdate();
            }
            connection.commit();
        } catch (SQLException ex) {
            idOrder = 0;
            try {
                connection.rollback();
            } catch (SQLException ex1) {
                Logger.getLogger(OrderDBContext.class.getName()).log(Level.SEVERE, null, ex1);
            }
            Logger.getLogger(OrderDBContext.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException ex) {
                Logger.getLogger(OrderDBContext.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                connection.close();
            } catch (SQLException ex) {
                Logger.getLogger(OrderDBContext.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return idOrder;
    }

}
