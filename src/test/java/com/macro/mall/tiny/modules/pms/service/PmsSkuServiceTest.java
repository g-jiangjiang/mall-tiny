package com.macro.mall.tiny.modules.pms.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.macro.mall.tiny.modules.pms.dto.PmsSkuParam;
import com.macro.mall.tiny.modules.pms.model.PmsSku;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class PmsSkuServiceTest {

    @Autowired
    private PmsSkuService skuService;

    @Test
    public void testListSku() {
        Page<PmsSku> page = skuService.list(null, null, 5, 1);
        assertNotNull(page);
    }

    @Test
    public void testCreateSku() {
        PmsSkuParam param = new PmsSkuParam();
        param.setSpuId(1L);
        param.setSkuCode("SKU_TEST_001");
        param.setName("测试SKU");
        param.setPrice(new BigDecimal("99.99"));
        param.setStock(100);
        param.setSpecs("{\"颜色\":\"红色\",\"尺寸\":\"M\"}");
        param.setPic("http://example.com/sku.jpg");

        boolean result = skuService.create(param);
        assertTrue(result);
    }

    @Test
    public void testUpdateSku() {
        PmsSkuParam param = new PmsSkuParam();
        param.setName("更新后的SKU");
        param.setPrice(new BigDecimal("89.99"));
        param.setStock(80);
        param.setSpecs("{\"颜色\":\"蓝色\",\"尺寸\":\"L\"}");

        boolean result = skuService.update(1L, param);
        assertTrue(result);
    }

    @Test
    public void testDeleteSku() {
        boolean result = skuService.delete(999L);
        assertTrue(result);
    }

    @Test
    public void testBatchCreateSku() {
        List<PmsSkuParam> skuParams = new ArrayList<>();

        PmsSkuParam skuParam1 = new PmsSkuParam();
        skuParam1.setSkuCode("BATCH_SKU_001");
        skuParam1.setName("批量SKU1");
        skuParam1.setPrice(new BigDecimal("99.00"));
        skuParam1.setStock(100);
        skuParam1.setSpecs("{\"颜色\":\"红色\"}");
        skuParams.add(skuParam1);

        PmsSkuParam skuParam2 = new PmsSkuParam();
        skuParam2.setSkuCode("BATCH_SKU_002");
        skuParam2.setName("批量SKU2");
        skuParam2.setPrice(new BigDecimal("109.00"));
        skuParam2.setStock(50);
        skuParam2.setSpecs("{\"颜色\":\"蓝色\"}");
        skuParams.add(skuParam2);

        boolean result = skuService.batchCreate(1L, skuParams);
        assertTrue(result);
    }
}
