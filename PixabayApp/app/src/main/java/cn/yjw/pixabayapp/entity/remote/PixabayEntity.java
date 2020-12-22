package cn.yjw.pixabayapp.entity.remote;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author yinjiawei
 * @date 2020/12/21
 */
public class PixabayEntity implements Parcelable {
    private int totalHits;
    private int total;
    @SerializedName("hits")
    private List<PhotoItemEntity> photoItemList;

    public PixabayEntity(int totalHits, int total, List<PhotoItemEntity> photoItemList) {
        this.totalHits = totalHits;
        this.total = total;
        this.photoItemList = photoItemList;
    }

    protected PixabayEntity(Parcel in) {
        totalHits = in.readInt();
        total = in.readInt();
        photoItemList = in.createTypedArrayList(PhotoItemEntity.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(totalHits);
        dest.writeInt(total);
        dest.writeTypedList(photoItemList);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<PixabayEntity> CREATOR = new Creator<PixabayEntity>() {
        @Override
        public PixabayEntity createFromParcel(Parcel in) {
            return new PixabayEntity(in);
        }

        @Override
        public PixabayEntity[] newArray(int size) {
            return new PixabayEntity[size];
        }
    };

    public int getTotalHits() {
        return totalHits;
    }

    public void setTotalHits(int totalHits) {
        this.totalHits = totalHits;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<PhotoItemEntity> getPhotoItemList() {
        return photoItemList;
    }

    public void setPhotoItemList(List<PhotoItemEntity> photoItemList) {
        this.photoItemList = photoItemList;
    }
}
