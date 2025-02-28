package com.my;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.model.Bucket;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;

@SpringBootTest
class CarRentalBackendApplicationTests {

    @Resource
    private COSClient cosClient;

    @Test
    void contextLoads() {
        List<Bucket> buckets = cosClient.listBuckets();
        for (Bucket bucket : buckets) {
            System.out.println(bucket.getName());
            System.out.println(bucket.getLocation());
        }
    }

}
