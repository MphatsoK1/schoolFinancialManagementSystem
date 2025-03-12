import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;


public class student implements Methods {
    JFrame frame;
    JTextField regField,fnameField,surField,ClassField,DOBField,locationField, DOriginField,feesField, searchNameField,updateTxt,
            updateTxt2, sexTxt, code_numTxt,orphanTxt,parentTxt, religionTxt;
    JLabel regLabel,fnameLabel,surLabel,ClassLabel,DOBLabel,locationLabel, DOriginLabel,feesLabel, searchLabel,classTotalLabel, stTotalLabel, searchNameLabel,clsFeesLabel,
            sexLabel, code_numLabel,orphanLabel,parentLabel, religionLabel, addParentLabel, directionLabel;
    JPanel panel, panel2, panel3;
    JTable table, table2;
    JButton paidSt,balSt,payList,balList, view, save,delete,back,edit, clearTable2btn,clearTXt, updateBtn, exportbtn;

    JComboBox<String> classBox;
    JComboBox<String> totalsBox;
    JComboBox<String> stNumBox;
    student(){

        table = new JTable();
        table.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                DefaultTableModel model = (DefaultTableModel) table.getModel();
                int selectedIndex = table.getSelectedRow();

                regField.setText(model.getValueAt(selectedIndex,0).toString());
                fnameField.setText(model.getValueAt(selectedIndex, 1).toString());
                surField.setText(model.getValueAt(selectedIndex,2).toString());
                ClassField.setText(model.getValueAt(selectedIndex,3).toString());
                DOBField.setText(model.getValueAt(selectedIndex,4).toString());
                locationField.setText(model.getValueAt(selectedIndex,5).toString());
                DOriginField.setText(model.getValueAt(selectedIndex,6).toString());
                feesField.setText(model.getValueAt(selectedIndex,7).toString());
                sexTxt.setText(model.getValueAt(selectedIndex,8).toString());
                code_numTxt.setText(model.getValueAt(selectedIndex,9).toString());
                orphanTxt.setText(model.getValueAt(selectedIndex,10).toString());
                parentTxt.setText(model.getValueAt(selectedIndex,11).toString());
                religionTxt.setText(model.getValueAt(selectedIndex,12).toString());

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

        table2 = new JTable();
        table2.setBackground(Color.white);
        table2.setForeground(Color.black);
        table2.setRowHeight(30);
        table2.setAutoCreateRowSorter(true);
        table2.setFont(new Font(null,Font.PLAIN,15));
        table2.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                DefaultTableModel model = (DefaultTableModel) table2.getModel();
                int selectedIndex = table2.getSelectedRow();

                updateTxt2.setText(model.getValueAt(selectedIndex,0).toString());
                updateTxt.setText(model.getValueAt(selectedIndex, 1).toString());

            }
            @Override
            public void mousePressed(MouseEvent e) {}
            @Override
            public void mouseReleased(MouseEvent e) {}
            @Override
            public void mouseEntered(MouseEvent e) {}
            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
        table2.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                if (e.getKeyCode()==10){ // this is when enter key is used
                    DefaultTableModel model = (DefaultTableModel) table2.getModel();
                    int selectedIndex = table2.getSelectedRow();

                    regField.setText(model.getValueAt(selectedIndex,0).toString());
                    fnameField.setText(model.getValueAt(selectedIndex, 1).toString());
                    surField.setText(model.getValueAt(selectedIndex,2).toString());
                    ClassField.setText(model.getValueAt(selectedIndex,3).toString());
                    DOBField.setText(model.getValueAt(selectedIndex,4).toString());
                    locationField.setText(model.getValueAt(selectedIndex,5).toString());
                    DOriginField.setText(model.getValueAt(selectedIndex,6).toString());
                    feesField.setText(model.getValueAt(selectedIndex,7).toString());
                    sexTxt.setText(model.getValueAt(selectedIndex,8).toString());
                    code_numTxt.setText(model.getValueAt(selectedIndex,9).toString());
                    orphanTxt.setText(model.getValueAt(selectedIndex,10).toString());
                    parentTxt.setText(model.getValueAt(selectedIndex,11).toString());
                    religionTxt.setText(model.getValueAt(selectedIndex,12).toString());
                }
            }
        });

        frame = new JFrame();
        viewAtOpen();

        String[] stNum = {"Baby", "Lower", "Middle", "Reception", "Standard 1","Standard 2","Standard 3","Standard 4"};
        stNumBox = new JComboBox<>(stNum);
        stNumBox.addActionListener(e -> {
            try {
                 // method to clear table
                clearTable2();

                Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/finance", "root", "aee?1Mkad");

                String sql = "SELECT count(regstration_number) as Number_Of_Students from student WHERE class = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, (String) stNumBox.getSelectedItem());

                ResultSet resultSet = preparedStatement.executeQuery();
                ResultSetMetaData rsmd = resultSet.getMetaData();
                DefaultTableModel model = (DefaultTableModel) table2.getModel();

                // reading columns
                int cols = rsmd.getColumnCount();
                String[] colname = new String[cols];
                for (int i = 0; i < cols; i++)
                    colname[i] = rsmd.getColumnName(i + 1);
                model.setColumnIdentifiers(colname);

                String total;
                while (resultSet.next()) {

                    total = resultSet.getString(1);

                    String[] row = {total};
                    model.addRow(row);
                }

                preparedStatement.close();
                connection.close();

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        String[] classTotal = {"Baby", "Lower", "Middle", "Reception", "Standard 1","Standard 2","Standard 3","Standard 4"};
        totalsBox = new JComboBox<>(classTotal);
        totalsBox.addActionListener(e -> {
            try {
                clearTable2(); // method to clear table

                Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/finance", "root", "aee?1Mkad");

                String sql = "SELECT sum(fees_paid) as PaidFeesTotal from student WHERE class = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, (String) totalsBox.getSelectedItem());

                ResultSet resultSet = preparedStatement.executeQuery();
                ResultSetMetaData rsmd = resultSet.getMetaData();
                DefaultTableModel model = (DefaultTableModel) table2.getModel();

                // reading columns
                int cols = rsmd.getColumnCount();
                String[] colname = new String[cols];
                for (int i = 0; i < cols; i++)
                    colname[i] = rsmd.getColumnName(i + 1);
                model.setColumnIdentifiers(colname);

                String total;
                while (resultSet.next()) {

                    total = resultSet.getString(1);

                    String[] row = {total};
                    model.addRow(row);
                }

                preparedStatement.close();
                connection.close();

            } catch (Exception ex) {
                ex.printStackTrace();
            }

        });

        String[] year = {"Baby", "Lower", "Middle", "Reception", "Standard 1","Standard 2","Standard 3","Standard 4"};
        classBox = new JComboBox<>(year);
        //classBox.setBounds(1000,1000,100,100);
        classBox.addActionListener(e -> { // lambda instead of new ActionListner ();
            try {
                clearTable(); // method to clear table

                Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/finance", "root", "aee?1Mkad");

                String sql = "SELECT * FROM student WHERE class = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, (String) classBox.getSelectedItem());

                ResultSet resultSet = preparedStatement.executeQuery();
                ResultSetMetaData rsmd = resultSet.getMetaData();
                DefaultTableModel model = (DefaultTableModel) table.getModel();

                // reading columns
                int cols = rsmd.getColumnCount();
                String[] colname = new String[cols];
                for (int i = 0; i < cols; i++)
                    colname[i] = rsmd.getColumnName(i + 1);
                model.setColumnIdentifiers(colname);

                // reading rows
                while (resultSet.next()) {
                    String reg_number = resultSet.getString(1);
                    String first_name = resultSet.getString(2);
                    String surname = resultSet.getString(3);
                    String stClass = resultSet.getString(4);
                    String DOB = resultSet.getString(5);
                    String location = resultSet.getString(6);
                    String DOrigin = resultSet.getString(7);
                    String fees = resultSet.getString(8);
                    String sex = resultSet.getString(9);
                    String code = resultSet.getString(10);
                    String orphan = resultSet.getString(11);
                    String parent = resultSet.getString(12);
                    String religion = resultSet.getString(13);

                    String[] row = {reg_number, first_name, surname, stClass, DOB, location, DOrigin, fees,sex, code,orphan,parent,religion};
                    model.addRow(row);
                }

                preparedStatement.close();
                connection.close();

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });


        //first panel
        regField = new JTextField();
        regField.setPreferredSize(new Dimension(250,30));

        fnameField = new JTextField();
        fnameField.setPreferredSize(new Dimension(250,30));

        DOBField = new JTextField();
        DOBField.setPreferredSize(new Dimension(250,30));

        locationField = new JTextField();
        locationField.setPreferredSize(new Dimension(250,30));

        DOriginField = new JTextField();
        DOriginField.setPreferredSize(new Dimension(250,30));

        surField = new JTextField();
        surField.setPreferredSize(new Dimension(250,30));

        ClassField = new JTextField();
        ClassField.setPreferredSize(new Dimension(250,30));

        feesField = new JTextField();
        feesField.setPreferredSize(new Dimension(250,30));

        updateTxt = new JTextField();
        updateTxt.setPreferredSize(new Dimension(100,30));

        sexTxt = new JTextField();
        sexTxt.setPreferredSize(new Dimension(100,30));

        code_numTxt = new JTextField();
        code_numTxt.setPreferredSize(new Dimension(100,30));

        orphanTxt = new JTextField();
        orphanTxt.setPreferredSize(new Dimension(100,30));

        parentTxt = new JTextField();
        parentTxt.setPreferredSize(new Dimension(100,30));

        religionTxt = new JTextField();
        religionTxt.setPreferredSize(new Dimension(100,30));

        updateTxt2 = new JTextField();
        updateTxt2.setPreferredSize(new Dimension(100,30));
        updateTxt2.setEditable(false);

        searchNameField = new JTextField();
        searchNameField.setPreferredSize(new Dimension(250,30));
        searchNameField.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {}

            @Override
            public void keyPressed(KeyEvent e) {}

            @Override
            public void keyReleased(KeyEvent e) {
                Searching();
            }
        });


        //buttons
        payList = new JButton("paid list");
        payList.setFocusable(false);
        payList.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {

                    clearTable();// method to clear table

                    Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/finance", "root", "aee?1Mkad");
                    Statement statement = connection.createStatement();
                    // above 2 lines connect to database
                    ResultSet resultSet = statement.executeQuery("select * from student where fees_paid != 0");
                    ResultSetMetaData rsmd  = resultSet.getMetaData();
                    DefaultTableModel model = (DefaultTableModel) table.getModel();
                    // reading columns
                    int cols = rsmd.getColumnCount();
                    String[] colname = new String[cols];
                    for (int i=0; i<cols;i++)
                        colname[i] = rsmd.getColumnName(i+1);
                    model.setColumnIdentifiers(colname);

                    // reading rows
                    String reg_number, first_name, surname,stClass, DOB, location, DOrigin, fees, sex,code,orphan,parent, religion;
                    while (resultSet.next()) {

                        reg_number = resultSet.getString(1);
                        first_name = resultSet.getString(2);
                        surname = resultSet.getString(3);
                        stClass = resultSet.getString(4);
                        DOB = resultSet.getString(5);
                        location = resultSet.getString(6);
                        DOrigin = resultSet.getString(7);
                        fees = resultSet.getString(8);
                        sex = resultSet.getString(9);
                        code = resultSet.getString(10);
                        orphan = resultSet.getString(11);
                        parent = resultSet.getString(12);
                        religion = resultSet.getString(13);

                        String[] row = {reg_number,first_name,surname,stClass,DOB,location,DOrigin,fees, sex, code, orphan, parent, religion};
                        model.addRow(row);
                    }
                    statement.close();
                    connection.close();

                } catch (Exception ex){
                    ex.printStackTrace();
                }

            }

        });

        balSt = new JButton("total balance");
        balSt.setFocusable(false);
        balSt.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {

                    clearTable2();// method to clear table

                    Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/finance", "root", "aee?1Mkad");
                    Statement statement = connection.createStatement();
                    // above 2 lines connect to database
                    ResultSet resultSet = statement.executeQuery("select sum(fees_paid) - sum(total_fees) as Balance from student inner join class_info on student.class = class_info.class");
                    ResultSetMetaData rsmd  = resultSet.getMetaData();
                    DefaultTableModel model = (DefaultTableModel) table2.getModel();
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

        paidSt = new JButton("total paid ");
        paidSt.setFocusable(false);
        paidSt.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                try {

                    clearTable2();// method to clear table

                    Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/finance", "root", "aee?1Mkad");
                    Statement statement = connection.createStatement();
                    // above 2 lines connect to database
                    ResultSet resultSet = statement.executeQuery("select sum(fees_paid) as Total from student");
                    ResultSetMetaData rsmd  = resultSet.getMetaData();
                    DefaultTableModel model = (DefaultTableModel) table2.getModel();
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


        balList = new JButton("balance list");
        balList.setFocusable(false);
        balList.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {


                try {

                    clearTable();// method to clear table

                    Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/finance", "root", "aee?1Mkad");
                    Statement statement = connection.createStatement();
                    // above 2 lines connect to database
                    ResultSet resultSet = statement.executeQuery("select student.regstration_number,first_name,surname,(class_info.total_fees - student.fees_paid) as Balance from student inner join class_info on student.class = class_info.class");
                    ResultSetMetaData rsmd  = resultSet.getMetaData();
                    DefaultTableModel model = (DefaultTableModel) table.getModel();
                    // reading columns
                    int cols = rsmd.getColumnCount();
                    String[] colname = new String[cols];
                    for (int i=0; i<cols;i++)
                        colname[i] = rsmd.getColumnName(i+1);
                    model.setColumnIdentifiers(colname);

                    // reading rows
                    String reg_number, first_name, surname,Balance;
                    while (resultSet.next()) {

                        reg_number = resultSet.getString(1);
                        first_name = resultSet.getString(2);
                        surname = resultSet.getString(3);
                        Balance = resultSet.getString(4);

                        String[] row = {reg_number,first_name,surname,Balance};
                        model.addRow(row);
                    }
                    statement.close();
                    connection.close();

                } catch (Exception ex){
                    ex.printStackTrace();
                }

            }
        });



        view = new JButton("view");
        view.setFocusable(false);
        view.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearTable();
                viewAtOpen();
                numberOfStudentsInClass();
            }
        });

        save = new JButton("add");
        save.setFocusable(false);
        save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {Save(); // calling the save method
            }
        });

        delete = new JButton("delete");
        delete.setFocusable(false);
        delete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {Delete();} // calling delete method
        });

        edit = new JButton("edit");
        edit.setFocusable(false);
        edit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {Edit();}// calling the edit method
        });

        updateBtn = new JButton("update");
        updateBtn.setFocusable(false);
        updateBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {update();} // calling the update method
        });

        back = new JButton("back");
        back.setFocusable(false);
        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Finance fin = new Finance();
                frame.dispose();
            }
        });

        clearTable2btn = new JButton("clear");
        clearTable2btn.setFocusable(false);
        clearTable2btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearTable2();
            }
        });

        clearTXt = new JButton("clear");
        clearTXt.setFocusable(false);
        clearTXt.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearTxtFields();
            }
        });

        exportbtn = new JButton("export");
        exportbtn.setFocusable(false);
        exportbtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                writeToFile();
            }
        });

        // first table's pane
        JScrollPane pane = new JScrollPane(table);
        pane.setPreferredSize(new Dimension(900,360));
        //Object[] row = new Object[6];

        // second table's pane
        JScrollPane pane2 = new JScrollPane(table2);
        pane2.setPreferredSize(new Dimension(900,200));


        // Labels of first panel
        regLabel = new JLabel("Registration Number");
        fnameLabel = new JLabel("First Name");
        surLabel = new JLabel("Surname");
        ClassLabel = new JLabel("Class");
        DOBLabel = new JLabel("Date of Birth(YR-MTH-DD)");
        locationLabel = new JLabel("Location");
        DOriginLabel = new JLabel("District of origin");
        feesLabel = new JLabel("Fees Paid");
        searchLabel = new JLabel("Search class");
        classTotalLabel = new JLabel("class fees total");
        stTotalLabel = new JLabel("number of students");
        searchNameLabel = new JLabel("search first name");
        clsFeesLabel = new JLabel(" click to view required fees per student in a class");
        clsFeesLabel.setForeground(Color.red);
        sexLabel = new JLabel("sex");
        code_numLabel = new JLabel("code number");
        orphanLabel = new JLabel("Orphanhood");
        parentLabel = new JLabel("Parent/Guardian ID number");
        religionLabel = new JLabel("religion");
        addParentLabel = new JLabel("click to add parent info");
        directionLabel = new JLabel("register a student below");
        addParentLabel.setForeground(Color.red);
        addParentLabel.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Parent parent = new Parent();
                frame.dispose();
            }
            @Override
            public void mousePressed(MouseEvent e) {}
            @Override
            public void mouseReleased(MouseEvent e) {}
            @Override
            public void mouseEntered(MouseEvent e) {}
            @Override
            public void mouseExited(MouseEvent e) {
            }
        });
        clsFeesLabel.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                feesClass();
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


        panel = new JPanel();
        panel.setLayout(new GridLayout(14,2,5,10));
