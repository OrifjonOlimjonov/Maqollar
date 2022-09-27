package uz.orifjon.maqolalarinjava.models;

import java.io.Serializable;

public class Maqollar implements Serializable {

    private String postUz;
    private String postRu;
    private String postEng;

    public Maqollar(String postUz, String postRu, String postEng) {
        this.postUz = postUz;
        this.postRu = postRu;
        this.postEng = postEng;
    }

    public String getPostUz() {
        return postUz;
    }

    public void setPostUz(String postUz) {
        this.postUz = postUz;
    }

    public String getPostRu() {
        return postRu;
    }

    public void setPostRu(String postRu) {
        this.postRu = postRu;
    }

    public String getPostEng() {
        return postEng;
    }

    public void setPostEng(String postEng) {
        this.postEng = postEng;
    }
}
