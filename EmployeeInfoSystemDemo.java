import java.awt.Button;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Label;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

class DBConn {
    public static Statement getDB() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/emp", "root", "root");
        Statement st = con.createStatement();
        return st;
    }
}

class Employee {
    int id;
    String name;
    String dept;

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getDept() {
        return dept;
    }
    public void setDept(String dept) {
        this.dept = dept;
    }
}

class EmpFrame extends Frame implements ActionListener {
    Label l1, l2, l3, l;
    TextField tf1, tf2, tf3;
    Button b1, b2, b3, b4;
    Employee emp;
    String msg="";

    EmpFrame(String title){
        l = new Label("EMPLOYEE INFO SYSTEM");
        l1 = new Label("EMP ID");
        tf1 = new TextField(20);

        l2 = new Label("EMP NAME");
        tf2 = new TextField(20);

        l3 = new Label("EMP SALARY");
        tf3 = new TextField(20);

        b1 = new Button("INSERT EMP");
        b2 = new Button("SEARCH EMP");
        b3 = new Button("DELETE EMP");
        b4 = new Button("UPDATE EMP");

        this.setLayout(null);
        l.setBounds(110, 60, 160, 20);
        l1.setBounds(50, 100, 100, 20);
        tf1.setBounds(200, 100, 100, 20);
        l2.setBounds(50, 150, 100, 20);
        tf2.setBounds(200, 150, 100, 20);
        l3.setBounds(50, 200, 100, 20);
        tf3.setBounds(200, 200, 100, 20);
        b1.setBounds(50, 250, 100, 25);
        b2.setBounds(170, 250, 100, 25);
        b3.setBounds(290, 250, 100, 25);
        b4.setBounds(410, 250, 100, 25);

        add(l);
        add(l1); add(tf1);
        add(l2); add(tf2);
        add(l3); add(tf3);
        add(b1); add(b2); add(b3); add(b4);

        b1.addActionListener(this);
        b2.addActionListener(this);
        b3.addActionListener(this);
        b4.addActionListener(this);
    }
// this is not mandatory
    public void actionPerformed(ActionEvent ae){
        Button b = (Button)ae.getSource();
        if(b.getActionCommand().equalsIgnoreCase("INSERT EMP")){
            emp = new Employee();
            emp.setId(Integer.parseInt(tf1.getText()));
            emp.setName(tf2.getText());
            emp.setDept(tf3.getText());

            try {
                Statement st = DBConn.getDB();
                String query = "insert into emp (eno, ename, esal) values ("+emp.getId()+",'"+emp.getName()+"','"+emp.getDept()+"')";
                System.out.println(query);
                int i = st.executeUpdate(query);

                if(i==1){
                    msg = "Employee Saved In DB";
                    tf1.setText("");
                    tf2.setText("");
                    tf3.setText("");
                    tf1.requestFocus();
                } else{
                    msg = "Not Saved";
                }

            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();
            }
        } else if(b.getActionCommand().equalsIgnoreCase("SEARCH EMP")){
            try {
                Statement st = DBConn.getDB();
                emp = new Employee();
                emp.setId(Integer.parseInt(tf1.getText()));
                String query = "select * from emp where eno = "+emp.getId();
                ResultSet rs = st.executeQuery(query);

                while(rs.next()){
                    emp.setId(rs.getInt(1));
                    emp.setName(rs.getString(2));
                    emp.setDept(rs.getString(3));

                    tf2.setText(emp.getName());
                    tf3.setText(emp.getDept());
                }
            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();
            }
        } else if(b.getActionCommand().equalsIgnoreCase("DELETE EMP")){
            try {
                Statement st = DBConn.getDB();
                if (emp != null) {
                    String query = "delete from emp where eno = "+emp.getId();
                    int i = st.executeUpdate(query);
                    if(i==1){
                        msg = "Employee Deleted in DB";
                    } else {
                        msg = "Not Found";
                    }
                } else {
                    msg = "Please search for an employee first.";
                }
            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();
            }
        } else if(b.getActionCommand().equalsIgnoreCase("UPDATE EMP")) {
            emp = new Employee();
            emp.setId(Integer.parseInt(tf1.getText()));
            emp.setName(tf2.getText());
            emp.setDept(tf3.getText());

            try {
                Statement st = DBConn.getDB();
                String query = "update emp set ename='" + emp.getName() + "', esal='" + emp.getDept() + "' where eno=" + emp.getId();
                int i = st.executeUpdate(query);

                if (i == 1) {
                    msg = "Employee Updated In DB";
                    tf1.setText("");
                    tf2.setText("");
                    tf3.setText("");
                    tf1.requestFocus();
                } else {
                    msg = "Update Failed";
                }
            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();
            }
        }
        repaint();
    }

    public void paint(Graphics g){
        g.drawString(msg, 200, 350);
    }
}

public class EmployeeInfoSystemDemo {
    public static void main(String[] args) {
        EmpFrame ef = new EmpFrame("EMPLOYEE INFO SYSTEM");
        ef.setSize(600, 450);
        ef.setVisible(true);
    }
}
