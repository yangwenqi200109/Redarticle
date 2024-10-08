package com.ape.waterwemedia.test;

import com.ape.waterwemedia.WaterWemediaApplication;
import com.ape.waterwemedia.service.WmNewsAutoScanService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest(classes = WaterWemediaApplication.class)
@RunWith(SpringRunner.class)
public class Test1 {
    @Autowired
    private WmNewsAutoScanService wmNewsAutoScanService;
    @Test
    public void autoScanWmNews() {
        wmNewsAutoScanService.autoScanWmNews(6252);
    }
}