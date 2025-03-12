import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
public class Daily implements Methods {
    JTextField descTxt,withdrawTxt,depositTxt,searchTxt;
    JLabel descLbl,withdrawLbl,depositLbl,totWithLbl, totDepLbl,totBalLbl,searchLbl;
    JButton saveBtn, deleteBtn, editBtn, clearBtn,viewBtn, backBtn, exportBtn;
    JTable table;
    JPanel panel;
    JFrame frame;


    Daily(){
        frame = new JFrame();
        schedule();// calling the time to write into the file

        table = new JTable();
        table.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                DefaultTableModel model = (DefaultTableModel) table.getModel();
                int selectedIndex = table.getSelectedRow();

                descTxt.setText(model.getValueAt(selectedIndex,2).toString());
                withdrawTxt.setText(model.getValueAt(selectedIndex, 3).toString());
                depositTxt.setText(model.getValueAt(selectedIndex,4).toString());
            }

            @Override
            public void mousePressed(MouseEvent e) {}
            @Override
            public void mouseReleased(MouseEvent e) {}
            @Override
            public void mouseEntered(MouseEvent e) {}
            @Override
            public void mouseExited(MouseEvent e) {}
        });

        table.setBackground(Color.white);
        table.setForeground(Color.black);
        table.setRowHeight(30);
        table.setAutoCreateRowSorter(true);
        table.setFont(new Font(null,Font.PLAIN,15));

        viewAtOpen();

        totBalLbl = new JLabel("get total balance");
        totBalLbl.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                try {

                    clearTable();// method to clear table

                    Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/finance", "root", "aee?1Mkad");
                    Statement statement = connection.createStatement();
                    // above 2 lines connect to database
                    ResultSet resultSet = statement.executeQuery("select sum(deposit) - sum(withdraw) as balance from daily");
                    ResultSetMetaData rsmd  = resultSet.getMetaData();
                    DefaultTableModel model = (DefaultTableModel) table.getModel();
                    // reading columns
                    int cols = rsmd.getColumnCount();
                    String[] colname = new String[cols];
                    for (int i=0; i<cols;i++)
                        colname[i] = rsmd.getColumnName(i+1);
                    model.setColumnIdentifiers(colname);

                    // reading rows
                    String total;
                    while (resultSet.next()) {

                        total = resultSet.getString(1);

                        String[] row = {total};
                        model.addRow(row);
                    }
                    statement.close();
                    connection.close();

                } catch (Exception ex){
                    ex.printStackTrace();
                }
            }
        });
        totWithLbl = new JLabel("get total withdraw");
        totWithLbl.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                try {

                    clearTable();// method to clear table

                    Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/finance", "root", "aee?1Mkad");
                    Statement statement = connection.createStatement();
                    // above 2 lines connect to database
                    ResultSet resultSet = statement.executeQuery("select sum(withdraw) as withdraws from daily");
                    ResultSetMetaData rsmd  = resultSet.getMetaData();
                    DefaultTableModel model = (DefaultTableModel) table.getModel();
                    // reading columns
                    int cols = rsmd.getColumnCount();
                    String[] colname = new String[cols];
                    for (int i=0; i<cols;i++)
                        colname[i] = rsmd.getColumnName(i+1);
                    model.setColumnIdentifiers(colname);

                    // reading rows
                    String total;
                    while (resultSet.next()) {

                        total = resultSet.getString(1);

                        String[] row = {total};
                        model.addRow(row);
                    }
                    statement.close();
                    connection.close();

                } catch (Exception ex){
                    ex.printStackTrace();
                }
            }
        });

        totDepLbl = new JLabel("get total deposit");
        totDepLbl.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                try {

                    clearTable();// method to clear table

                    Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/finance", "root", "aee?1Mkad");
                    Statement statement = connection.createStatement();
                    // above 2 lines connect to database
                    ResultSet resultSet = statement.executeQuery("select sum(deposit)  as deposits from daily");
                    ResultSetMetaData rsmd  = resultSet.getMetaData();
                    DefaultTableModel model = (DefaultTableModel) table.getModel();
                    // reading columns
                    int cols = rsmd.getColumnCount();
                    String[] colname = new String[cols];
                    for (int i=0; i<cols;i++)
                        colname[i] = rsmd.getColumnName(i+1);
                    model.setColumnIdentifiers(colname);

                    // reading rows
                    String total;
                    while (resultSet.next()) {

                        total = resultSet.getString(1);

                        String[] row = {total};
                        model.addRow(row);
                    }
                    statement.close();
                    connection.close();

                } catch (Exception ex){
                    ex.printStackTrace();
                }
            }
        });

        descLbl = new JLabel("description");
        descTxt = new JTextField();
        descTxt.setPreferredSize(new Dimension(250,30));

        withdrawLbl = new JLabel("withdraw");
        withdrawTxt = new JTextField();
        withdrawTxt.setPreferredSize(new Dimension(100,30));

        depositLbl = new JLabel("deposit ");
        depositTxt = new JTextField();
        depositTxt.setPreferredSize(new Dimension(100,30));

        searchLbl = new JLabel("search using date or time ");
        searchTxt = new JTextField();
        searchTxt.setPreferredSize(new Dimension(100,30));
        searchTxt.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
                Searching();
            }
        });
        backBtn = new JButton("back");
        backBtn.setFocusable(false);
        backBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Finance finance = new Finance();
                frame.dispose();
            }
        });


        saveBtn = new JButton("save");
        saveBtn.setFocusable(false);
        saveBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Save();

            }
        });

        exportBtn = new JButton("Export");
        exportBtn.setFocusable(false);
        exportBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                writeToFile();
            }
        });

        deleteBtn = new JButton("delete");
        deleteBtn.setFocusable(false);
        deleteBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Delete();
            }
        });

        editBtn = new JButton("edit");
        editBtn.setFocusable(false);
        editBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Edit();
            }
        });

        clearBtn = new JButton("clear");
        clearBtn.setFocusable(false);
        clearBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearTxtFields();
            }
        });

        viewBtn = new JButton("view");
        viewBtn.setFocusable(false);
        viewBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewAtOpen();
            }
        });


        panel = new JPanel();
        panel.setPreferredSize(new Dimension(650,60));
        panel.setLayout(new FlowLayout(FlowLayout.LEFT,3,10));
        panel.setBackground(Color.CYAN);

        panel.add(descLbl);
        panel.add(descTxt);

        panel.add(withdrawLbl);
        panel.add(withdrawTxt);

        panel.add(depositLbl);
        panel.add(depositTxt);

        frame.add(saveBtn);
        frame.add(deleteBtn);
        frame.add(editBtn);
        frame.add(clearBtn);
        frame.add(viewBtn);
        frame.add(exportBtn);
        frame.add(backBtn);


        JScrollPane pane = new JScrollPane(table);
        pane.setPreferredSize(new Dimension(650,300));
        frame.add(panel);
        frame.add(totBalLbl);
        frame.add(totDepLbl);
        frame.add(totWithLbl);
        frame.add(searchLbl);
        frame.add(searchTxt);
        frame.add(pane);

        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout( new FlowLayout(FlowLayout.CENTER, 30,10));
        frame.setBounds(400,100,700,600);
        frame.setBackground(Color.WHITE);
        frame.setVisible(true);
        frame.setResizable(false);

    }
    @Override
    public void clearTable() {
        table.setModel(new DefaultTableModel());
    }

    @Override
    public void Searching() {
        try {

            clearTable();

            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/finance", "root", "aee?1Mkad");
            Statement statement = connection.createStatement();
            // above 2 lines connect to database
            ResultSet resultSet = statement.executeQuery("select * from daily where date_time like '%"+searchTxt.getText()+"%'");
            //ResultSet resultSet = statement.executeQuery("select * from student where first_name R");
            ResultSetMetaData rsmd  = resultSet.getMetaData();
            DefaultTableModel model = (DefaultTableModel) table.getModel();
            // reading columns
            int cols = rsmd.getColumnCount();
            String[] colname = new String[cols];
            for (int i=0; i<cols;i++)
                colname[i] = rsmd.getColumnName(i+1);
            model.setColumnIdentifiers(colname);

            // reading rows
            String txID, date, desc,withd, depos;
            while (resultSet.next()) {

                txID = resultSet.getString(1);
                date = resultSet.getString(2);
                desc = resultSet.getString(3);
                withd = resultSet.getString(4);
                depos = resultSet.getString(5);



                String[] row = {txID,date,desc,withd,depos};
                model.addRow(row);
            }
            statement.close();
            connection.close();

        } catch (Exception ex){
            ex.printStackTrace();
        }

    }

    @Override
    public void clearTxtFields() {

        descTxt.setText("");
        withdrawTxt.setText("");
        depositTxt.setText("");
    }

    @Override
    public void update() {

    }

    @Override
    public void Edit() {

        if (withdrawTxt.getText().isEmpty()|| depositTxt.getText().isEmpty()||descTxt.getText().isEmpty()) {

            JOptionPane.showMessageDialog(frame,"error! select row in the table to edit");

        }

        else{

            try {

                //clearTable();

                DefaultTableModel model = (DefaultTableModel) table.getModel();
                int selectedIndex = table.getSelectedRow();

                //int regstration_number = Integer.parseInt(model.getValueAt(selectedIndex,0).toString());
                String regstration_number = (model.getValueAt(selectedIndex,0).toString());

                String desc, withd, dep;

                desc = descTxt.getText();
                withd = withdrawTxt.getText();
                dep = depositTxt.getText();

                Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/finance", "root", "aee?1Mkad");
                PreparedStatement pst = connection.prepareStatement("update daily set description=?,withdraw=?,deposit=? where trans_id =? ");
                pst.setString(4,regstration_number);
                pst.setString(1,desc);
                pst.setString(2,withd);
                pst.setString(3,dep);

                pst.executeUpdate();
                JOptionPane.showMessageDialog(frame,"record edited");
                viewAtOpen();
                clearTxtFields();

                descTxt.requestFocus();// brings back focus to reg text field


            }catch (Exception ex){
                ex.printStackTrace();
            }
            clearTable();
            viewAtOpen();

        }
    }

    @Override
    public void Delete() {

        if (descTxt.getText().isEmpty()|| withdrawTxt.getText().isEmpty()||depositTxt.getText().isEmpty()) {

            JOptionPane.showMessageDialog(frame,"error! select row in the table to delete");

        }

        else {

            try {

                DefaultTableModel model = (DefaultTableModel) table.getModel();
                int selectedIndex = table.getSelectedRow();

                int ID_number = Integer.parseInt(model.getValueAt(selectedIndex,0).toString());
                Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/finance", "root", "aee?1Mkad");
                PreparedStatement pst = connection.prepareStatement("delete from daily where trans_id=?");
                pst.setInt(1,ID_number);

                pst.executeUpdate();
                JOptionPane.showMessageDialog(frame,"record deleted");
                viewAtOpen();

                //clearing the txt fields
                clearTxtFields();
                descTxt.requestFocus();// brings back focus to reg text field

            }catch (Exception ex){
                ex.printStackTrace();
            }
            clearTable();
            viewAtOpen();

        }

    }

    @Override
    public void Save() {
        if (descTxt.getText().isEmpty()|| withdrawTxt.getText().isEmpty()||depositTxt.getText().isEmpty()) {

            JOptionPane.showMessageDialog(frame,"error! fill all fields");
        }

        else {

            try {

                clearTable();

                String phone, first_name, surname;
                phone = descTxt.getText();
                first_name = withdrawTxt.getText();
                surname = depositTxt.getText();

                Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/finance", "root", "aee?1Mkad");
                PreparedStatement pst = connection.prepareStatement("insert into daily (description,withdraw,deposit)values (?,?,?)");
                pst.setString(1,phone);
                pst.setString(2,first_name);
                pst.setString(3,surname);

                pst.executeUpdate();
                JOptionPane.showMessageDialog(frame,"record saved");
                viewAtOpen();

                //clearing the txt fields
                clearTxtFields();

                descTxt.requestFocus();// brings back focus to reg text field


            }catch (Exception ex){
                ex.printStackTrace();
            }

        }
    }

    public void writeToFile(){

        try {

            BufferedWriter writer = new BufferedWriter(new FileWriter("C:\\Users\\MPHATSO\\Desktop\\Report.csv"));
            for (int i =0; i < table.getRowCount(); i++){
                for (int j=0; j < table.getColumnCount(); j++ ){
                    writer.write(table.getColumnName(j)+",");
                    writer.write(table.getValueAt(i,j) + ",");
                }
                writer.newLine();
            }

            writer.close();
            JOptionPane.showMessageDialog(frame,"Export successful! saved as Report on desktop.");

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public void schedule(){

                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY, 16); // 4:00 PM
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);

                // If the specified time is in the past, move it to the next day
                if (calendar.getTime().before(new java.util.Date())) {
                    calendar.add(Calendar.DAY_OF_MONTH, 1);
                }

                java.util.Timer timer = new Timer();
                TimerTask task = new TimerTask() {
                    @Override
                    public void run() {
                        writeToFile();
                    }
                };

                // Schedule the task to run daily at 4:00 PM
                timer.schedule(task, calendar.getTime(), 24 * 60 * 60 * 1000); // 24 hours in milliseconds

    }



    @Override
    public void viewAtOpen() {
        try {
            clearTable();

            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/finance", "root", "aee?1Mkad");
            Statement statement = connection.createStatement();
            // above 2 lines connect to database

            ResultSet resultSet = statement.executeQuery("select * from daily ");
            ResultSetMetaData rsmd  = resultSet.getMetaData();
            DefaultTableModel model = (DefaultTableModel) table.getModel();

            // reading columns
            int cols = rsmd.getColumnCount();
            String[] colname = new String[cols];
            for (int i=0; i<cols;i++)
                colname[i] = rsmd.getColumnName(i+1);
            model.setColumnIdentifiers(colname);

            // reading rows
            String ID,first_name, surname,phone,date;
            while (resultSet.next()) {

                ID = resultSet.getString(1);
                phone = resultSet.getString(4);
                first_name = resultSet.getString(2);
                surname = resultSet.getString(3);
                date = resultSet.getString(5);

                String[] row = {ID,first_name,surname,phone,date};
                model.addRow(row);
            }
            statement.close();
            connection.close();

        } catch (Exception ex){
            ex.printStackTrace();
        }
    }
}
