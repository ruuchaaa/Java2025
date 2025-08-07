package com.fo.dao;

import com.fo.model.FoodItem;
import com.fo.util.DBConnection;
import java.sql.*;
import java.util.*;

public class FoodItemDao {

    public List<FoodItem> getAllItems() {
        List<FoodItem> list = new ArrayList<>();
        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM food_items")) {
            while (rs.next()) {
                list.add(new FoodItem(rs.getInt(1), rs.getString(2), rs.getDouble(3)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public void addItem(String name, double price) {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement("INSERT INTO food_items (name, price) VALUES (?, ?)")) {
            ps.setString(1, name);
            ps.setDouble(2, price);
            ps.executeUpdate();
            System.out.println("Item added.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
