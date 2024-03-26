package RequestServer;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.LinkedHashMap;
import java.util.Map;

public class FaceRecognition {
    public final String USER_AGENT = "Mozilla/5.0";

    public static void main(String[] args) throws Exception{
        FaceRecognition http = new FaceRecognition();
        System.out.println("Sending HTTP GET request");
        http.sendGet();
        http.sendPost("ádasdliajdlaksdjad");
    }

    public void sendGet() throws Exception {

        String url = "http://81aa31fcdde5.ngrok.io/okchua";

        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        //Request header
        con.setRequestProperty("User-Agent", USER_AGENT);

        int responseCode = con.getResponseCode();
        System.out.println("\nSending 'GET' request to URL : " + url);
        System.out.println("Response Code : " + responseCode);

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        System.out.println(response.toString());
    }

    // HTTP POST request
    public String sendPost(String image_b64) throws Exception {

        URL url = new URL("https://8797-34-139-195-36.ngrok-free.app/detect");
        Map<String,Object> params = new LinkedHashMap<>();
        params.put("image", image_b64);
        StringBuilder postData = new StringBuilder();
        for (Map.Entry<String,Object> param : params.entrySet()) {
            if (postData.length() != 0) postData.append('&');
            postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
            postData.append('=');
            postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
        }
        byte[] postDataBytes = postData.toString().getBytes("UTF-8");

        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        conn.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
        conn.setDoOutput(true);
        conn.getOutputStream().write(postDataBytes);

        Reader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
        String result= "";
        for (int c; (c = in.read()) >= 0;){
//            System.out.print((char)c);
//            System.out.println("-");
            result += String.valueOf((char)c);
        }
        return result;
    }
}
