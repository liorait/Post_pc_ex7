package huji.postpc.y2021.liorait.post_pc_ex7;

import android.annotation.SuppressLint;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Sandwich implements Serializable{

     String id = null;
     String costumer_name = "";
     String pickles="0";
     boolean hummus=false;
     boolean tahini=false;
     String comment="";
     String status="";

    public Sandwich(){
        //this.id = "id";
        //this.status = "status";
        //this.pickles = "0";
        //this.hummus =false;
       // this.tahini = false;
       // this.comment = "comment";
    }

    public Sandwich(String id, String name, String status, String pickles, boolean hummus, boolean tahini, String comment) {
        this.id = id;
        this.costumer_name = name;
        this.status = status;
        this.pickles = pickles;
        this.hummus =hummus;
        this.tahini = tahini;
        this.comment = comment;
    }

    public String toString(){
        return " id: " + this.id + " costumer name: " + this.costumer_name + " status: " + this.status + " pickles: " + this.pickles + " hummus: " +
                this.hummus + " tahinh: " + this.tahini + " comment: " + this.comment;
     }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public String getCostumerName() {
        return costumer_name;
    }

    public String getStatus() {
        return status;
    }

    public String getComment() {
        return comment;
    }

    public String getPickles() {
        return pickles;
    }

    public boolean getHummus() {
        return hummus;
    }

    public boolean getTahini() {
        return tahini;
    }

}






