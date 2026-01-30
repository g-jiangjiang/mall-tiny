package com.macro.mall.tiny.service;

import com.macro.mall.tiny.entity.PmsSku;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PmsSkuServiceTest {

    @Autowired
    private PmsSkuService skuService;

    private static Long testSkuId;

    @Test
    @Order(1)
    public void testCreate() {
        PmsSku sku = new PmsSku();
        sku.setProductId(1L);
        sku.setSkuCode("SKU001");
        sku.setPrice(new BigDecimal("6999.00"));
        sku.setStock(100);
        sku.setLowStock(10);
        sku.setSp1("红色");
        sku.setSp2("256GB");
        sku.setSp3("中国大陆");
        sku.setPic("sku_pic.jpg");
        sku.setSale(50);
        sku.setPromotionPrice(new BigDecimal("6499.00"));
        sku.setLockStock(5);
        sku.setSpData("{\"color\":\"红色\",\"storage\":\"256GB\"}");

        boolean result = skuService.create(sku);
        assertTrue(result);
        assertNotNull(sku.getId());
        testSkuId = sku.getId();
    }

    @Test
    @Order(2)
    public void testUpdate() {
        PmsSku sku = new PmsSku();
        sku.setId(testSkuId);
        sku.setPrice(new BigDecimal("6899.00"));
        sku.setStock(150);

        boolean result = skuService.update(testSkuId, sku);
        assertTrue(result);

        PmsSku updated = skuService.getById(testSkuId);
        assertEquals(new BigDecimal("6899.00"), updated.getPrice());
    }

    @Test
    @Order(3)
    public void testBatchCreate() {
        PmsSku sku1 = new PmsSku();
        sku1.setProductId(1L);
        sku1.setSkuCode("SKU002");
        sku1.setPrice(new BigDecimal("7999.00"));
        sku1.setStock(80);

        PmsSku sku2 = new PmsSku();
        sku2.setProductId(1L);
        sku2.setSkuCode("SKU003");
        sku2.setPrice(new BigDecimal("8999.00"));
        sku2.setStock(60);

        List<PmsSku> skuList = Arrays.asList(sku1, sku2);
        boolean result = skuService.batchCreate(skuList);
        assertTrue(result);
    }

    @Test
    @Order(4)
    public void testUpdateStock() {
        boolean result = skuService.updateStock(testSkuId, 200);
        assertTrue(result);

        PmsSku updated = skuService.getById(testSkuId);
        assertEquals(200, updated.getStock());
    }

    @Test
    @Order(5)
    public void testUpdatePrice() {
        boolean result = skuService.updatePrice(testSkuId, new BigDecimal("6699.00"));
        assertTrue(result);

        PmsSku updated = skuService.getById(testSkuId);
        assertEquals(new BigDecimal("6699.00"), updated.getPrice());
    }

    @Test
    @Order(6)
    public void testGetByProductId() {
        List<PmsSku> skuList = skuService.getByProductId(1L);
        assertNotNull(skuList);
    }

    @Test
    @Order(7)
    public void testGetById() {
        PmsSku sku = skuService.getById(testSkuId);
        assertNotNull(sku);
        assertEquals(testSkuId, sku.getId());
    }

    @Test
    @Order(8)
    public void testDelete() {
        boolean result = skuService.delete(testSkuId);
        assertTrue(result);

        PmsSku deleted = skuService.getById(testSkuId);
        assertNull(deleted);
    }
}
