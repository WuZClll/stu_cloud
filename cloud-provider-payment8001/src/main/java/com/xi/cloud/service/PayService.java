package com.xi.cloud.service;

import com.xi.cloud.entities.Pay;

import java.util.List;

/**
 * @author ZC_Wu 汐
 * @date 2024/8/6 19:52:25
 * @description
 */
public interface PayService {
    public int add(Pay pay);
    public int delete(Integer id);
    public int update(Pay pay);
    public Pay getById(Integer id);
    public List<Pay> getAll();
}
