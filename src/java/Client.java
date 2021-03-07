/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;

@ManagedBean
@RequestScoped

/**
 *
 * @author edwin
 */
public class Client {
    
     private static final long serialVersionUID = 8799656478674716638L;

    int id;
    int dni;
    String name;
    String name2;
    String lastname;
    String lastname2;
    Date birthdate;
    String occupation;
    int salary;

    ArrayList usersList;
    private Map<String, Object> sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
    Connection connection;

    public Client() {
        FacesContext context = FacesContext.getCurrentInstance();
        String viewId = context.getViewRoot().getViewId();
        
        System.out.println("Client.<init>()" + viewId);
        
        if(sessionMap.get("logged")== null && !viewId.contains("login")){
            try {
                FacesContext.getCurrentInstance().getExternalContext().redirect("login.xhtml?faces-redirect=true");
            } catch (Exception e) {
                System.out.println(e);
            }
        }
        
    }
    
    
    
    

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDni() {
        return dni;
    }

    public void setDni(int dni) {
        this.dni = dni;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName2() {
        return name2;
    }

    public void setName2(String name2) {
        this.name2 = name2;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getLastname2() {
        return lastname2;
    }

    public void setLastname2(String lastname2) {
        this.lastname2 = lastname2;
    }

    public Date getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(Date birthdate) {
        this.birthdate = birthdate;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public int getSalary() {
        return salary;
    }

    public void setSalary(int salary) {
        this.salary = salary;
    }

    public ArrayList getUsersList() {
        return usersList;
    }

    public void setUsersList(ArrayList usersList) {
        this.usersList = usersList;
    }

    public Map<String, Object> getSessionMap() {
        return sessionMap;
    }

    public void setSessionMap(Map<String, Object> sessionMap) {
        this.sessionMap = sessionMap;
    }

    // Used to establish connection
    public Connection getConnection() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            //connection = DriverManager.getConnection( "jdbc:mysql://localhost:3306/User","root","");
            connection = DriverManager.getConnection("jdbc:mysql://132.148.165.131:3306/dowesoft_lengpro?autoReconnect=true&useSSL=false", "dowesoft_lengprouser", "}cqUV6x~+Nwd");
        } catch (Exception e) {
            System.out.println(e);
        }
        return connection;
    }

    // Used to fetch all records
    public ArrayList usersList() {
        
       
        
        
        try {
            usersList = new ArrayList();
            connection = getConnection();
            Statement stmt = getConnection().createStatement();
            ResultSet rs = stmt.executeQuery("select * from client order by id desc");
            while (rs.next()) {
                Client client = new Client();
                client.setId(rs.getInt("id"));
                client.setName(rs.getString("name"));
                client.setName(rs.getString("name"));
                client.setName2(rs.getString("name2"));
                client.setDni(rs.getInt("dni"));

                client.setLastname(rs.getString("lastname"));
                client.setLastname2(rs.getString("lastname2"));
                client.setLastname2(rs.getString("lastname2"));

                client.setBirthdate(this.convertStringToDate(rs.getString("birthdate")));
                client.setOccupation(rs.getString("occupation"));
                client.setSalary(rs.getInt("salary"));

                usersList.add(client);
            }
            connection.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        return usersList;
    }


    // Used to save client record
    public String save() {
        int result = 0;

        if (existClient(dni)) {
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Su solicitud ya ha sido recibida pronto un agente del banco se contactará con usted.", "Información"));
            return "";
        } else {
            try {

                Locale locale = new Locale("es", "CO");

                DateFormat df = DateFormat.getDateInstance(DateFormat.MEDIUM, locale);
                String formattedDate = df.format(birthdate);

                connection = getConnection();
                PreparedStatement stmt = connection.prepareStatement("insert into client(dni, name, name2, lastname, lastname2, birthdate, occupation, salary) values(?,?,?,?,?,?,?,?)");
                stmt.setInt(1, dni);
                stmt.setString(2, name);
                stmt.setString(3, name2);
                stmt.setString(4, lastname);
                stmt.setString(5, lastname2);
                stmt.setString(6, formattedDate);
                stmt.setString(7, occupation);
                stmt.setInt(8, salary);
                result = stmt.executeUpdate();
                connection.close();

            } catch (Exception e) {
                System.out.println(e);
            }

            if (result != 0) {
                return "workspace.xhtml?faces-redirect=true";
            } else {
                return "newclient.xhtml?faces-redirect=true";
            }

        }
    }

    // Used to fetch all records
    private boolean existClient(int dni) {
        try {
            connection = getConnection();
            Statement stmt = getConnection().createStatement();
            ResultSet rs = stmt.executeQuery("select * from client where dni='" + dni + "'");
            Client client = null;
            while (rs.next()) {
                client = new Client();
            }
            connection.close();

            if (client != null) {
                return true;
            } else {
                return false;
            }

        } catch (Exception e) {
            System.out.println(e);
        }
        return false;
    }

    public Date convertStringToDate(String dateString) {
        Date date = null;
        Date formatteddate = null;
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        try {
            date = df.parse(dateString);

        } catch (Exception ex) {
            System.out.println(ex);
        }
        return date;
    }

    // Used to save client record
    public String saveLogin() {
        int result = 0;

        if (existClient(dni)) {
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Su solicitud ya ha sido recibida pronto un agente del banco se contactará con usted.", "Información"));
            return "";
        } else {
            try {

                Locale locale = new Locale("es", "CO");

                DateFormat df = DateFormat.getDateInstance(DateFormat.MEDIUM, locale);
                String formattedDate = df.format(birthdate);

                connection = getConnection();
                PreparedStatement stmt = connection.prepareStatement("insert into client(dni, name, name2, lastname, lastname2, birthdate, occupation, salary) values(?,?,?,?,?,?,?,?)");
                stmt.setInt(1, dni);
                stmt.setString(2, name);
                stmt.setString(3, name2);
                stmt.setString(4, lastname);
                stmt.setString(5, lastname2);
                stmt.setString(6, formattedDate);
                stmt.setString(7, occupation);
                stmt.setInt(8, salary);
                result = stmt.executeUpdate();
                connection.close();

            } catch (Exception e) {
                System.out.println(e);
            }

            return "login.xhtml?faces-redirect=true";

        }
    }

    // Used to fetch record to update
    public String edit(int id) {
        Client client = null;
        System.out.println(id);
        try {
            connection = getConnection();
            Statement stmt = getConnection().createStatement();
            ResultSet rs = stmt.executeQuery("select * from client where id = " + (id));
            rs.next();

            client = new Client();
            client.setId(rs.getInt("id"));
            client.setName(rs.getString("name"));
            client.setName2(rs.getString("name2"));
            client.setDni(rs.getInt("dni"));

            client.setLastname(rs.getString("lastname"));
            client.setLastname2(rs.getString("lastname2"));

            client.setBirthdate(this.convertStringToDate(rs.getString("birthdate")));

            client.setOccupation(rs.getString("occupation"));
            client.setSalary(rs.getInt("salary"));

            sessionMap.put("editClient", client);
            connection.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        return "/edit.xhtml?faces-redirect=true";
    }

    // Used to update user record
    public String update(Client c) {
     
        try {

            Locale locale = new Locale("es", "CO");

            DateFormat df = DateFormat.getDateInstance(DateFormat.MEDIUM, locale);
            String formattedDate = df.format(c.getBirthdate());

            connection = getConnection();
            PreparedStatement stmt = connection.prepareStatement("update client set dni=?, name=?, name2=?, lastname=?, lastname2=?, birthdate=?, occupation=?, salary=? where id=?");
            stmt.setInt(1, c.getDni());
            stmt.setString(2, c.getName());
            stmt.setString(3, c.getName2());
            stmt.setString(4, c.getLastname());
            stmt.setString(5, c.getLastname2());
            stmt.setString(6, formattedDate);
            stmt.setString(7, c.getOccupation());
            stmt.setInt(8, c.getSalary());
            stmt.setInt(9, c.getId());

            stmt.executeUpdate();
            connection.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        return "/workspace.xhtml?faces-redirect=true";
    }

}
