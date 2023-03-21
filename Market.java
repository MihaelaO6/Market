import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;


class Comparator1 implements Comparator<JSONObject> {
    @Override
    public int compare(JSONObject obj1, JSONObject obj2){
        String first = " ";
        try {
            first = (String) obj1.get("name");
        }catch (JSONException e){
            e.printStackTrace();
        }
        String sec = " ";
        try {
            sec = (String) obj2.get("name");
        }catch (JSONException e){
            e.printStackTrace();
        }
        assert first !=null;
        assert sec !=null;
        return  first.compareTo(sec);
    }
}
class MyResult {
    private final double total1;
    private final int counter1;
    private final double total2;
    private final int counter2;

    public MyResult(double total1, int counter1, double total2, int counter2) {
        this.total1 = total1;
        this.counter1 = counter1;
        this.total2 = total2;
        this.counter2 = counter2;
    }


    public double getTotal1() {
        return total1;
    }

    public int getCounter1() {
        return counter1;
    }

    public double getTotal2() {
        return total2;
    }

    public int getCounter2() {
        return counter2;
    }
}

public class Market {
    public static void print(String name,double price,String desc) throws IOException,JSONException{
        System.out.print("... " + name + "\n");
        System.out.print("    Price: $" + price + "\n");
        System.out.print("    " + desc.substring(0, 10) + "...\n");

    }
    public static String getJson(String link) throws IOException, JSONException {
        InputStream inputStream = new URL(link).openStream();
        try {
            BufferedReader bufReader = new BufferedReader(new InputStreamReader(inputStream));
            String lineText;
            String json = new String(" ");
            while ((lineText = bufReader.readLine()) != null) //read line by line
                json += lineText+"\n";
            return json;    // Returning json
        } catch (Exception e) {
            return null;
        } finally {
            inputStream.close();
        }
    }
    public static MyResult getProducts(double total1,int counter1, double total2, int counter2)throws IOException, JSONException{
        String data = getJson("https://interview-task-api.mca.dev/qr-scanner-codes/alpha-qr-gFpwhsQ8fkY1");

        JSONArray jsonArray = new JSONArray(data);
        ArrayList<JSONObject> list1 = new ArrayList<>();

        for (int i = 0; i<jsonArray.length();i++) {
            String str  = jsonArray.get(i).toString();
            JSONObject obj = new JSONObject(str);
            list1.add(obj);
        }
        list1.sort(new Comparator1());
        jsonArray = new JSONArray(list1);
        System.out.print(". Domestic \n");
        for (int i = 0; i<jsonArray.length();i++){
            JSONObject object1 = new JSONObject(jsonArray.get(i).toString());
            String name = object1.getString("name");
            boolean domestic = object1.getBoolean("domestic");
            double price = object1.getDouble("price");
            String desc = object1.getString("description");
            if(domestic){
                print(name,price,desc);
                total1+=price;
                counter1++;
                if (object1.has("weight")){
                    int weight = object1.getInt("weight");
                    System.out.println("    Weight: "+weight+"g");
                }else{
                    System.out.println("    Weight: N/A");
                }
            }
        }
        System.out.print(". Imported \n");
        for (int i = 0; i<jsonArray.length();i++){
            JSONObject object1 = new JSONObject(jsonArray.get(i).toString());
            String name = object1.getString("name");
            boolean domestic = object1.getBoolean("domestic");
            double price = object1.getDouble("price");
            String desc = object1.getString("description");
            if(!domestic){
                print(name,price,desc);
                total2+=price;
                counter2++;
                if (object1.has("weight")){
                    int weight = object1.getInt("weight");
                    System.out.println("    Weight: "+weight+"g");
                }else{
                    System.out.println("    Weight: N/A");
                }
            }
        }
        return new MyResult(total1, counter1,total2,counter2);

    }

    public static void main(String[] args) throws IOException, JSONException {
        double total1 = 0.0,total2=0.0;
        int counter1 = 0, counter2 = 0;

        MyResult result = getProducts(total1,counter1,total2,counter2);
        System.out.print("Domestic cost: $"+result.getTotal1()+"\nImported cost: $"+result.getTotal2()+"\n");
        System.out.print("Domestic count: "+result.getCounter1()+"\nImported count: "+result.getCounter2());
    }
}