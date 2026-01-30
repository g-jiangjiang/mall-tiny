package com.macro.mall.tiny.service;

import com.macro.mall.tiny.entity.PmsSpu;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PmsSpuServiceTest {

    @Autowired
    private PmsSpuService spuService;

    private static Long testSpuId;

    @Test
    @Order(1)
    public void testCreate() {
        PmsSpu spu = new PmsSpu();
        spu.setName("iPhone 15 Pro");
        spu.setBrandId(1L);
        spu.setCategoryId(1L);
        spu.setProductCategoryName("手机");
        spu.setBrandName("苹果");
        spu.setPic("iphone.jpg");
        spu.setProductSn("IPHONE15PRO");
        spu.setPublishStatus(1);
        spu.setNewStatus(1);
        spu.setRecommandStatus(1);
        spu.setVerifyStatus(0);
        spu.setSort(1);
        spu.setSale(1000L);
        spu.setPrice(new BigDecimal("8999.00"));
        spu.setPromotionPrice(new BigDecimal("7999.00"));
        spu.setGiftGrowth(100);
        spu.setGiftPoint(50);
        spu.setUsePointLimit(1000);
        spu.setSubTitle("最新款苹果手机");
        spu.setDescription("苹果年度旗舰手机");
        spu.setOriginalPrice(new BigDecimal("9999.00"));
        spu.setStock(500);
        spu.setLowStock(10);
        spu.setUnit("台");
        spu.setWeight(new BigDecimal("0.2"));
        spu.setPreviewStatus(1);
        spu.setServiceIds("1,2,3");
        spu.setKeywords("iphone,apple,phone");
        spu.setNote("高端机型");
        spu.setAlbumPics("pic1.jpg,pic2.jpg");
        spu.setDetailTitle("iPhone 15 Pro 详细介绍");
        spu.setDetailDesc("采用最新A17 Pro芯片");
        spu.setDetailHtml("<h1>iPhone 15 Pro</h1>");
        spu.setDetailMobileHtml("<h1>iPhone 15 Pro</h1>");
        spu.setPromotionStartTime(null);
        spu.setPromotionEndTime(null);
        spu.setPromotionPerLimit(0);
        spu.setPromotionType(0);
        spu.setBrandLogo("apple_logo.png");

        boolean result = spuService.create(spu);
        assertTrue(result);
        assertNotNull(spu.getId());
        testSpuId = spu.getId();
    }

    @Test
    @Order(2)
    public void testUpdate() {
        PmsSpu spu = new PmsSpu();
        spu.setId(testSpuId);
        spu.setName("iPhone 15 Pro Max");
        spu.setPrice(new BigDecimal("9999.00"));

        boolean result = spuService.update(testSpuId, spu);
        assertTrue(result);

        PmsSpu updated = spuService.getById(testSpuId);
        assertEquals("iPhone 15 Pro Max", updated.getName());
    }

    @Test
    @Order(3)
    public void testUpdatePublishStatus() {
        boolean result = spuService.updatePublishStatus(testSpuId, 0);
        assertTrue(result);

        PmsSpu updated = spuService.getById(testSpuId);
        assertEquals(0, updated.getPublishStatus());
    }

    @Test
    @Order(4)
    public void testUpdateVerifyStatus() {
        boolean result = spuService.updateVerifyStatus(testSpuId, 1);
        assertTrue(result);

        PmsSpu updated = spuService.getById(testSpuId);
        assertEquals(1, updated.getVerifyStatus());
    }

    @Test
    @Order(5)
    public void testGetById() {
        PmsSpu spu = spuService.getById(testSpuId);
        assertNotNull(spu);
        assertEquals(testSpuId, spu.getId());
    }

    @Test
    @Order(6)
    public void testDelete() {
        boolean result = spuService.delete(testSpuId);
        assertTrue(result);

        PmsSpu deleted = spuService.getById(testSpuId);
        assertNull(deleted);
    }
}
