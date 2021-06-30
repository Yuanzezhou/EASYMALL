package com.harbin.mymall.mymallmember.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

//import org.apache.shiro.authz.annotation.RequiresPermissions;
import com.harbin.common.exception.BizCodeEnum;
import com.harbin.mymall.mymallmember.entity.MemberReceiveAddressEntity;
import com.harbin.mymall.mymallmember.exception.PhoneExistException;
import com.harbin.mymall.mymallmember.exception.UserNameExistException;
import com.harbin.mymall.mymallmember.feign.CouponFeignService;
import com.harbin.mymall.mymallmember.service.MemberReceiveAddressService;
import com.harbin.mymall.mymallmember.vo.MemberLoginVo;
import com.harbin.mymall.mymallmember.vo.MemberRegistVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.harbin.mymall.mymallmember.entity.MemberEntity;
import com.harbin.mymall.mymallmember.service.MemberService;
import com.harbin.common.utils.PageUtils;
import com.harbin.common.utils.R;



/**
 * 会员
 *
 * @author yuanzz
 * @email 1002271900@qq.com
 * @date 2021-01-14 21:12:51
 */
@RestController
@RequestMapping("mymallmember/member")
public class MemberController {
    @Autowired
    private MemberService memberService;

    @Autowired
    private CouponFeignService couponFeignService;

    @Autowired
    MemberReceiveAddressService memberReceiveAddressService;

    @GetMapping("/{memberId}/addresses")
    public List<MemberReceiveAddressEntity> getAddress(@PathVariable("memberId") Long memberId){
        return memberReceiveAddressService.getAddress(memberId);
    }

    @RequestMapping("/coupons")
    public R test(){
        MemberEntity memberEntity = new MemberEntity();
        memberEntity.setNickname("张三");
        R membercoupons = couponFeignService.membercoupons();
        return membercoupons.ok().put("member", memberEntity).put("coupons", membercoupons.get("coupons"));
    }

    /**
     * 列表
     */
    @RequestMapping("/list")
    //@RequiresPermissions("mymallmember:member:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = memberService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    //@RequiresPermissions("mymallmember:member:info")
    public R info(@PathVariable("id") Long id){
        MemberEntity member = memberService.getById(id);

        return R.ok().put("member", member);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("mymallmember:member:save")
    public R save(@RequestBody MemberEntity member){
		memberService.save(member);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("mymallmember:member:update")
    public R update(@RequestBody MemberEntity member){
		memberService.updateById(member);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("mymallmember:member:delete")
    public R delete(@RequestBody Long[] ids){
		memberService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }


    @PostMapping("/regist")
    //接收json数据要用@RequestBody注解
    public R regist(@RequestBody MemberRegistVo vo){
        try{
            memberService.regist(vo);
            //异常机制：通过捕获对应的自定义异常判断出现何种错误并封装错误信息
        }catch (PhoneExistException e){
            return R.error(BizCodeEnum.PHONE_EXIST_EXCEPTION.getCode(), BizCodeEnum.PHONE_EXIST_EXCEPTION.getMsg());
        }catch (UserNameExistException e){
            return R.error(BizCodeEnum.USER_EXIST_EXCEPTION.getCode(),BizCodeEnum.USER_EXIST_EXCEPTION.getMsg());
        }
        return R.ok();
    }


    @PostMapping("/login")
    public R login(@RequestBody MemberLoginVo vo){
        MemberEntity entity = memberService.login(vo);
        if (entity != null){
            return R.ok().setData(entity);
        }else {
            return R.error(BizCodeEnum.LOGINACCT_PASSWORD_INVAILD_EXCEPTION.getCode(),BizCodeEnum.LOGINACCT_PASSWORD_INVAILD_EXCEPTION.getMsg());
        }
    }
}
