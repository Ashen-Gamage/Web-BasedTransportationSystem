package com.example.dao;
import com.example.util.DBUtil;
import java.sql.*;

public class WalletDao {
    public void creditRideEarning(Connection c, int driverId, int rideId, long amountCents, String note) throws Exception {
        insertTxn(c, driverId, rideId, "CREDIT_RIDE_EARNING", amountCents, note);
        upsertBalance(c, driverId, amountCents);
    }

    public void debitPayout(Connection c, int driverId, long amountCents, String note) throws Exception {
        insertTxn(c, driverId, null, "DEBIT_PAYOUT", -amountCents, note);
        upsertBalance(c, driverId, -amountCents);
    }

    public void adjustment(Connection c, int driverId, Integer rideId, long deltaCents, String note) throws Exception {
        insertTxn(c, driverId, rideId, "ADJUSTMENT", deltaCents, note);
        upsertBalance(c, driverId, deltaCents);
    }

    private void insertTxn(Connection c, int driverId, Integer rideId, String type, long amountCents, String note) throws Exception {
        try (PreparedStatement ps = c.prepareStatement(
                "INSERT INTO wallet_transactions(driver_user_id, ride_id, type, amount_cents, note, created_at) VALUES(?,?,?,?,?,GETDATE())")) {
            ps.setInt(1, driverId);
            if (rideId == null) ps.setNull(2, java.sql.Types.INTEGER); else ps.setInt(2, rideId);
            ps.setString(3, type);
            ps.setLong(4, amountCents);
            ps.setString(5, note);
            ps.executeUpdate();
        }
    }

    private void upsertBalance(Connection c, int driverId, long deltaCents) throws Exception {
        // Try update, if 0 rows then insert
        try (PreparedStatement ps = c.prepareStatement(
                "UPDATE driver_wallets SET balance_cents = balance_cents + ?, updated_at=GETDATE() WHERE driver_user_id=?")) {
            ps.setLong(1, deltaCents);
            ps.setInt(2, driverId);
            int updated = ps.executeUpdate();
            if (updated == 0) {
                try (PreparedStatement ins = c.prepareStatement(
                        "INSERT INTO driver_wallets(driver_user_id, balance_cents, updated_at) VALUES(?, ?, GETDATE())")) {
                    ins.setInt(1, driverId);
                    ins.setLong(2, Math.max(deltaCents, 0)); // first insert cannot be negative; adjust policy if needed
                    ins.executeUpdate();
                }
            }
        }
    }
}
