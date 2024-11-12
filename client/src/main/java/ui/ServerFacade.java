package ui;



public class ServerFacade{
    private String url;
    public ServerFacade(String url){
        this.url = url;
    }

    public String login(String username, String password){
        String authToken = "";
        String path = "/session";
        //LogRequest req = new LogRequest();
        return authToken;
    }

}