package sgcarvalet.adam.com.sgcarvalet.adam.com.model;

/**
 * Created by adam on 1/1/15.
 */
public class User {

    private int id;
    private String nama;
    private String email;
    private String password;
    private String status_logout;
    private String telp;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
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

    public String getStatus_logout() {
        return status_logout;
    }

    public void setStatus_logout(String status_logout) {
        this.status_logout = status_logout;
    }

    public String getTelp() {
        return telp;
    }

    public void setTelp(String telp) {
        this.telp = telp;
    }
}
