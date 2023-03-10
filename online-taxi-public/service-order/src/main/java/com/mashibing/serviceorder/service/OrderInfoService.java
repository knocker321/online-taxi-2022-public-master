package com.mashibing.serviceorder.service;

import com.mashibing.internalcommon.dto.OrderInfo;
import com.mashibing.internalcommon.dto.ResponseResult;
import com.mashibing.internalcommon.request.OrderRequest;
import com.mashibing.serviceorder.mapper.OrderInfoMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author cpf
 * @since 2022-10-10
 */
@Service
public class OrderInfoService {

    @Autowired
    OrderInfoMapper orderInfoMapper;

    public ResponseResult add(OrderRequest orderRequest){
        OrderInfo orderInfo = new OrderInfo();

        BeanUtils.copyProperties(orderRequest,orderInfo);

        orderInfoMapper.insert(orderInfo);
        return ResponseResult.success();
    }
}