//        panel.setPreferredSize(new Dimension(280,600));
        panel.setPreferredSize(new Dimension(310,600));
        panel.setBackground(Color.CYAN);


        panel.add(addParentLabel);
        panel.add(directionLabel);

        panel.add(regLabel);
        panel.add(regField);

        panel.add(fnameLabel);
        panel.add(fnameField);

        panel.add(surLabel);
        panel.add(surField);

        panel.add(ClassLabel);
        panel.add(ClassField);

        panel.add(DOBLabel);
        panel.add(DOBField);

        panel.add(locationLabel);
        panel.add(locationField);

        panel.add(DOriginLabel);
        panel.add(DOriginField);

        panel.add(feesLabel);
        panel.add(feesField);

        panel.add(sexLabel);
        panel.add(sexTxt);

        panel.add(code_numLabel);
        panel.add(code_numTxt);

        panel.add(orphanLabel);
        panel.add(orphanTxt);

        panel.add(parentLabel);
        panel.add(parentTxt);

        panel.add(religionLabel);
        panel.add(religionTxt);

        //panel.add(addParentLabel);

        /*panel.add(clearTXt);
        panel.add(save);
        panel.add(edit);
        panel.add(view);
        panel.add(delete);
        panel.add(paidSt);
        panel.add(payList);
        panel.add(balSt);
        panel.add(balList);
        panel.add(back);*/

        panel3 = new JPanel(new FlowLayout());
