package huji.postpc.y2021.liorait.post_pc_ex7;

import android.annotation.SuppressLint;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Sandwich implements Serializable{

    private String id = null;
    private String costumerName=null;
    private int pickles=0;
    private boolean hummus=false;
    private boolean tahini=false;
    private String comment="";
    private String status="";

    public Sandwich(){
    }

    public Sandwich(String id, String name, String status, int pickles, boolean hummus, boolean tahini, String comment) {
        this.id = id;
        this.costumerName = name;
        this.status = status;
        this.pickles = pickles;
        this.hummus = hummus;
        this.tahini = tahini;
        this.comment = comment;
    }

    public String toString(){
        String pickles_str = Integer.toString(pickles);
        return " id: " + this.id + " costumer name: " + this.costumerName + " status: " + this.status + " pickles: " + pickles_str + " hummus: " +
                this.hummus + " tahini: " + this.tahini + " comment: " + this.comment;
     }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public String getCostumerName() {
        return costumerName;
    }

    public String getStatus() {
        return status;
    }

    public String getComment() {
        return comment;
    }

    public int getPickles() {
        return pickles;
    }

    public boolean getHummus() {
        return hummus;
    }

    public boolean getTahini() {
        return tahini;
    }

}






