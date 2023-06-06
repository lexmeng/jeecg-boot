package org.jeecg.modules.publishlist.domainservice;

public interface IPublishlistDomainService {

    public void develop(String publishlistId);

    public void test(String publishlistId);

    public void publish(String publishlistId);

    public boolean isPublished(String publishlistId);
}
