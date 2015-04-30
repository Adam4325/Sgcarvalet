package sgcarvalet.adam.com.sgcarvalet;

import java.util.Date;

/**
 * Created by adam on 1/7/15.
 */
public class M_History {

    private String address;
    private String LNG;
    private String LTD;
    private Date dtWaktu;
    private String id_mapss;

    public M_History(String address, String LNG, String LTD,Date waktu,String id_mapss) {
        super();
        this.address = address;
        this.LNG = LNG;
        this.LTD = LTD;
        this.dtWaktu = waktu;
        this.id_mapss = id_mapss;

    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLNG() {
        return LNG;
    }

    public void setLNG(String LNG) {
        this.LNG = LNG;
    }

    public String getLTD() {
        return LTD;
    }

    public void setLTD(String LTD) {
        this.LTD = LTD;
    }

    public Date getDtWaktu() {
        return dtWaktu;
    }

    public void setDtWaktu(Date dtWaktu) {
        this.dtWaktu = dtWaktu;
    }

    public String getId_mapss() {
        return id_mapss;
    }

    public void setId_mapss(String id_mapss) {
        this.id_mapss = id_mapss;
    }
}
