package com.macro.mall.tiny.modules.pms.service;

import com.macro.mall.tiny.modules.pms.dto.PmsSpecTypeParam;
import com.macro.mall.tiny.modules.pms.dto.PmsSpecValueParam;
import com.macro.mall.tiny.modules.pms.model.PmsSpecType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class PmsSpecTypeServiceTest {

    @Autowired
    private PmsSpecTypeService specTypeService;

    @Test
    public void testListWithValues() {
        List<PmsSpecType> specTypes = specTypeService.listWithValues();
        assertNotNull(specTypes);
    }

    @Test
    public void testCreateSpecType() {
        PmsSpecTypeParam param = new PmsSpecTypeParam();
        param.setName("颜色");
        param.setSort(0);

        List<PmsSpecValueParam> specValueParams = new ArrayList<>();
        PmsSpecValueParam valueParam1 = new PmsSpecValueParam();
        valueParam1.setValue("红色");
        valueParam1.setSort(0);
        specValueParams.add(valueParam1);

        PmsSpecValueParam valueParam2 = new PmsSpecValueParam();
        valueParam2.setValue("蓝色");
        valueParam2.setSort(1);
        specValueParams.add(valueParam2);

        param.setSpecValueList(specValueParams);

        boolean result = specTypeService.create(param);
        assertTrue(result);
    }

    @Test
    public void testUpdateSpecType() {
        PmsSpecTypeParam param = new PmsSpecTypeParam();
        param.setName("尺寸");
        param.setSort(1);

        List<PmsSpecValueParam> specValueParams = new ArrayList<>();
        PmsSpecValueParam valueParam1 = new PmsSpecValueParam();
        valueParam1.setValue("S");
        valueParam1.setSort(0);
        specValueParams.add(valueParam1);

        PmsSpecValueParam valueParam2 = new PmsSpecValueParam();
        valueParam2.setValue("M");
        valueParam2.setSort(1);
        specValueParams.add(valueParam2);

        param.setSpecValueList(specValueParams);

        boolean result = specTypeService.update(1L, param);
        assertTrue(result);
    }

    @Test
    public void testDeleteSpecType() {
        boolean result = specTypeService.delete(999L);
        assertTrue(result);
    }
}
