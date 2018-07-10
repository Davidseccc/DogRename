
import java.text.SimpleDateFormat;
import java.util.Date;

public class GyroData {

    public static final String OUTPUT_DATE_FORMAT = "yyyyMMddHHmmssSSS";

    String firstColwithG;
    Date date;
    String originalDate;
    String x;
    String y;
    String z;
    double delta;

    public GyroData(String firstColwithG, Date date, String x, String y, String z) {
        this.firstColwithG = firstColwithG;
        this.date = date;
        this.x = x;
        this.y = y;
        this.z = z;
        this.delta = 0;
    }

    public GyroData(){

    }

    public String gyroOrAccIdentificator() {
        return firstColwithG;
    }

    public void setFirstColwithG(String firstColwithG) {
        this.firstColwithG = firstColwithG;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getX() {
        return x;
    }

    public void setX(String x) {
        this.x = x;
    }

    public String getY() {
        return y;
    }

    public void setY(String y) {
        this.y = y;
    }

    public String getZ() {
        return z;
    }

    public void setZ(String z) {
        this.z = z;
    }

    public double getDelta() {
        return delta;
    }

    public void setDelta(double delta) {
        this.delta = delta;
    }

    public String getOriginalDate() {
        return originalDate;
    }

    public void setOriginalDate(String originalDate) {
        this.originalDate = originalDate;
    }

    @Override
    public String toString() {
        SimpleDateFormat sdf = new SimpleDateFormat(OUTPUT_DATE_FORMAT);
        return "GyroData{" +
                "firstColwithG='" + firstColwithG + '\'' +
                ", date='" + sdf.format(date) + '\'' +
                ", originalDate='" + originalDate + '\'' +
                ", x='" + x + '\'' +
                ", y='" + y + '\'' +
                ", z='" + z + '\'' +
                ", delta='" + delta + '\'' +
                '}';
    }
}
