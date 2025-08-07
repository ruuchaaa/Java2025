package com.fo.dao;

import com.fo.util.DBConnection;

import java.sql.*;
import java.util.*;

public class DeliveryAgentDao {

    public List<String> getAllAgents() {
        List<String> agents = new ArrayList<>();
        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery("SELECT name FROM delivery_agents")) {
            while (rs.next()) {
                agents.add(rs.getString("name"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return agents;
    }

    public void addAgent(String name) {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement("INSERT INTO delivery_agents(name) VALUES(?)")) {
            ps.setString(1, name);
            ps.executeUpdate();
            System.out.println("Delivery agent added.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
