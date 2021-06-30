package com.harbin.mymall.mymallware.constant;

/**
 * @author Yuanzz
 * @creat 2021-03-11-14:16
 */
public enum WareLockStatusEnum {
        LOCKED(1,"已锁定"),
        UNLOCKED(2,"已解锁"),
        FINISHED(3,"已扣单");
        private Integer code;
        private String msg;

    WareLockStatusEnum(Integer code, String msg) {
            this.code = code;
            this.msg = msg;
        }

        public Integer getCode() {
            return code;
        }

        public String getMsg() {
            return msg;
        }
}
