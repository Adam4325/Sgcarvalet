package sgcarvalet.adam.com.sgcarvalet.adam.com.model;

import android.app.Application;

/**
 * Created by adam on 1/7/15.
 */
public class static_user extends Application {

    private String email;
    private String Nama;
    private String password;
    private String telpn;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNama() {
        return Nama;
    }

    public void setNama(String nama) {
        this.Nama = nama;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTelpn() {
        return telpn;
    }

    public void setTelpn(String telpn) {
        this.telpn = telpn;
    }
}
