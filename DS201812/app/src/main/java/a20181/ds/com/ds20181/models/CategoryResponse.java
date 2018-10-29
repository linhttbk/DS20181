package a20181.ds.com.ds20181.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CategoryResponse {
    @SerializedName("error")
    private boolean error;

    public boolean isError() {
        return error;
    }

    public List<Category> getCategories() {
        return categories;
    }

    @SerializedName("categories")

    private List<Category> categories;
}
