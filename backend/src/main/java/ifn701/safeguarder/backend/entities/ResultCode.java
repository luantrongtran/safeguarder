package ifn701.safeguarder.backend.entities;

/**
 * Created by lua on 26/09/2015.
 */
public class ResultCode {
    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    String msg;

    boolean result;
}
