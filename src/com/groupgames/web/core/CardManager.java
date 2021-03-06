package com.groupgames.web.core;

import java.sql.*;
import java.util.ArrayList;

public class CardManager {
    public static ArrayList<Card> getWhiteCards(int cardAmount) {
        String SQL = "SELECT * FROM KaH_WhiteCard ORDER BY RAND() LIMIT " + cardAmount;
        ArrayList<Card> whiteCardsList = new ArrayList<>();

        try {
            Connection conn = DBManager.getInstance().getConnection();
            ResultSet rs = conn.createStatement().executeQuery(SQL);

            while (rs.next()) {
                int cardID = rs.getInt(1);
                String cardText = rs.getString(2);
                Card card = new Card(cardID, cardText);
                whiteCardsList.add(card);
            }
        } catch (SQLException e){
            e.printStackTrace();
        }

        return whiteCardsList;
    }

    public static ArrayList<Card> getAllBlackCards() {
        String SQL = "SELECT * FROM KaH_BlackCard";
        ArrayList<Card> blackCardsList = new ArrayList<>();

        try {
            Connection conn  = DBManager.getInstance().getConnection();
            ResultSet rs = conn.createStatement().executeQuery(SQL);

            while(rs.next()) {
                int cardID = rs.getInt(1);
                String cardText = rs.getString(2);
                Card card = new Card(cardID, cardText);
                blackCardsList.add(card);
            }
        } catch (SQLException e){
            e.printStackTrace();
        }

        return blackCardsList;
    }

    public static Card getRandBlackCard() {
        String SQL = "SELECT * FROM KaH_BlackCard ORDER BY RAND() LIMIT 1";
        Card card = null;

        try {
            Connection conn  = DBManager.getInstance().getConnection();
            ResultSet rs = conn.createStatement().executeQuery(SQL);

            while(rs.next()) {
                int cardID = rs.getInt(1);
                String cardText = rs.getString(2);
                card = new Card(cardID, cardText);
            }
        } catch (SQLException e){
            e.printStackTrace();
        }

        return card;
    }

    public static Card getWhiteCardId(int cardId) {
        String SQL = "SELECT * FROM KaH_WhiteCard WHERE CardId = " + cardId;
        Card card = null;

        try {
            Connection conn  = DBManager.getInstance().getConnection();
            ResultSet rs = conn.createStatement().executeQuery(SQL);

            if(rs.next()) {
                int cardID = rs.getInt(1);
                String cardText = rs.getString(2);
                card = new Card(cardID, cardText);
            }
        } catch (SQLException e){
            e.printStackTrace();
        }

        return card;
    }
}
