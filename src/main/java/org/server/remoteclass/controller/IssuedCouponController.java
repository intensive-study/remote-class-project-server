package org.server.remoteclass.controller;

import org.server.remoteclass.dto.IssuedCouponDto;
import org.server.remoteclass.exception.IdNotExistException;
import org.server.remoteclass.service.IssuedCouponService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/mycoupons") // 어감이 좀 이상해서 수정할 수도 있습니다. issuedcoupons이 아니니 조심하세요!
public class IssuedCouponController {

    private final IssuedCouponService issuedCouponService;

    public IssuedCouponController(IssuedCouponService issuedCouponService){
        this.issuedCouponService = issuedCouponService;
    }

    //내가 가진 모든 쿠폰 보기
    @GetMapping
    public ResponseEntity<List<IssuedCouponDto>> getAllCoupons() throws IdNotExistException{
        return ResponseEntity.status(HttpStatus.OK).body(issuedCouponService.getAllMyCoupons());
    }

    //내가 가진 쿠폰 상세보기
    @GetMapping("/{couponId}")
    public ResponseEntity<IssuedCouponDto> getCoupon(@PathVariable("couponId") Long couponId) throws IdNotExistException{
        return ResponseEntity.status(HttpStatus.OK).body(issuedCouponService.getMyCoupon(couponId));
    }

    //쿠폰 코드 입력해서 발급받기
    @PostMapping
    public ResponseEntity<IssuedCouponDto> issueCoupon(@RequestBody @Valid IssuedCouponDto issuedCouponDto) throws IdNotExistException{
        return ResponseEntity.status(HttpStatus.CREATED).body(issuedCouponService.issueCoupon(issuedCouponDto));
    }

    //쿠폰 사용하기의 경우, 결제 단계에서 사용되어야 한다고 생각해서 잠시 보류하겠습니다.

}