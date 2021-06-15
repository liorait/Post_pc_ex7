package huji.postpc.y2021.liorait.post_pc_ex7;

import android.annotation.SuppressLint;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Sandwich implements Serializable{

     String id = null;
     String costumer_name="";
     String pickles="0";
     boolean hummus=false;
     boolean tahini=false;
     String comment="";
     String status=null;
     String READY = "ready";
     String DONE = "done";
     String WAITING = "waiting";
     String IN_PROGRESS = "in progress";

    public Sandwich(String id, String status, String pickles, boolean hummus, boolean tahini, String comment) {
        this.id = id;
        this.status = status;
        this.pickles = pickles;
        this.hummus =hummus;
        this.tahini = tahini;
        this.comment = comment;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public String getStatus() {
        return status;
    }

}






