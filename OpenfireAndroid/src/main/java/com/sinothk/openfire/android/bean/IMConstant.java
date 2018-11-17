package com.sinothk.openfire.android.bean;

public class IMConstant {
    public static class Chat {
        public static final String CHAT_TARGET = "chatTarget";
        public static final String CHAT_TITLE = "chatTitle";
        public static final String CHAT_TYPE = "CHAT_TYPE";

        @Deprecated
        public static final int CHAT_TYPE_SINGLE = 1;
        @Deprecated
        public static final int CHAT_TYPE_ROOM = 2;
        @Deprecated
        public static final int CHAT_TYPE_GROUP = 3;
    }

    public static class ChatType {
        public static final String SINGLE = "single_chat";
        public static final String ROOM = "room_chat";
        public static final String GROUP = "group_chat";
    }


    public static class ContentType {
        @Deprecated
        public static final String CONTENT_TEXT = "CONTENT_TEXT";

        public static final String TEXT = "TEXT";
        public static final String IMAGE = "IMAGE";
        public static final String FILE = "FILE";
        public static final String LOCATION = "LOCATION";
    }

    public static class FromType {
        public static final String SEND = "SEND";
        public static final String RECEIVE = "FROM_FRIEND";
    }

    public static class MsgStatus {
        public static final int READ = 0;// 已读
        public static final int UNREAD = 1;// 未读
    }
}
