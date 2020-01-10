package cn.kanyun.qurtzjdbc.entity;

/**
 * Created by HLWK-06 on 2019/6/17.
 */
public enum JobStatus {
    NONE("无"),
    NORMAL("正常"),
    PAUSED("暂停"),
    COMPLETE("完成"),
    ERROR("错误"),
    BLOCKED("阻塞");

    private String info;

    JobStatus(String info) {
        this.info = info;
    }


    @Override
    public String toString() {
        return info;
    }

    public static void main(String[] args) {
        System.out.println(BLOCKED.toString());
    }
}
