package com.hgq.security.consts;

public enum AuthorityEnum {
    ;
    /** 权限类型 */
    public enum Type {
        /** 接口 */
        API(1),
        /** 菜单 */
        MENU(2),
        /** 按钮 */
        BUTTON(3),
        /** 目录 */
        FOLDER(4)
        ;
        private int value;

        Type(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        public static Type of(int value) {
            for (Type type : values()) {
                if (type.getValue() == value) {
                    return type;
                }
            }
            throw new IllegalArgumentException("No matching constant for [" + value + "]");
        }
    }
}
