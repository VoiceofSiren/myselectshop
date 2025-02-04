package com.sparta.myselectshop.controller;

import com.sparta.myselectshop.dto.ProductMyPriceRequestDto;
import com.sparta.myselectshop.dto.ProductRequestDto;
import com.sparta.myselectshop.dto.ProductResponseDto;
import com.sparta.myselectshop.security.UserDetailsImpl;
import com.sparta.myselectshop.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ProductController {

    private final ProductService productService;

    // 관심 상품 등록
    @PostMapping("/products")
    public ProductResponseDto createProduct(@RequestBody ProductRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return productService.createProduct(requestDto, userDetails.getUser());
    }

    // 희망 최저가 설정
    @PutMapping("/products/{id}")
    public ProductResponseDto updateProduct(@PathVariable Long id,
                                            @RequestBody ProductMyPriceRequestDto requestDto) {
        return productService.updateProduct(id, requestDto);
    }

    // 관심 상품 리스트 조회
    @GetMapping("/products")
    public Page<ProductResponseDto> getProducts(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                // page: 클라이언트에서는 1, 서버에서는 0부터 시작
                                                @RequestParam(name = "page") int page,
                                                @RequestParam(name = "size") int size,
                                                @RequestParam(name = "sortBy") String sortBy,
                                                @RequestParam(name = "isAsc") boolean isAsc) {
        return productService.getProducts(userDetails.getUser(),
                                          page - 1,
                                          size,
                                          sortBy,
                                          isAsc);
    }

    // 관심 상품에 폴더를 추가하는 기능
    @PostMapping("/products/{productId}/folder")
    public void addFolder(@PathVariable Long productId,
                          @RequestParam(required = false) Long folderId,
                          @AuthenticationPrincipal UserDetailsImpl userDetails) {
        productService.addFolder(productId, folderId, userDetails.getUser());
    }

    @GetMapping("/folders/{folderId}/products")
    public Page<ProductResponseDto> getProductsInFolders(@PathVariable Long folderId,
                                                         @AuthenticationPrincipal UserDetailsImpl userDetails,
                                                         // page: 클라이언트에서는 1, 서버에서는 0부터 시작
                                                         @RequestParam(name = "page") int page,
                                                         @RequestParam(name = "size") int size,
                                                         @RequestParam(name = "sortBy") String sortBy,
                                                         @RequestParam(name = "isAsc") boolean isAsc) {
        return productService.getProductsInFolder(folderId,
                                                  page - 1,
                                                  size,
                                                  sortBy,
                                                  isAsc,
                                                  userDetails.getUser());
    }


}
