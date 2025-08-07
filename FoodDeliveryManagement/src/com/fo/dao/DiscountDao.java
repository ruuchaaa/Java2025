package com.fo.dao;

import com.fo.util.DBConnection;
import java.sql.*;

public class DiscountDao {
    public double getDiscountAmount(double total) {
        double discount = 0;
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement("SELECT discount_amount FROM discounts WHERE ? >= min_amount")) {
            ps.setDouble(1, total);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                discount = rs.getDouble("discount_amount");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return discount;
    }

    public void updateDiscountRule(double minAmount, double discountAmount) {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement("UPDATE discounts SET min_amount = ?, discount_amount = ? WHERE id = 1")) {
            ps.setDouble(1, minAmount);
            ps.setDouble(2, discountAmount);
            ps.executeUpdate();
            System.out.println("Discount rule updated.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
