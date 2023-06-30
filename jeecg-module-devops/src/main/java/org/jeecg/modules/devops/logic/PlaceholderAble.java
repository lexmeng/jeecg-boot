package org.jeecg.modules.devops.logic;

public interface PlaceholderAble<T> {
    /**
     * 获得占位符对应的内容
     * @return
     */
    public String getPlaceholderContent(String placeholder, T t);
}
