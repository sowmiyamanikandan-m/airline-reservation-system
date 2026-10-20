import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class BookingForm extends JFrame {

    JTextField tfBookingID, tfPassengerName, tfFlightName;
    JTextField tfSource, tfDestination, tfPrice;

    JButton btnAdd, btnView, btnUpdate, btnDelete;

    JTextArea taOutput;

    String url = "jdbc:mysql://localhost:3306/formdb";
    String user = "root";
    String pass = "";

    public BookingForm() {

        setTitle("Airline Booking Form");
        setSize(650, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new FlowLayout());

        getContentPane().setBackground(new Color(210, 230, 250));

        tfBookingID = new JTextField(5);
        tfPassengerName = new JTextField(10);
        tfFlightName = new JTextField(10);
        tfSource = new JTextField(10);
        tfDestination = new JTextField(10);
        tfPrice = new JTextField(7);

        btnAdd = new JButton("Add");
        btnView = new JButton("View");
        btnUpdate = new JButton("Update");
        btnDelete = new JButton("Delete");

        taOutput = new JTextArea(15, 55);
        taOutput.setEditable(false);

        JScrollPane scroll = new JScrollPane(taOutput);

        add(new JLabel("Booking ID:"));
        add(tfBookingID);

        add(new JLabel("Passenger Name:"));
        add(tfPassengerName);

        add(new JLabel("Flight Name:"));
        add(tfFlightName);

        add(new JLabel("Source:"));
        add(tfSource);

        add(new JLabel("Destination:"));
        add(tfDestination);

        add(new JLabel("Price:"));
        add(tfPrice);

        add(btnAdd);
        add(btnView);
        add(btnUpdate);
        add(btnDelete);

        add(scroll);

        // ADD
        btnAdd.addActionListener(e -> {

            try (Connection con =
                    DriverManager.getConnection(url, user, pass)) {

                String sql = "INSERT INTO bookings VALUES (?, ?, ?, ?, ?, ?)";

                PreparedStatement pst = con.prepareStatement(sql);

                pst.setString(1, tfBookingID.getText());
                pst.setString(2, tfPassengerName.getText());
                pst.setString(3, tfFlightName.getText());
                pst.setString(4, tfSource.getText());
                pst.setString(5, tfDestination.getText());
                pst.setDouble(6, Double.parseDouble(tfPrice.getText()));

                pst.executeUpdate();

                JOptionPane.showMessageDialog(
                        this,
                        "Booking Added Successfully!"
                );

            } catch (Exception ex) {

                JOptionPane.showMessageDialog(
                        this,
                        "Error: " + ex.getMessage()
                );
            }
        });

        // VIEW
        btnView.addActionListener(e -> {

            try (Connection con =
                    DriverManager.getConnection(url, user, pass);
                 Statement st = con.createStatement();
                 ResultSet rs =
                    st.executeQuery("SELECT * FROM bookings")) {

                StringBuilder sb = new StringBuilder();

                while (rs.next()) {

                    sb.append("Booking ID: ")
                      .append(rs.getString("booking_id"))
                      .append(", Passenger: ")
                      .append(rs.getString("passenger_name"))
                      .append(", Flight: ")
                      .append(rs.getString("flight_name"))
                      .append(", From: ")
                      .append(rs.getString("source"))
                      .append(", To: ")
                      .append(rs.getString("destination"))
                      .append(", Price: ₹")
                      .append(rs.getDouble("price"))
                      .append("\n");
                }

                taOutput.setText(sb.toString());

            } catch (Exception ex) {

                JOptionPane.showMessageDialog(
                        this,
                        "Error: " + ex.getMessage()
                );
            }
        });

        // UPDATE
        btnUpdate.addActionListener(e -> {

            try (Connection con =
                    DriverManager.getConnection(url, user, pass)) {

                String sql =
                    "UPDATE bookings SET passenger_name=?, "
                    + "flight_name=?, source=?, destination=?, price=? "
                    + "WHERE booking_id=?";

                PreparedStatement pst =
                    con.prepareStatement(sql);

                pst.setString(1, tfPassengerName.getText());
                pst.setString(2, tfFlightName.getText());
                pst.setString(3, tfSource.getText());
                pst.setString(4, tfDestination.getText());
                pst.setDouble(5,
                        Double.parseDouble(tfPrice.getText()));
                pst.setString(6, tfBookingID.getText());

                int rows = pst.executeUpdate();

                if (rows > 0) {
                    JOptionPane.showMessageDialog(
                            this,
                            "Booking Updated Successfully!"
                    );
                } else {
                    JOptionPane.showMessageDialog(
                            this,
                            "Booking ID Not Found!"
                    );
                }

            } catch (Exception ex) {

                JOptionPane.showMessageDialog(
                        this,
                        "Error: " + ex.getMessage()
                );
            }
        });

        // DELETE
        btnDelete.addActionListener(e -> {

            try (Connection con =
                    DriverManager.getConnection(url, user, pass)) {

                String sql =
                    "DELETE FROM bookings WHERE booking_id=?";

                PreparedStatement pst =
                    con.prepareStatement(sql);

                pst.setString(1, tfBookingID.getText());

                int rows = pst.executeUpdate();

                if (rows > 0) {
                    JOptionPane.showMessageDialog(
                            this,
                            "Booking Deleted Successfully!"
                    );
                } else {
                    JOptionPane.showMessageDialog(
                            this,
                            "Booking ID Not Found!"
                    );
                }

            } catch (Exception ex) {

                JOptionPane.showMessageDialog(
                        this,
                        "Error: " + ex.getMessage()
                );
            }
        });

        setVisible(true);
    }

    public static void main(String[] args) {

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    null,
                    e.getMessage()
            );
        }

        new BookingForm();
    }
}
