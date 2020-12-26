package cn.yjw.pixabayapp.appenum;

/**
 * @author yinjiawei
 * @date 2020/12/22
 */
public enum PixabayUrlParamEnum {
    QUERY("q"), LAGUAGE("lang"), ID("id"),
    IMAGETYPE("image_type"), ORIENTATION("orientation"), CATEGORY("category"),
    ORDER("order"), PAGE("page"), PERPAGE("per_page");

    private final String key;

    PixabayUrlParamEnum(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
