package com.tejasvi.ecom_web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tejasvi.ecom_web.dto.AddProductRequest;

import com.tejasvi.ecom_web.dto.ProductResponse;
import com.tejasvi.ecom_web.dto.UpdateProductRequest;
import com.tejasvi.ecom_web.model.Product;
import com.tejasvi.ecom_web.service.ProductService;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:5173/")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService){
        this.productService = productService;
    }
    
    
    @PreAuthorize("hasAuthority('Admin')")
    @PostMapping(value = "/admin/product", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ProductResponse> addProduct(
            @RequestParam("product") String productJson,
            @RequestPart("imageFile") MultipartFile imageFile) throws IOException {

        ObjectMapper mapper = new ObjectMapper();
        AddProductRequest request = mapper.readValue(productJson, AddProductRequest.class);

        return ResponseEntity.ok(productService.addProduct(request, imageFile));
    }
    
    

    @PreAuthorize("hasAuthority('Admin')")
    @PutMapping(value = "/admin/product/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ProductResponse> updateProduct(
            @PathVariable Long id,
            @RequestParam("product") String request,
            @RequestPart(value = "imageFile", required = false) MultipartFile imageFile) throws IOException {
    		
    	ObjectMapper mapper = new ObjectMapper();
    	UpdateProductRequest request1 = mapper.readValue(request, UpdateProductRequest.class);
    	
    	return ResponseEntity.ok(productService.updateProduct(id, request1, imageFile));
    }
    
    
    
    @PreAuthorize("hasAuthority('Admin')")
    @DeleteMapping("/admin/product/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long id){
    	
    	return productService.deleteProduct(id);
 
    }
    
    
    @PreAuthorize("hasAnyAuthority('Admin', 'Customer')")
    @GetMapping("/products")
    public List<ProductResponse> getAllProducts(){
    	System.out.println("Entered into getAllProducts");
        return productService.getAllProducts();
    }

    @PreAuthorize("hasAnyAuthority('Admin', 'Customer')")
    @GetMapping("/product/{id}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable Long id){
        ProductResponse product = productService.getProductById(id);
        if(product == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(product);
    }
    
    
    @PreAuthorize("hasAnyAuthority('Admin', 'Customer')")
    @GetMapping("/products/category/{id}")
    public ResponseEntity<List<ProductResponse>> getProductByCategory(@PathVariable Long id){
    	
    		List<ProductResponse> product = productService.getProductByCategory(id);
    		return ResponseEntity.ok(product);
    	
    }
    
    
    @PreAuthorize("hasAnyAuthority('Admin', 'Customer')")
    @GetMapping("/product/{productId}/image")
    public ResponseEntity<byte[]> getImageById(@PathVariable Long productId){
    	
    	return productService.getImageById(productId);
    	
    }
    
    
    
    @GetMapping("/products/search")
    public ResponseEntity<List<Product>> searchProducts(@RequestParam String keyword){
    	List<Product> product = productService.searchProduct(keyword);
    	return new ResponseEntity<>(product, HttpStatus.OK);
    }


}
