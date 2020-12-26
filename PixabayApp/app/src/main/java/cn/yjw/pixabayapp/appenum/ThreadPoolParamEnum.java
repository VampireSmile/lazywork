package cn.yjw.pixabayapp.appenum;

/**
 * @author yinjiawei
 * @date 2020/12/24
 */
public enum ThreadPoolParamEnum {
    CORE_POOL_SIZE("CORE_POOL_SIZE"), MAX_POOL_SIZE("MAX_POOL_SIZE"), KEEP_ALIVE_TIME("KEEP_ALIVE_TIME"),
    TIME_UNIT("TIME_UNIT"), BLOCK_QUEUE_CAPACITY("BLOCK_QUEUE_CAPACITY"), FACTORY("FACTORY"),
    REJECTED_HANDLER("REJECTED_HANDLER");
    private final String key;

    ThreadPoolParamEnum(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
