package com.fo.dao;

import com.fo.model.Order;
import com.fo.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class OrderDao {
    public void saveOrder(Order order) {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement("INSERT INTO orders (customer_name, items, total, discount, payment_mode, delivery_partner, final_amount) VALUES (?, ?, ?, ?, ?, ?, ?)")) {
            ps.setString(1, order.customerName);
            ps.setString(2, order.items);
            ps.setDouble(3, order.total);
            ps.setDouble(4, order.discount);
            ps.setString(5, order.paymentMode);
            ps.setString(6, order.deliveryPartner);
            ps.setDouble(7, order.finalAmount);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
