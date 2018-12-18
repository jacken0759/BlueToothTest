package com.example.demo.blueTooth;



public class WZUtil {
    static final int MODE_UNKNOWN = 0;
    static final int MODE_GET_RAND = 1;
    static final int MODE_SET_RAND = 2;
    static final int MODE_VALID = 3;
    static int gsSequence  = 0;
    static int btmode = MODE_UNKNOWN;
    String    password;
    public  boolean btencode = true;
    //跟密钥
    public static String key = "2d9ac79128a5d5ae29fec3b2797725bd";
    public static String jiaohangKey = "";


    /**
     * 重置蓝牙连接参数参数
     */
    public void resetParams(){
        jiaohangKey = "";
        gsSequence  = 0;
    }


    public String cmdValid(){
        password = "303030303030";
        resetParams();
        String cmd = getCmd("33", password, btencode);
        return cmd;
    }

    public String cmdTimer(){
        String cmd = getCmd("34", String.format("%02X", 1), false);
        return cmd;
    }
    public String cmdBeacon(String msg){
        //	FF550930012F038C0100C0B900
        String cmd = getCmd("39", msg, btencode);
        return cmd;
    }
    public String getCmd(String type, String msg, boolean en){
        String cmd, enmsg = "",body;
        boolean haveJFlag = false;
        if(StringUtil.isNullOrEmpty(jiaohangKey)){
            haveJFlag = false;
        } else {
            haveJFlag = true;
        }
        int tlen,blen,flen,shl = 0;
        int mlen = 14;
        char send[];
        gsSequence ++;
        if(gsSequence > 255){
            gsSequence = 0;
        }

        msg += "0000000000000000000000000000";
        msg = msg.substring(0, 30);

//        if(_req != null){
//            if(_req._seq.isEmpty()) {
//                _req._seq = String.format("%02X", gsSequence);
//            }
//        }

        flen = 0;

        blen = mlen + flen + 4;
        tlen = blen;
        if(en){
            if(!haveJFlag){

                cmd = String.format("FF11%s%02X%s", type, gsSequence, msg);
            } else {
                cmd = String.format("FF12%s%02X%s", type, gsSequence, msg);
            }
        } else {
            cmd = String.format("FF00%s%02X%s", type, gsSequence, msg);
        }

        send = Hex.hex2char(cmd);
        int len = send.length;
        int sum = 0;

        for(int i = 0; i < len; i ++){
            sum += send[i];
        }
        shl = sum % 256;

        blen = mlen + flen + 4;
        tlen = blen;
        if(en){
            cmd = String.format("%02X%s", gsSequence, msg);


            if(!haveJFlag) {
                enmsg = AESEbcHelper.encrypt(cmd, key);
                cmd = String.format("FF11%s%s", type,enmsg);
            } else {
                enmsg = AESEbcHelper.encrypt(cmd, jiaohangKey);
                cmd = String.format("FF12%s%s", type,enmsg);

            }
        } else {
            flen = 0;

            blen = mlen + flen + 4;
            tlen = blen;
            cmd = String.format("FF00%s%02X%s", type, gsSequence, msg);
        }


        cmd = String.format("%s%02X", cmd, shl);

        return cmd;
    }


    public String cmdMake(String msg){
        String cmd;
        char send[];
        int shl;
        int len = msg.length()/2 + 2;
        cmd = "AA55" + String.format("%02X", len);
        cmd += msg;

        send = Hex.hex2char(cmd);
        len = send.length;
        int sum = 0;

        for(int i = 2; i < len; i ++) {
            sum += send[i];
        }
        shl = sum % 256;

        cmd += String.format("%02X", shl);
        cmd = getCmd("35", cmd, btencode);
        return cmd;
    }



//
//    public boolean parseMsg(String rsp, BtMsg msg){
//        String en,dn,encd;
//        if(rsp.length() < 6)
//        {
//            return false;
//        }
////        if(rsp.equals("FF050200000208"))
//        if(rsp.substring(0, 8).equals("FF050200"))
//        {
////			ToastUtil.makeText(rsp, Gravity.CENTER);
//            btmode = MODE_SET_RAND;
//            return false;
//        }
//
//        if(chkRsp(rsp)){
//            try{
//                msg.pack = rsp;
//                if(btencode){
//                    String sk = rsp.substring(2,4);
//                    if(sk.equals("11")){
//                        en = rsp.substring(6, rsp.length() - 2);
//                        if(en.length() % 32 == 0){
//                            dn = AESEbcHelper.decrypt(en, key );
//                            rsp = rsp.substring(0, 6) + dn + rsp.substring(rsp.length() - 2);
//                        }
//                        else {
////                        Log.e("ble invalid response", rsp);
//                            return false;
//                        }
//                    } else if(sk.equals("12")){
//                        en = rsp.substring(6, rsp.length() - 2);
//                        if(en.length() % 32 == 0){
//                            dn = AESEbcHelper.decrypt(en, btrand);
//                            rsp = rsp.substring(0, 6) + dn + rsp.substring(rsp.length() - 2);
//                        }
//                        else {
////                        Log.e("ble invalid response", rsp);
//                            return false;
//                        }
//                    }
//// 					Log.e("EXTRA_DATA", rsp);
//                }
////				ToastUtil.makeText("dis:" + UtilsIBeacon.getDis() + ":" + rsp, Gravity.CENTER);
//
//                msg.type = rsp.substring(4, 6);
//                msg.trsq = rsp.substring(6, 8);
//                msg.msg = rsp.substring(8, rsp.length() - 2);
//                msg.trcd = msg.type;
//                if(msg.msg == null){
//                    msg.msg = "";
//                }
//            }catch(StringIndexOutOfBoundsException e){
//                e.printStackTrace();
//            }catch(NumberFormatException e){
//                e.printStackTrace();
//            }
//
//            return true;
//        } else {
////            Log.e("BLECY002", "invalid response:" + rsp);
//            return false;
//        }
//    }
}
