package org.jeecg.modules.publishlist.logic;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IteratePlaceholderLogic<T extends PlaceholderAble<S>, S> {

    public <T extends PlaceholderAble<S>, S> String replacePlaceholderInIterate(T t, List<S> list, String inContent) {
        String resultContent = "";
        List<String> strList = new ArrayList<>();

        for (S s : list) {

            Pattern pattern = Pattern.compile("\\$\\{.+?\\}");
            Matcher matcher = pattern.matcher(inContent);
            String tempContent = inContent;

            while (matcher.find()) {
                String group = matcher.group(0);
                tempContent = matcher.replaceFirst(t.getPlaceholderContent(group, s));

                matcher = pattern.matcher(tempContent);
            }
            strList.add(tempContent);

        }

        for (String str : strList) {
            resultContent = resultContent.concat(str.substring(1, str.length() - 1));//去掉字符串前后的大括弧
            //resultContent = resultContent.concat("\n\t");
        }
        return resultContent;
    }
}
