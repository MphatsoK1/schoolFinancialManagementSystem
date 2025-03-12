import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Finance {
    public static void main (String[] args) {

        JFrame frame = new JFrame("FINANCE MANAGEMENT SYSTEM");

        JPanel panel = new JPanel();

        //panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        //panel.setBounds(1500,500,500,500);
        //panel.setPreferredSize(new Dimension(350, 50));
        panel.setLayout(new FlowLayout(FlowLayout.CENTER,20,20));


        JButton stButton = new JButton("STUDENT");
        stButton.setFocusable(false);
        stButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                student st = new student();
            }
        });

        JButton empButton = new JButton("EMPLOYEE");
        empButton.setFocusable(false);

        JButton dayButton = new JButton("DAILY EXPENCES");
        dayButton.setFocusable(false);
        dayButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Daily daily = new Daily();
            }
        });

        JButton reptButton = new JButton("REPORT");
        reptButton.setFocusable(false);

        JLabel titleLabel = new JLabel("WELCOME TO THE FINANCIAL MANAGEMENT SYSTEM");
        titleLabel.setForeground(Color.BLACK);
        titleLabel.setFont(new Font("Maiandra GD", Font.BOLD,20));
        JPanel titlePanel = new JPanel();
        titlePanel.setPreferredSize(new Dimension(1500, 40));
        titlePanel.add(titleLabel);


        panel.add(stButton);
        panel.add(empButton);
        panel.add(dayButton);
        panel.add(reptButton);

        frame.add(titlePanel);
        frame.add(panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout( new FlowLayout());
        frame.setBounds(400,100,700,600);
        frame.setVisible(true);


    }
}