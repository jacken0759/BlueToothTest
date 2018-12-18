package com.example.demo.blueTooth;

import java.util.Arrays;

public class BTTest {

    static final int MODE_UNKNOWN = 0;
    static final int MODE_GET_RAND = 1;
    static final int MODE_SET_RAND = 2;
    static final int MODE_VALID = 3;
    static int gsSequence  = 0;
    static int btmode = MODE_UNKNOWN;

    public static boolean btencode = true;
    //跟密钥
    public static String key = "2d9ac79128a5d5ae29fec3b2797725bd";
    public static String jiaohangKey = "";

    public static void main(String[] args) {
        String password = "303030303030";
        String cmd = getCmd("33", password, btencode);
        System.out.println(cmd);//FF1133DABA57B37637AE8BF5A01D8CAB8EBD4964
        char[] buf = Hex.decodeHex("FF1133B73F1D7BAC33F719268D36FFCA2DBB3964".toCharArray());
        byte[] bytes3 = Hex.getBytes(buf);
        System.out.println(Arrays.toString(bytes3));

        String str = AESEbcHelper.decrypt("5e097f68330bcced6f2a25c33e0522b0", WZUtil.key);
        System.out.println(str);

        String jiami = AESEbcHelper.encrypt("01C2B6D119F8B4F7478E927D55845063",WZUtil.key);
        System.out.println(jiami);


        String tem = "ff113310cc10e455165324ba8464e1dbf3ba2e2e";

        System.out.println("收到蓝牙回传,解密前：" + tem);

        if (cmd.equalsIgnoreCase("69")) {

        } else {
            String en = tem.substring(6, tem.length() - 2);
            if (en.length() % 32 == 0) {
                if (StringUtil.isNullOrEmpty(WZUtil.jiaohangKey)) {
                    String dn = AESEbcHelper.decrypt(en, WZUtil.key);
                    tem = tem.substring(0, 6) + dn + tem.substring(tem.length() - 2);
                } else {
                    String dn = AESEbcHelper.decrypt(en, WZUtil.jiaohangKey);
                    tem = tem.substring(0, 6) + dn + tem.substring(tem.length() - 2);
                }

            }
//                    if (onBlueLogListering != null) {
//                        onBlueLogListering.getLog("收到蓝牙回传4,解密后：" + tem, value);
//                    }
//                    LogUtil.tLogD("收到蓝牙回传4,解密后：" + tem);
//            log("收到蓝牙回传4,解密后：" + tem, value);
            System.out.println("收到蓝牙回传4,解密后：" + tem);
        }


        int[] arr = {-1, 17, 51, -73, 63, 29, 123, -84, 51, -9, 25, 38, -115, 54, -1, -54, 45, -69, 57, 100};
        int sum = 0;
        for (int i = 0; i < arr.length; i++) {
            sum += arr[i];
        }
        System.out.println("和："+sum);

    }



    public static String getCmd(String type, String msg, boolean en){
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
        System.out.println(Arrays.toString(send));
        for(int i = 0; i < len; i ++){
            sum += send[i];
        }
        shl = sum % 256;

        blen = mlen + flen + 4;
        tlen = blen;
        System.out.println("cmd加密之前:"+cmd);
        System.out.println("sum:"+sum);
        System.out.println("shl:"+shl);
        char[] buf = Hex.decodeHex(cmd.toCharArray());
        byte[] bytes3 = Hex.getBytes(buf);
        System.out.println(Arrays.toString(bytes3));


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
}
