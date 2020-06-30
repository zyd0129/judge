package com.ps.judge.provider;

import org.drools.core.io.impl.UrlResource;
import org.junit.Test;
import org.kie.api.KieServices;
import org.kie.api.builder.KieModule;
import org.kie.api.builder.KieRepository;
import org.kie.api.io.Resource;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

public class KieSessionTest {
    @Test
    public void test() throws IOException {
        String url = "http://factoryfilestore.oss-cn-hangzhou.aliyuncs.com/2020-06-24/1592982825310.jar?Expires=1624086825&OSSAccessKeyId=LTAI4Ftbiuu38HBpvMeikex1&Signature=qmNl8re59APIRC6maMJxRZF7GRI%3D";
        KieServices kieServices = KieServices.Factory.get();
        KieRepository kieRepository = kieServices.getRepository();
        UrlResource urlResource = (UrlResource) kieServices.getResources().newUrlResource(url);
        InputStream is = null;
        is = urlResource.getInputStream();
        //获取资源
        Resource resource = kieServices.getResources().newInputStreamResource(is);

        //获取加载资源获取KieModule
        KieModule kieModule = kieRepository.addKieModule(resource);
        //通过kieModule的ReleaseId获取kieContainer
        KieContainer kieContainer = kieServices.newKieContainer(kieModule.getReleaseId());

        KieSession kieSession = kieContainer.newKieSession();

        System.out.println("end");

    }
}
