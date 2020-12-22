package cn.yjw.pixabayapp.entity.remote;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * @author yinjiawei
 * @date 2020/12/21
 */
public class PhotoItemEntity implements Parcelable {
    private int id;
    private String type;
    @SerializedName("webFormatURL")
    private String webFormatUrl;
    @SerializedName("largeImageURL")
    private String largeImageUrl;

    public PhotoItemEntity(int id, String type, String webFormatUrl, String largeImageUrl) {
        this.id = id;
        this.type = type;
        this.webFormatUrl = webFormatUrl;
        this.largeImageUrl = largeImageUrl;
    }

    protected PhotoItemEntity(Parcel in) {
        id = in.readInt();
        type = in.readString();
        webFormatUrl = in.readString();
        largeImageUrl = in.readString();
    }

    public static final Creator<PhotoItemEntity> CREATOR = new Creator<PhotoItemEntity>() {
        @Override
        public PhotoItemEntity createFromParcel(Parcel in) {
            return new PhotoItemEntity(in);
        }

        @Override
        public PhotoItemEntity[] newArray(int size) {
            return new PhotoItemEntity[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(type);
        dest.writeString(webFormatUrl);
        dest.writeString(largeImageUrl);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getWebFormatUrl() {
        return webFormatUrl;
    }

    public void setWebFormatUrl(String webFormatUrl) {
        this.webFormatUrl = webFormatUrl;
    }

    public String getLargeImageUrl() {
        return largeImageUrl;
    }

    public void setLargeImageUrl(String largeImageUrl) {
        this.largeImageUrl = largeImageUrl;
    }

}
