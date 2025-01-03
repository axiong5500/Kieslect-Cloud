import cn.hutool.http.HttpUtil;

public class openai {
    public static void main(String[] args) {
        String url = "http://127.0.0.1:11434/api/chat";
        String json = "{\n" +
                "\"model\": \"llama2\",\n" +
                "\"messages\": [\n" +
                "    {\n" +
                "        \"role\": \"user\",\n" +
                "        \"content\": \"why is the sky blue?\"\n" +
                "    }\n" +
                "],\n" +
                "\"stream\": false\n" +
                "}\n";
        try {
            String response = HttpUtil.post(url, json);
            System.out.println(response);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
