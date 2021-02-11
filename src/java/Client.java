/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
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
    

    int id;
    int dni;
    String name;
    String name2;
    String lastname;
    String lastname2;
    String birthdate;
    String occupation;
    int salary;
    
    
    ArrayList usersList ;
    private Map<String,Object> sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
    Connection connection;
    
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

    public String getBirthdate() {
        return birthdate;
    }


    public void setBirthdate(String birthdate) {
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
    public Connection getConnection(){
        try{
            Class.forName("com.mysql.jdbc.Driver");   
            connection = DriverManager.getConnection( "jdbc:mysql://localhost:3306/User","root","");
        }catch(Exception e){
            System.out.println(e);
        }
        return connection;
    }
    // Used to fetch all records
    public ArrayList usersList(){
        try{
            usersList = new ArrayList();
            connection = getConnection();
            Statement stmt=getConnection().createStatement();  
            ResultSet rs=stmt.executeQuery("select * from users");  
            while(rs.next()){
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setName(rs.getString("name"));
                user.setEmail(rs.getString("email"));
                user.setPassword(rs.getString("password"));
                user.setGender(rs.getString("gender"));
                user.setAddress(rs.getString("address"));
                usersList.add(user);
            }
            connection.close();        
        }catch(Exception e){
            System.out.println(e);
        }
        return usersList;
    }
    // Used to save client record
    public String save(){
        int result = 0;
        try{
            connection = getConnection();
            PreparedStatement stmt = connection.prepareStatement("insert into client(dni, name, name2, lastname, lastname2, birthdate, occupation, salary) values(?,?,?,?,?)");
            stmt.setInt(1, dni);
            stmt.setString(2, name);
            stmt.setString(3, name2);
            stmt.setString(4, lastname);
            stmt.setString(5, lastname2);
            stmt.setString(6, birthdate);
            stmt.setString(7, occupation);
            stmt.setInt(8, salary);
            result = stmt.executeUpdate();
            connection.close();
        }catch(Exception e){
            System.out.println("sfsfdfdfsdfsdfsd");            
            System.out.println(e);

        }
        if(result !=0)
            return "index.xhtml?faces-redirect=true";
        else return "create.xhtml?faces-redirect=true";
    }
    
    // Used to save client record
    public String saveLogin(){
        int result = 0;
        try{
            
               
            String dateParseString = "EEE MMM dd HH:mm:ss Z yyyy";
            Locale locale = new Locale("es","CO");

            SimpleDateFormat formatter = new SimpleDateFormat(dateParseString, locale);
            Date parsedDate = formatter.parse(birthdate); 

            Calendar cal = Calendar.getInstance();
            cal.setTime(parsedDate);
            String formatedDate = cal.get(Calendar.YEAR) + "-" + (cal.get(Calendar.MONTH) + 1) + "-" +  cal.get(Calendar.DATE);
     
            connection = getConnection();
            PreparedStatement stmt = connection.prepareStatement("insert into client(dni, name, name2, lastname, lastname2, birthdate, occupation, salary) values(?,?,?,?,?,?,?,?)");
            stmt.setInt(1, dni);
            stmt.setString(2, name);
            stmt.setString(3, name2);
            stmt.setString(4, lastname);
            stmt.setString(5, lastname2);
            stmt.setString(6, formatedDate);
            stmt.setString(7, occupation);
            stmt.setInt(8, salary);
            result = stmt.executeUpdate();
            connection.close();
        }catch(Exception e){
            System.out.println(e);
        }
        return "login.xhtml?faces-redirect=true";
    }
    // Used to fetch record to update
    public String edit(int id){
        User user = null;
        System.out.println(id);
        try{
            connection = getConnection();
            Statement stmt=getConnection().createStatement();  
            ResultSet rs=stmt.executeQuery("select * from users where id = "+(id));
            rs.next();
            user = new User();
            user.setId(rs.getInt("id"));
            user.setName(rs.getString("name"));
            user.setEmail(rs.getString("email"));
            user.setGender(rs.getString("gender"));
            user.setAddress(rs.getString("address"));
            user.setPassword(rs.getString("password"));  
            System.out.println(rs.getString("password"));
            sessionMap.put("editUser", user);
            connection.close();
        }catch(Exception e){
            System.out.println(e);
        }       
        return "/edit.xhtml?faces-redirect=true";
    }
    // Used to update user record
    public String update(User u){
        //int result = 0;
        try{
            connection = getConnection();  
            PreparedStatement stmt=connection.prepareStatement("update users set name=?,email=?,password=?,gender=?,address=? where id=?");  
            stmt.setString(1,u.getName());  
            stmt.setString(2,u.getEmail());  
            stmt.setString(3,u.getPassword());  
            stmt.setString(4,u.getGender());  
            stmt.setString(5,u.getAddress());  
            stmt.setInt(6,u.getId());  
            stmt.executeUpdate();
            connection.close();
        }catch(Exception e){
            System.out.println();
        }
        return "/index.xhtml?faces-redirect=true";      
    }
    // Used to delete user record
    public void delete(int id){
        try{
            connection = getConnection();  
            PreparedStatement stmt = connection.prepareStatement("delete from users where id = "+id);  
            stmt.executeUpdate();  
        }catch(Exception e){
            System.out.println(e);
        }
    }
    // Used to set user gender
    public String getGenderName(char gender){
        if(gender == 'M'){
            return "Male";
        }else return "Female";
    }
}