//        panel3.setPreferredSize(new Dimension(900,690));
        panel3.setPreferredSize(new Dimension(920,690));
        panel3.setBackground(Color.white);
        panel3.add(searchLabel);
        panel3.add(classBox);
        panel3.add(pane);
        panel3.add(classTotalLabel);
        panel3.add(totalsBox);
        panel3.add(stTotalLabel);
        panel3.add(stNumBox);
        panel3.add(searchNameLabel);
        panel3.add(searchNameField);
        panel3.add(clearTable2btn);
        panel3.add(clsFeesLabel);

        panel3.add(updateTxt2);
        panel3.add(updateTxt);
        panel3.add(updateBtn);
        panel3.add(pane2);
        // the labels

        panel2 = new JPanel();

        //panel2.setPreferredSize(new Dimension(1400,730));
        panel2.setBackground(Color.CYAN);
        panel2.add(panel);
        panel2.add(panel3);

        frame.add(clearTXt);
        frame.add(save);
        frame.add(edit);
        frame.add(view);
        frame.add(delete);
        frame.add(paidSt);
        frame.add(payList);
        frame.add(balSt);
        frame.add(balList);
        frame.add(exportbtn);
        frame.add(back);

        frame.add(panel2);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout( new FlowLayout(FlowLayout.CENTER, 30,10));
        //frame.setBounds(400,100,700,600);
        frame.pack();
        frame.setVisible(true);

    }

    @Override
    public void clearTable(){
        table.setModel(new DefaultTableModel());
    }

    public void clearTable2(){
        table2.setModel(new DefaultTableModel());
    }

    public void numberOfStudentsInClass(){
        try {

            clearTable2();// method to clear table

            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/finance", "root", "aee?1Mkad");
            Statement statement = connection.createStatement();
            // above 2 lines connect to database
            ResultSet resultSet = statement.executeQuery("select count(regstration_number) from student");
            ResultSetMetaData rsmd  = resultSet.getMetaData();
            DefaultTableModel model = (DefaultTableModel) table2.getModel();
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

    @Override
    public void Searching(){

        try {

            clearTable2();

            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/finance", "root", "aee?1Mkad");
            Statement statement = connection.createStatement();
            // above 2 lines connect to database
            ResultSet resultSet = statement.executeQuery("select * from student where first_name like '%"+searchNameField.getText()+"%'");
            //ResultSet resultSet = statement.executeQuery("select * from student where first_name R");
            ResultSetMetaData rsmd  = resultSet.getMetaData();
            DefaultTableModel model = (DefaultTableModel) table2.getModel();
            // reading columns
            int cols = rsmd.getColumnCount();
            String[] colname = new String[cols];
            for (int i=0; i<cols;i++)
                colname[i] = rsmd.getColumnName(i+1);
            model.setColumnIdentifiers(colname);

            // reading rows
            String reg_number, first_name, surname,stClass, DOB, location, DOrigin, fees,sex, code,orphan,parent,religion;
            while (resultSet.next()) {

                reg_number = resultSet.getString(1);
                first_name = resultSet.getString(2);
                surname = resultSet.getString(3);
                stClass = resultSet.getString(4);
                DOB = resultSet.getString(5);
                location = resultSet.getString(6);
                DOrigin = resultSet.getString(7);
                fees = resultSet.getString(8);
                sex = resultSet.getString(9);
                code = resultSet.getString(10);
                orphan = resultSet.getString(11);
                parent = resultSet.getString(12);
                religion = resultSet.getString(13);


                String[] row = {reg_number,first_name,surname,stClass,DOB,location,DOrigin,fees,sex,code,orphan,parent,religion};
                model.addRow(row);
            }
            statement.close();
            connection.close();

        } catch (Exception ex){
            ex.printStackTrace();
        }

    }

    @Override
    public void clearTxtFields(){

        //clearing the txt fields
        regField.setText("");
        fnameField.setText("");
        surField.setText("");
        DOBField.setText("");
        locationField.setText("");
        DOriginField.setText("");
        feesField.setText("");
        ClassField.setText("");
        sexTxt.setText("");
        code_numTxt.setText("");
        orphanTxt.setText("");
        parentTxt.setText("");
        religionTxt.setText("");
        regField.requestFocus();// brings back focus to reg text field

    }

    public void feesClass(){

        try {
            clearTable2();

            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/finance", "root", "aee?1Mkad");
            Statement statement = connection.createStatement();
            // above 2 lines connect to database
            ResultSet resultSet = statement.executeQuery("select * from class_info ");
            ResultSetMetaData rsmd  = resultSet.getMetaData();
            DefaultTableModel model = (DefaultTableModel) table2.getModel();
            // reading columns
            int cols = rsmd.getColumnCount();
            String[] colname = new String[cols];
            for (int i=0; i<cols;i++)
                colname[i] = rsmd.getColumnName(i+1);
            model.setColumnIdentifiers(colname);

            // reading rows
            String reg_number, first_name;
            while (resultSet.next()) {

                reg_number = resultSet.getString(1);
                first_name = resultSet.getString(2);

                String[] row = {reg_number,first_name};
                model.addRow(row);
            }
            statement.close();
            connection.close();

        } catch (Exception ex){
            ex.printStackTrace();
        }

    }

    @Override
    public void update(){
        if (updateTxt2.getText().isEmpty()|| updateTxt.getText().isEmpty()) {

            JOptionPane.showMessageDialog(frame,"error! select row in the table to edit");

        }

        else{

            try {

                //clearTable();
                DefaultTableModel model = (DefaultTableModel) table2.getModel();
                int selectedIndex = table2.getSelectedRow();

                String theClass = model.getValueAt(selectedIndex,0).toString();
                String theFees;
                theFees = updateTxt.getText();

                Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/finance", "root", "aee?1Mkad");
                PreparedStatement pst = connection.prepareStatement("update class_info set total_fees=? where class=?");
                pst.setString(1,theFees);
                pst.setString(2,theClass);


                pst.executeUpdate();
                JOptionPane.showMessageDialog(frame,"record edited");
                viewAtOpen();

                //clearing the txt fields
                updateTxt.setText("");
                updateTxt2.setText("");
                //regField.requestFocus();// brings back focus to reg text field



            }catch (Exception ex){
                ex.printStackTrace();
            }
            clearTable2();
            feesClass();

        }

    }

    @Override
    public void Edit(){
        if (regField.getText().isEmpty()|| fnameField.getText().isEmpty()||surField.getText().isEmpty()||DOBField.getText().isEmpty()
                ||locationField.getText().isEmpty()||DOriginField.getText().isEmpty()||
                feesField.getText().isEmpty()||ClassField.getText().isEmpty()|| sexTxt.getText().isEmpty()||code_numTxt.getText().isEmpty()||
                orphanTxt.getText().isEmpty()||parentTxt.getText().isEmpty()||religionTxt.getText().isEmpty()) {

            JOptionPane.showMessageDialog(frame,"error! select row in the table to edit");

        }

        else{

            try {

                //clearTable();

                DefaultTableModel model = (DefaultTableModel) table.getModel();
                int selectedIndex = table.getSelectedRow();

                int regstration_number = Integer.parseInt(model.getValueAt(selectedIndex,0).toString());

                String first_name, surname,stClass, DOB, location, DOrigin, fees,sex,code,orphan,parent, religion;

                first_name = fnameField.getText();
                surname = surField.getText();
                DOB = DOBField.getText();
                location = locationField.getText();
                DOrigin = DOriginField.getText();
                fees = feesField.getText();
                stClass = ClassField.getText();
                sex = sexTxt.getText();
                code = code_numTxt.getText();
                orphan = orphanTxt.getText();
                parent = parentTxt.getText();
                religion = religionTxt.getText();

                Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/finance", "root", "aee?1Mkad");
                PreparedStatement pst = connection.prepareStatement("update student set first_name=?,surname=?,class=?,date_of_birth=?,location=?,District_of_orign=?,fees_paid=?,sex=?,code_number=?,orphanhood=?,parent=?,religion=? where regstration_number=?");
                pst.setInt(13,regstration_number);
                pst.setString(1,first_name);
                pst.setString(2,surname);
                pst.setString(3,stClass);
                pst.setString(4,DOB);
                pst.setString(5,location);
                pst.setString(6,DOrigin);
                pst.setString(7,fees);
                pst.setString(8,sex);
                pst.setString(9,code);
                pst.setString(10,orphan);
                pst.setString(11,parent);
                pst.setString(12,religion);

                pst.executeUpdate();
                JOptionPane.showMessageDialog(frame,"record edited");
                viewAtOpen();
                clearTxtFields();

                regField.requestFocus();// brings back focus to reg text field


            }catch (Exception ex){
                ex.printStackTrace();
            }
            clearTable();
            viewAtOpen();

        }
    }

    @Override
    public void Delete(){
        if (regField.getText().isEmpty()|| fnameField.getText().isEmpty()||surField.getText().isEmpty()||DOBField.getText().isEmpty()
                ||locationField.getText().isEmpty()||DOriginField.getText().isEmpty()||
                feesField.getText().isEmpty()||ClassField.getText().isEmpty()|| sexTxt.getText().isEmpty()||code_numTxt.getText().isEmpty()||
                orphanTxt.getText().isEmpty()||parentTxt.getText().isEmpty()||religionTxt.getText().isEmpty()) {

            JOptionPane.showMessageDialog(frame,"error! select row in the table to delete");

        }

        else {

            try {

                DefaultTableModel model = (DefaultTableModel) table.getModel();
                int selectedIndex = table.getSelectedRow();

                int regstration_number = Integer.parseInt(model.getValueAt(selectedIndex,0).toString());
                Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/finance", "root", "aee?1Mkad");
                PreparedStatement pst = connection.prepareStatement("delete from student where regstration_number=?");
                pst.setInt(1,regstration_number);

                pst.executeUpdate();
                JOptionPane.showMessageDialog(frame,"record deleted");
                viewAtOpen();

                //clearing the txt fields
                clearTxtFields();
                regField.requestFocus();// brings back focus to reg text field


            }catch (Exception ex){
                ex.printStackTrace();
            }
            clearTable();
            viewAtOpen();

        }
    }

    @Override
    public void Save(){
        if (regField.getText().isEmpty()|| fnameField.getText().isEmpty()||surField.getText().isEmpty()||DOBField.getText().isEmpty()
                ||locationField.getText().isEmpty()||DOriginField.getText().isEmpty()||
                feesField.getText().isEmpty()||ClassField.getText().isEmpty()|| sexTxt.getText().isEmpty()||code_numTxt.getText().isEmpty()||
                orphanTxt.getText().isEmpty()||parentTxt.getText().isEmpty()||religionTxt.getText().isEmpty()) {

            JOptionPane.showMessageDialog(frame,"error! fill all fields");
        }

        else {

            try {

                clearTable();

                String reg_number, first_name, surname,stClass, DOB, location, DOrigin, fees,sex, code,orphan,parent,religion;
                reg_number = regField.getText();
                first_name = fnameField.getText();
                surname = surField.getText();
                DOB = DOBField.getText();
                location = locationField.getText();
                DOrigin = DOriginField.getText();
                fees = feesField.getText();
                stClass = ClassField.getText();
                sex = sexTxt.getText();
                code = code_numTxt.getText();
                orphan = orphanTxt.getText();
                parent = parentTxt.getText();
                religion = religionTxt.getText();

                Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/finance", "root", "aee?1Mkad");
                PreparedStatement pst = connection.prepareStatement("insert into student(regstration_number,first_name,surname,class,date_of_birth,location,District_of_orign,fees_paid,sex,code_number,orphanhood,parent,religion)values (?,?,?,?,?,?,?,?,?,?,?,?,?)");
                pst.setString(1,reg_number);
                pst.setString(2,first_name);
                pst.setString(3,surname);
                pst.setString(4,stClass);
                pst.setString(5,DOB);
                pst.setString(6,location);
                pst.setString(7,DOrigin);
                pst.setString(8,fees);
                pst.setString(9,sex);
                pst.setString(10,code);
                pst.setString(11,orphan);
                pst.setString(12,parent);
                pst.setString(13,religion);

                pst.executeUpdate();
                JOptionPane.showMessageDialog(frame,"record saved");
                viewAtOpen();

                //clearing the txt fields
             clearTxtFields();

                regField.requestFocus();// brings back focus to reg text field


            }catch (Exception ex){
                ex.printStackTrace();
            }

        }
    }

    @Override
    public void viewAtOpen(){

        try {

            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/finance", "root", "aee?1Mkad");
            Statement statement = connection.createStatement();
            // above 2 lines connect to database
            ResultSet resultSet = statement.executeQuery("select * from student order by class");
            ResultSetMetaData rsmd  = resultSet.getMetaData();
            DefaultTableModel model = (DefaultTableModel) table.getModel();
            // reading columns
            int cols = rsmd.getColumnCount();
            String[] colname = new String[cols];
            for (int i=0; i<cols;i++)
                colname[i] = rsmd.getColumnName(i+1);
            model.setColumnIdentifiers(colname);

            // reading rows
            String reg_number, first_name, surname,stClass, DOB, location, DOrigin, fees, sex, code,orphan,parent,religion;
            while (resultSet.next()) {

                reg_number = resultSet.getString(1);
                first_name = resultSet.getString(2);
                surname = resultSet.getString(3);
                stClass = resultSet.getString(4);
                DOB = resultSet.getString(5);
                location = resultSet.getString(6);
                DOrigin = resultSet.getString(7);
                fees = resultSet.getString(8);
                sex = resultSet.getString(9);
                code = resultSet.getString(10);
                orphan = resultSet.getString(11);
                parent = resultSet.getString(12);
                religion = resultSet.getString(13);

                String[] row = {reg_number,first_name,surname,stClass,DOB,location,DOrigin,fees,sex,code,orphan,parent,religion};
                model.addRow(row);
            }
            statement.close();
            connection.close();

        } catch (Exception ex){
            ex.printStackTrace();
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

}
