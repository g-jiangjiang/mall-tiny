package com.macro.mall.tiny.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.macro.mall.tiny.entity.PmsSpu;

public interface PmsSpuService extends IService<PmsSpu> {

    boolean create(PmsSpu spu);

    boolean update(Long id, PmsSpu spu);

    boolean delete(Long id);

    Page<PmsSpu> list(String keyword, Long categoryId, Integer pageNum, Integer pageSize);

    PmsSpu getDetail(Long id);

    boolean updatePublishStatus(Long id, Integer publishStatus);

    boolean updateVerifyStatus(Long id, Integer verifyStatus);
}
