package org.server.remoteclass.controller;

import io.swagger.annotations.ApiOperation;
import org.server.remoteclass.dto.issuedcoupon.RequestIssuedCouponDto;
import org.server.remoteclass.dto.issuedcoupon.ResponseIssuedCouponDto;
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
    @ApiOperation(value = "내가 발급받은 모든 쿠폰 조회")
    @GetMapping
    public ResponseEntity<List<ResponseIssuedCouponDto>> getAllCoupons() throws IdNotExistException{
        return ResponseEntity.status(HttpStatus.OK).body(issuedCouponService.getAllMyCoupons());
    }

    //내가 가진 쿠폰 상세보기
    @ApiOperation(value = "내가 발급받은 쿠폰 번호를 톷해 조회")
    @GetMapping("/{couponId}")
    public ResponseEntity<ResponseIssuedCouponDto> getCoupon(@PathVariable("couponId") Long couponId) throws IdNotExistException{
        return ResponseEntity.status(HttpStatus.OK).body(issuedCouponService.getMyCoupon(couponId));
    }

    //쿠폰 코드 입력해서 발급받기
    @PostMapping
    @ApiOperation(value = "쿠폰 발급받기", notes = "쿠폰을 발급받아 내 쿠폰함에 생성한다.")
    public ResponseEntity issueCoupon(@RequestBody @Valid RequestIssuedCouponDto requestIssuedCouponDto) throws IdNotExistException{
        issuedCouponService.issueCoupon(requestIssuedCouponDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    //쿠폰 사용하기의 경우, 결제 단계에서 사용되어야 한다고 생각해서 잠시 보류하겠습니다.

}