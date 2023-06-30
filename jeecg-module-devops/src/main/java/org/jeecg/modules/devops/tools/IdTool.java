package org.jeecg.modules.devops.tools;

import java.util.UUID;

public class IdTool {
    public static String generalId(){
        String uuid = UUID.randomUUID().toString();
        String[] uuidStrList = uuid.split("-");

        return uuidStrList[2]+uuidStrList[3]+uuidStrList[4].substring(0,4);
    }

}
