import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class Parent implements Methods{

    JTextField idNumTxt,fNameTxt,sNameTxt, phoneTxt;
    JLabel idNumLbl,fNameLbl,sNameLbl,phoneLbl;
    JButton viewBtn, saveBtn, deleteBtn, editBtn, clearBtn,C_PBtn, backBtn;
    JTable table;
    JPanel panel;

    JFrame frame ;

    Parent(){

        frame = new JFrame();
        table = new JTable();
        table.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                DefaultTableModel model = (DefaultTableModel) table.getModel();
                int selectedIndex = table.getSelectedRow();

                idNumTxt.setText(model.getValueAt(selectedIndex,0).toString());
                fNameTxt.setText(model.getValueAt(selectedIndex, 1).toString());
                sNameTxt.setText(model.getValueAt(selectedIndex,2).toString());
                phoneTxt.setText(model.getValueAt(selectedIndex,3).toString());

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

        fNameLbl = new JLabel("first name");
        fNameTxt = new JTextField();
        fNameTxt.setPreferredSize(new Dimension(250,30));
        fNameTxt.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {}
            @Override
            public void keyPressed(KeyEvent e) {}
            @Override
            public void keyReleased(KeyEvent e) {
                Searching();
            }
        });

        idNumLbl = new JLabel("id number");
        idNumTxt = new JTextField();
        idNumTxt.setPreferredSize(new Dimension(250,30));

        sNameLbl = new JLabel("surname");
        sNameTxt = new JTextField();
        sNameTxt.setPreferredSize(new Dimension(250,30));

        phoneLbl = new JLabel("phone number");
        phoneTxt = new JTextField();
        phoneTxt.setPreferredSize(new Dimension(250,30));

        viewBtn = new JButton("view");
        viewBtn.setFocusable(false);
        viewBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewAtOpen();
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

        C_PBtn = new JButton("parent/child");
        C_PBtn.setFocusable(false);
        C_PBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                try {

                    clearTable();// method to clear table

                    Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/finance", "root", "aee?1Mkad");
                    Statement statement = connection.createStatement();
                    // above 2 lines connect to database
                    ResultSet resultSet = statement.executeQuery("select parent.first_name, parent.surname, student.first_name, student.surname\n" +
                            "from parent\n" +
                            "inner join student \n" +
                            "on parent.ID_number = student.parent ");
                    ResultSetMetaData rsmd  = resultSet.getMetaData();
                    DefaultTableModel model = (DefaultTableModel) table.getModel();
                    // reading columns
                    int cols = rsmd.getColumnCount();
                    String[] colname = new String[cols];
                    for (int i=0; i<cols;i++)
                        colname[i] = rsmd.getColumnName(i+1);
                    model.setColumnIdentifiers(colname);

                    // reading rows
                    String pFname, pSname, cFname,cSname;
                    while (resultSet.next()) {

                        pFname = resultSet.getString(1);
                        pSname = resultSet.getString(2);
                        cFname = resultSet.getString(3);
                        cSname = resultSet.getString(4);

                        String[] row = {pFname,pSname,cFname,cSname};
                        model.addRow(row);
                    }
                    statement.close();
                    connection.close();

                } catch (Exception ex){
                    ex.printStackTrace();
                }
            }
        });

        backBtn = new JButton("back");
        backBtn.setFocusable(false);
        backBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                student st = new student();
                frame.dispose();
            }
        });

        panel = new JPanel();
        panel.setPreferredSize(new Dimension(650,100));
        panel.setLayout(new FlowLayout(FlowLayout.LEFT,3,10));
        panel.setBackground(Color.CYAN);

        panel.add(idNumLbl);
        panel.add(idNumTxt);
        panel.add(fNameLbl);
        panel.add(fNameTxt);
        panel.add(sNameLbl);
        panel.add(sNameTxt);
        panel.add(phoneLbl);
        panel.add(phoneTxt);
        frame.add(viewBtn);
        frame.add(saveBtn);
        frame.add(deleteBtn);
        frame.add(editBtn);
        frame.add(clearBtn);
        frame.add(C_PBtn);
        frame.add(backBtn);

        JScrollPane pane = new JScrollPane(table);
        pane.setPreferredSize(new Dimension(650,300));
        frame.add(panel);
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
            ResultSet resultSet = statement.executeQuery("select * from parent where first_name like '%"+fNameTxt.getText()+"%'");
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
            String reg_number, first_name, surname,stClass, DOB;
            while (resultSet.next()) {

                reg_number = resultSet.getString(1);
                first_name = resultSet.getString(2);
                surname = resultSet.getString(3);
                stClass = resultSet.getString(4);
                DOB = resultSet.getString(5);

                String[] row = {reg_number,first_name,surname,stClass,DOB};
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
        sNameTxt.setText("");
        fNameTxt.setText("");
        phoneTxt.setText("");
        idNumTxt.setText("");
    }

    @Override
    public void update() {

    }

    @Override
    public void Edit() {

        if (fNameTxt.getText().isEmpty()|| sNameTxt.getText().isEmpty()||phoneTxt.getText().isEmpty()) {

            JOptionPane.showMessageDialog(frame,"error! select row in the table to edit");

        }

        else{

            try {

                //clearTable();

                DefaultTableModel model = (DefaultTableModel) table.getModel();
                int selectedIndex = table.getSelectedRow();

                //int regstration_number = Integer.parseInt(model.getValueAt(selectedIndex,0).toString());
                String regstration_number = (model.getValueAt(selectedIndex,0).toString());

                String first_name, surname, phone;

                first_name = sNameTxt.getText();
                surname = fNameTxt.getText();
                phone = phoneTxt.getText();

                Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/finance", "root", "aee?1Mkad");
                PreparedStatement pst = connection.prepareStatement("update parent set first_name=?,surname=?,phone_number=? where ID_number =? ");
                pst.setString(4,regstration_number);
                pst.setString(1,first_name);
                pst.setString(2,surname);
                pst.setString(3,phone);

                pst.executeUpdate();
                JOptionPane.showMessageDialog(frame,"record edited");
                viewAtOpen();
                clearTxtFields();

                idNumTxt.requestFocus();// brings back focus to reg text field


            }catch (Exception ex){
                ex.printStackTrace();
            }
            clearTable();
            viewAtOpen();

        }
    }

    @Override
    public void Delete() {

        if (fNameTxt.getText().isEmpty()|| sNameTxt.getText().isEmpty()||phoneTxt.getText().isEmpty()) {

            JOptionPane.showMessageDialog(frame,"error! select row in the table to delete");

        }

        else {

            try {

                DefaultTableModel model = (DefaultTableModel) table.getModel();
                int selectedIndex = table.getSelectedRow();

                int ID_number = Integer.parseInt(model.getValueAt(selectedIndex,0).toString());
                Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/finance", "root", "aee?1Mkad");
                PreparedStatement pst = connection.prepareStatement("delete from parent where ID_number=?");
                pst.setInt(1,ID_number);

                pst.executeUpdate();
                JOptionPane.showMessageDialog(frame,"record deleted");
                viewAtOpen();

                //clearing the txt fields
                clearTxtFields();
                idNumTxt.requestFocus();// brings back focus to reg text field

            }catch (Exception ex){
                ex.printStackTrace();
            }
            clearTable();
            viewAtOpen();

        }

    }

    @Override
    public void Save() {
        if (fNameTxt.getText().isEmpty()|| sNameTxt.getText().isEmpty()||phoneTxt.getText().isEmpty()) {

            JOptionPane.showMessageDialog(frame,"error! fill all fields");
        }

        else {

            try {

                clearTable();

                String ID,phone, first_name, surname;
                phone = phoneTxt.getText();
                first_name = fNameTxt.getText();
                surname = sNameTxt.getText();
                ID = idNumTxt.getText();


                Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/finance", "root", "aee?1Mkad");
                PreparedStatement pst = connection.prepareStatement("insert into parent (ID_number,first_name,surname,phone_number)values (?,?,?,?)");
                pst.setString(4,phone);
                pst.setString(2,first_name);
                pst.setString(3,surname);
                pst.setString(1,ID);



                pst.executeUpdate();
                JOptionPane.showMessageDialog(frame,"record saved");
                viewAtOpen();

                //clearing the txt fields
                clearTxtFields();

                fNameTxt.requestFocus();// brings back focus to reg text field


            }catch (Exception ex){
                ex.printStackTrace();
            }

        }
    }

    @Override
    public void viewAtOpen() {
        try {
            clearTable();

            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/finance", "root", "aee?1Mkad");
            Statement statement = connection.createStatement();
            // above 2 lines connect to database
            ResultSet resultSet = statement.executeQuery("select * from parent ");
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
