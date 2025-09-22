package com.tejasvi.ecom_web.service;

import com.tejasvi.ecom_web.dto.AddProductRequest;

import com.tejasvi.ecom_web.dto.ProductResponse;
import com.tejasvi.ecom_web.dto.UpdateProductRequest;
import com.tejasvi.ecom_web.model.Category;
import com.tejasvi.ecom_web.model.Product;
import com.tejasvi.ecom_web.repository.CategoryRepository;
import com.tejasvi.ecom_web.repository.ProductRepository;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class ProductService {


	private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    public ProductResponse addProduct(AddProductRequest dto, MultipartFile imageFile) throws IOException {
        Product product = new Product();
        mapAddRequest(dto, product, imageFile);
        Product saved = productRepository.save(product);
        return mapToResponse(saved);
    }

    public ProductResponse updateProduct(Long id, UpdateProductRequest dto, MultipartFile imageFile) throws IOException {
        Product product = productRepository.findById(id).orElseThrow();
        mapUpdateRequest(dto, product, imageFile);
        Product updated = productRepository.save(product);
        return mapToResponse(updated);
    }

    private void mapAddRequest(AddProductRequest dto, Product product, MultipartFile imageFile) throws IOException {
        product.setName(dto.getName());
        product.setBrand(dto.getBrand());
        product.setPrice(dto.getPrice());
        product.setDescription(dto.getDescription());
        product.setProductAvailable(dto.isProductAvailable());
        product.setStockQuantity(dto.getStockQuantity());
        product.setReleaseDate(dto.getReleaseDate());

        Category category = categoryRepository.findById(dto.getCategoryId()).orElseThrow();
        product.setCategory(category);

        if (imageFile != null) {
            product.setImageName(imageFile.getOriginalFilename());
            product.setImageType(imageFile.getContentType());
            product.setImageData(imageFile.getBytes());
        }
    }

    private void mapUpdateRequest(UpdateProductRequest dto, Product product, MultipartFile imageFile) throws IOException {
        if (dto.getName() != null) product.setName(dto.getName());
        if (dto.getBrand() != null) product.setBrand(dto.getBrand());
        if (dto.getPrice() != null) product.setPrice(dto.getPrice());
        if (dto.getDescription() != null) product.setDescription(dto.getDescription());
        if (dto.getProductAvailable() != null) product.setProductAvailable(dto.getProductAvailable());
        if (dto.getStockQuantity() != 0) product.setStockQuantity(dto.getStockQuantity());
        if (dto.getReleaseDate() != null) product.setReleaseDate(dto.getReleaseDate());

        if (dto.getCategoryId() != 0) {
            Category category = categoryRepository.findById(dto.getCategoryId()).orElseThrow();
            product.setCategory(category);
        }

        if (imageFile != null) {
            product.setImageName(imageFile.getOriginalFilename());
            product.setImageType(imageFile.getContentType());
            product.setImageData(imageFile.getBytes());
        }
    }

    private ProductResponse mapToResponse(Product product) {
        ProductResponse response = new ProductResponse();
        response.setId(product.getId());
        response.setName(product.getName());
        response.setBrand(product.getBrand());
        response.setPrice(product.getPrice());
        response.setDescription(product.getDescription());
        response.setProductAvailable(product.isProductAvailable());
        response.setStockQuantity(product.getStockQuantity());
        response.setReleaseDate(product.getReleaseDate());
        response.setCategoryName(product.getCategory().getName());

        // Instead of sending bytes → expose URL
        response.setImageUrl("/api/product/" + product.getId() + "/image");
        return response;
    }
    
   

	public ResponseEntity<String> deleteProduct(Long id) {
		
		Product product = productRepository.findById(id).orElse(null);
    	if(product != null) {
    		productRepository.delete(product);
    		return new ResponseEntity<>("Deleted", HttpStatus.OK);
    	}else {
    		return new ResponseEntity<>("Product not found", HttpStatus.NOT_FOUND);
    	}
	}

	public List<Product> searchProduct(String keyword) {
		
		return productRepository.searchProduct(keyword);
	}

	public List<ProductResponse> getAllProducts() {
		List<Product> product = productRepository.findAll();
		List<ProductResponse> productResponse = product.stream().map(p -> mapToResponse(p)).toList();
		return productResponse;
	}

	public ProductResponse getProductById(Long id) {
		Product product = productRepository.findById(id).get();
		ProductResponse productResponse = mapToResponse(product);
		return productResponse;
	}
	
	public ResponseEntity<byte[]> getImageById(Long id){
		Product product = productRepository.findById(id).orElseThrow();
		return ResponseEntity.ok().contentType(MediaType.valueOf(product.getImageType())).body(product.getImageData());
		
	}

	public List<ProductResponse> getProductByCategory(Long id) {
		return productRepository.findByCategoryId(id).stream().map(p -> mapToResponse(p)).toList();
	}

	
}
