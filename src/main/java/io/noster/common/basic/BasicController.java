package io.noster.common.basic;

import io.noster.common.util.GenException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class BasicController {
    //protected Configuration config = Configuration.getInstance();
    protected Random random = new Random();
    public Logger log = LoggerFactory.getLogger(BasicController.class);


    public static final String XSTATUS_CODE   = "X-Status-Code";
    public static final String XSTATUS_REASON = "X-Status-Reason";
    public static final String SESSION_KEY    = "Session-Key";

    public int genTid() {
        return random.nextInt();
    }

    public String getServerName() {
        //return config.getServerName();
        return "";
    }

    public byte[] genErrorJson(int tid, byte[] reqByte, String operation, Exception ex, Logger log) {
        byte[] resByte = null;

        try {
            int errorCode = GenException.INTERNAL_ERROR;
            String message = ex.getMessage() + "";

            if(ex instanceof GenException) {
                errorCode = ((GenException)ex).getErrorCode();
            }

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

            JSONObject jsonObj = new JSONObject();

            jsonObj.put("source",    getServerName());
            jsonObj.put("operation", operation);
            jsonObj.put("timestamp", sdf.format(new Date()));
            jsonObj.put("status",    errorCode);
            jsonObj.put("message",   message);




            try {
                resByte = jsonObj.toString(4).getBytes("UTF-8");
            } catch (Exception e) {
                resByte = jsonObj.toString(4).getBytes();
            }

            if(log != null) {
                log.error("["+tid+"] Input  >>" + new String(reqByte) + "<<");
                log.error("["+tid+"] Output >>" + new String(resByte) + "<<");
                log.error("["+tid+"] " + ex.getMessage(), ex);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        return resByte;
    }


    public String nullToBlank(String str) {
        if(str == null) {
            return "";
        } else {
            return str;
        }
    }


    public byte[] getInputStreamToByte(InputStream is) throws IOException {

        int readNum = -1;
        byte[] buf = new byte[8192];
        ByteArrayOutputStream os = new ByteArrayOutputStream();

        while((readNum = is.read(buf)) != -1) {
            os.write(buf, 0, readNum);
        }

        is.close();
        return os.toByteArray();
    }

    public JSONObject genByteToJson(byte[] buf) {
        JSONObject jsonObj = null;
        try {
            return new JSONObject(new String(buf, "UTF-8"));
        } catch (Exception e) {
            log.warn("genByteToJson " + e);
        }

        return jsonObj;
    }


    public String genReqInfo(HttpServletRequest request)
    {
        StringBuffer sb = new StringBuffer();
        if (request != null)
        {
            String userIp = getIpFromRequest(request);
            String userAgent = request.getHeader("User-Agent");
            if ((userAgent != null) && (userAgent.length() > 20)) {
                userAgent = userAgent.substring(0, 20) + "...";
            }
            String method = request.getMethod();
            String path = request.getRequestURI();
            String query = request.getQueryString();

            sb.append("[" + method + " " + path);
            if (query != null) {
                sb.append("?" + query + "]");
            } else {
                sb.append("]");
            }
            sb.append(" >>> [" + userIp + "][" + userAgent + "]");
        }
        return sb.toString();
    }

    public String getIpFromRequest(HttpServletRequest request)
    {
        String userIp = request.getHeader("x-forwarded-for");
        if ((userIp == null) || (userIp.trim().equals(""))) {
            userIp = request.getRemoteAddr();
        }
        userIp.indexOf(':');
        if (userIp.indexOf(',') != -1)
        {
            String separateIp = userIp.substring(0, userIp.indexOf(','));

            userIp = separateIp;
        }
        return userIp;
    }


    public String getSimpleErrRes(GenException ex){
        JSONObject errJson = new JSONObject();
        try {
            errJson.put("status", ex.getErrorCode());
            errJson.put("message", ex.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return errJson.toString();
    }

    public String getSimpleErrRes(Exception ex)
    {
        JSONObject errJson = new JSONObject();
        try {
            errJson.put("status", 500);
            errJson.put("message", ex.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return errJson.toString();
    }


    public String makeResponseMsg(int status, String msg) {

        JSONObject resObj = new JSONObject();
        try {
            resObj.put("message", msg);		// 블록 개수 데이터 설정
            resObj.put("status", status);		// 상태 코드 입력
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resObj.toString();
    }

    public String makeObjResponseMsg(int status, JSONObject msg) {

        JSONObject resObj = new JSONObject();
        try {
            resObj.put("message", msg);		// 블록 개수 데이터 설정
            resObj.put("status", status);		// 상태 코드 입력
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resObj.toString();
    }


    public String makeStartTimeString(String endTime, String unit, int unitValue) {

        try {
            Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(endTime);
            long ONE_HOUR = 3600 * 1000;
            long ONE_DAY  = ONE_HOUR * 24;
            long ONE_WEEK  = ONE_HOUR * 24;
            String start = "";

            if(unit.equals("Min")) {
                log.debug("case min");
                Date startDate = new Date(date.getTime() - (ONE_HOUR * Long.valueOf(1)));
                start = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(startDate);
                log.debug("case hour finish"+start);
            } else if(unit.equals("Hour")) {
                log.debug("case hour");
                Date startDate = new Date(date.getTime() - (ONE_HOUR * Long.valueOf(unitValue)));
                start = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(startDate);
                log.debug("case hour finish"+start);
            } else if(unit.equals("Day")) {
                log.debug("case day");
                Date startDate = new Date(date.getTime() - (ONE_DAY * Long.valueOf(unitValue)));
                start = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(startDate);
                log.debug("case day finish"+start);
            } else if(unit.equals("Week")) {
                log.debug("case week");
                Date startDate = new Date(date.getTime() - (ONE_WEEK * Long.valueOf(unitValue)));
                start = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(startDate);
                log.debug("case week finish"+start);
            } else {
                throw new Exception("Invalid Unit["+unit+"]") ;
            }

            return start;

        } catch (Exception e) {
            log.error("[MakeStartTimeString()] status [" + 500 + "] / errMsg : [" + e + "]");
        }

        return null;

    }


    public boolean isNumeric(String input) {
        try {
            Double.parseDouble(input);
            return true;
        }
        catch (NumberFormatException e) {
            return false;
        }
    }

    public boolean isInteger(String input) {
        try {
            BigInteger bigIntegerStr=new BigInteger(input);
            return true;
        }
        catch (NumberFormatException e) {
            return false;
        }
    }
}
