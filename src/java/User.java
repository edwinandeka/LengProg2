import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Map;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;

@ManagedBean
@RequestScoped
public class User{
    int id;
    String name;
    String email;
    String password;
    String gender;
    String address;
    ArrayList usersList ;
    private Map<String,Object> sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
    Connection connection;
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
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getGender() {
        return gender;
    }
    public void setGender(String gender) {
        this.gender = gender;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
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
    
    
    public void login() {
        
        FacesContext context = FacesContext.getCurrentInstance();
        if (email != null && password != null ) {
             try{
                Statement stmt=getConnection().createStatement();
                ResultSet rs=stmt.executeQuery("select * from users where email = '"+(email)+ "' and password= '" + password + "'");
                
                if (rs.next() == false) { 
                
                    context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Usuario y/o contraseña inválido" , "Información"));

                } else {
                    User user = new User();
                    user.setId(rs.getInt("id"));
                    user.setName(rs.getString("name"));
                    user.setEmail(rs.getString("email"));
                    user.setPassword(rs.getString("password"));
                    user.setGender(rs.getString("gender"));
                    user.setAddress(rs.getString("address"));
                    
                    sessionMap.put("logged", user);
                    try {
                        FacesContext.getCurrentInstance().getExternalContext().redirect("workspace.xhtml?faces-redirect=true");
                    } catch (Exception e) {
                        System.out.println(e);
                    } 
               }
                
            }catch(Exception e){
                System.out.println(e);
            }
          
      } else {
        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Usuario y/o contraseña inválido", "Información"));
      }
    }

    public void logout() {
      sessionMap.clear();
    }

}