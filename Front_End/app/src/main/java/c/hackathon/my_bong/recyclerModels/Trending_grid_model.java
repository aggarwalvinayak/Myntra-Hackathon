package c.hackathon.my_bong.recyclerModels;

public class Trending_grid_model {

    String name;
    String price;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    String url;

    public Trending_grid_model(){

    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public Trending_grid_model(String name, String price) {
        this.name = name;
        this.price = price;
        this.url = url;
    }


}
