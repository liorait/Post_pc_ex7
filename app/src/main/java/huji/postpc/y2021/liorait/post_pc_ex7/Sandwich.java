package huji.postpc.y2021.liorait.post_pc_ex7;

import android.annotation.SuppressLint;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Sandwich implements Serializable{

    private String id=null;
    private String costumer_name="";
    private int pickles=0;
    private boolean hummus=false;
    private boolean tahini=false;
    private String comment="";
    private String status=null;
    private String READY = "ready";
    private String DONE = "done";
    private String WAITING = "waiting";
    private String IN_PROGRESS = "in progress";

    public Sandwich(String id, String status) {
        this.id = id;
        this.status = status;
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






