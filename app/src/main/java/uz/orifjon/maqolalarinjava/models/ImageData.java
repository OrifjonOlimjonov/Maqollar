package uz.orifjon.maqolalarinjava.models;

import java.io.Serializable;
import java.util.Objects;

public class ImageData implements Serializable {

    private int image;
    private String count;
    private String name;
    public ImageData(int image, String count, String name) {
        this.image = image;
        this.count = count;
        this.name = name;

    }

    public int getImage() {
        return image;
    }

    public String getCount() {
        return count;
    }

    public String getName() {
        return name;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ImageData imageData = (ImageData) o;
        return image == imageData.image && Objects.equals(count, imageData.count) && Objects.equals(name, imageData.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(image, count, name);
    }
}
