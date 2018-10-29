package a20181.ds.com.ds20181.services;

public class AppClient {
    private AppClient() {}

    public static APIService getAPIService() {
        return RetrofitClient.getClient().create(APIService.class);
    }
}
