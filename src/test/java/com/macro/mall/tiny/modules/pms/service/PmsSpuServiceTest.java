package com.macro.mall.tiny.modules.pms.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.macro.mall.tiny.modules.pms.dto.PmsSkuParam;
import com.macro.mall.tiny.modules.pms.dto.PmsSpuParam;
import com.macro.mall.tiny.modules.pms.model.PmsSpu;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class PmsSpuServiceTest {

    @Autowired
    private PmsSpuService spuService;

    @Test
    public void testListSpu() {
        Page<PmsSpu> page = spuService.list(null, 5, 1);
        assertNotNull(page);
    }

    @Test
    public void testCreateSpu() {
        PmsSpuParam param = new PmsSpuParam();
        param.setName("测试商品SPU");
        param.setBrandId(1L);
        param.setCategoryId(1L);
        param.setDetail("商品详情");
        param.setPic("http://example.com/pic.jpg");
        param.setUnit("件");
        param.setDescription("商品描述");
        param.setSort(0);

        List<PmsSkuParam> skuParams = new ArrayList<>();
        PmsSkuParam skuParam1 = new PmsSkuParam();
        skuParam1.setSkuCode("SKU001");
        skuParam1.setName("测试SKU1");
        skuParam1.setPrice(new BigDecimal("99.00"));
        skuParam1.setStock(100);
        skuParam1.setSpecs("{\"颜色\":\"红色\"}");
        skuParams.add(skuParam1);

        PmsSkuParam skuParam2 = new PmsSkuParam();
        skuParam2.setSkuCode("SKU002");
        skuParam2.setName("测试SKU2");
        skuParam2.setPrice(new BigDecimal("109.00"));
        skuParam2.setStock(50);
        skuParam2.setSpecs("{\"颜色\":\"蓝色\"}");
        skuParams.add(skuParam2);

        param.setSkuList(skuParams);

        boolean result = spuService.create(param);
        assertTrue(result);
    }

    @Test
    public void testUpdateSpu() {
        PmsSpuParam param = new PmsSpuParam();
        param.setName("更新后的商品SPU");
        param.setBrandId(1L);
        param.setCategoryId(1L);
        param.setDetail("更新后的商品详情");
        param.setSort(1);

        List<PmsSkuParam> skuParams = new ArrayList<>();
        PmsSkuParam skuParam1 = new PmsSkuParam();
        skuParam1.setSkuCode("SKU001");
        skuParam1.setName("更新后的SKU1");
        skuParam1.setPrice(new BigDecimal("89.00"));
        skuParam1.setStock(80);
        skuParam1.setSpecs("{\"颜色\":\"红色\"}");
        skuParams.add(skuParam1);

        param.setSkuList(skuParams);

        boolean result = spuService.update(1L, param);
        assertTrue(result);
    }

    @Test
    public void testDeleteSpu() {
        boolean result = spuService.delete(999L);
        assertTrue(result);
    }

    @Test
    public void testGetDetail() {
        PmsSpu spu = spuService.getDetail(1L);
        assertNotNull(spu);
    }
}
