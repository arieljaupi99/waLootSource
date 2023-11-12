package junction.finland.nova_spring.utility;

import java.text.SimpleDateFormat;
import java.util.Date;

public class RequestDataUtility {
    private RequestDataUtility(){}
    private static final String ZONED_DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSz";

    public static String getCurrentDateTime() {
        return new SimpleDateFormat(ZONED_DATE_TIME_FORMAT).format(new Date());
    }

}
