package org.jeecg.modules.publishlist.domainservice;

public interface IPublishlistDomainService {
    public void publish(String publishlistId);

    public boolean isPublished(String publishlistId);
}
