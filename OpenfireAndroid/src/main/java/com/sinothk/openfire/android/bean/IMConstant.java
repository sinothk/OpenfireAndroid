package com.sinothk.openfire.android.bean;

public class IMConstant {
    public static class Chat {
        public static final String CHAT_TARGET = "chatTarget";
        public static final String CHAT_TITLE = "chatTitle";
        public static final String CHAT_TYPE = "CHAT_TYPE";

        public static final int CHAT_TYPE_SINGLE = 1;
        public static final int CHAT_TYPE_ROOM = 2;
        public static final int CHAT_TYPE_GROUP = 3;
    }

    public static class ContentType {
        public static final String CONTENT_TEXT = "CONTENT_TEXT";
    }

    public static class FromType {
        public static final String SEND = "SEND";
        public static final String RECEIVE = "FROM_FRIEND";
    }
}
