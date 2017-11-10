//package common;
//
///**
// * Prefix1.0 control|text
// *
// * body
// */
//public class PostCryptProtocol {
//
//    private static String HEADER_PROTOCOL = "PostCrypt1.0~";
//    private static String HEADER_CONTROL = "control~";
//    private static String HEADER_TEXT = "text~";
//
//    public static int TYPE_CONTROL = 1;
//    public static int TYPE_TEXT = 2;
//
//    public boolean isControl;
//    public boolean isText;
//
//    private boolean enableConfidentiality = false;
//    private boolean enableIntegrity = false;
//    private boolean enableAuthentication = false;
//
//    public String body;
//
//    public PostCryptProtocol(int type, String body) {
//        if (type == TYPE_CONTROL) {
//            this.isControl = true;
//            this.isText = false;
//        } else if (type == TYPE_TEXT) {
//            this.isControl = false;
//            this.isText = true;
//        }
//        this.body = body;
//    }
//
//    public PostCryptProtocol(String recievedString) {
//        System.out.println(recievedString);
//        String[] tokens = recievedString.split("~");
//
//        if (tokens[1].equals(TYPE_CONTROL)) {
//            this.isControl = true;
//            this.isText = false;
//        } else if (tokens[1].equals(TYPE_TEXT)) {
//            this.isControl = false;
//            this.isText = true;
//        }
//
//        this.body = tokens[2];
//
//    }
//
//    @Override
//    public String toString() {
//        String str = "";
//        str += HEADER_PROTOCOL;
//        if (isControl) {
//            str += HEADER_CONTROL;
//        } else if (isText) {
//            str += HEADER_TEXT;
//        }
//        str += body;
////        System.out.println("tostring");
////        System.out.println(str);
//        return str;
//    }
//
//}
