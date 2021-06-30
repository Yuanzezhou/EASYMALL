package com.harbin.mymall.mymallmember.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.harbin.common.utils.PageUtils;
import com.harbin.mymall.mymallmember.entity.MemberEntity;
import com.harbin.mymall.mymallmember.exception.PhoneExistException;
import com.harbin.mymall.mymallmember.exception.UserNameExistException;
import com.harbin.mymall.mymallmember.vo.MemberLoginVo;
import com.harbin.mymall.mymallmember.vo.MemberRegistVo;

import java.util.Map;

/**
 * 会员
 *
 * @author yuanzz
 * @email 1002271900@qq.com
 * @date 2021-01-14 21:12:51
 */
public interface MemberService extends IService<MemberEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void regist(MemberRegistVo vo);

    void checkPhoneUnique(String phone) throws PhoneExistException;

    void checkUserNameUnique(String userName) throws UserNameExistException;

    MemberEntity login(MemberLoginVo vo);
}

