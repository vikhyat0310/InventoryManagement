package com.gl.inventorymanagement.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gl.inventorymanagement.entity.Product;
import com.gl.inventorymanagement.entity.Seller;
import com.gl.inventorymanagement.service.ProductService;
import com.gl.inventorymanagement.service.SellerService;

import jakarta.transaction.Transactional;

@RestController
@Transactional
public class ProductController {	
	@Autowired
	ProductService productService;
	@Autowired
	SellerService sellerService;
	
	
	@PreAuthorize("hasAuthority('ROLE_SELLER')")
	@PostMapping("/add/product/{id}")
	public ResponseEntity<String> register( @RequestBody Product product,@PathVariable int id) {
		
//		int i=product.getSeller().getSellerId();
//		System.out.println(i);
		Seller seller=sellerService.findSeller(id).get();
		product.setSeller(seller);
		productService.addProduct(product);
		return new ResponseEntity<String>("Product ",HttpStatus.CREATED);
	}
	
	@GetMapping("/get/product")
	public ResponseEntity<List<Product>> getAllProducts() {
		return new ResponseEntity<List<Product>>(productService.getAllProducts(),HttpStatus.OK);
		
	}
	
	@GetMapping("/get/productById/{id}")
	public ResponseEntity<Product> getAllProductsById(@PathVariable int id) {

		return new ResponseEntity<Product>(productService.findProductById(id).get(),HttpStatus.OK);
//		return productService.findProductById(id).get();
	}
	
	@PreAuthorize("hasAuthority('ROLE_SELLER')")
	@PutMapping("/update/product")
	public ResponseEntity<String> updateProduct( @RequestBody Product product) {
		Optional<Product> product2 = productService.findProductById(product.getId());
		Seller seller=product2.get().getSeller();
		product.setSeller(seller);
		if(product2.isPresent()) {
			productService.updateProduct(product);
			System.out.println("product updated");
			return new ResponseEntity<String>("Product Updated with Product Id :" + product2.get().getId(), HttpStatus.ACCEPTED);
		  
	}
		return new ResponseEntity<String>("Product Not Found" , HttpStatus.NO_CONTENT);
}
	@PreAuthorize("hasAuthority('ROLE_SELLER')")
	@PutMapping("/update/description")
	public ResponseEntity<String> updateDescription( @RequestParam int id,@RequestParam String description) {
		Optional<Product> product2 = productService.findProductById(id);
		
		if(product2.isPresent()) {
			productService.updateDescription(id, description);
			return new ResponseEntity<String>("Description Updated with Product Id :" + product2.get().getId(), HttpStatus.ACCEPTED);
		  
	}
		return new ResponseEntity<String>("Product Not Found" , HttpStatus.NO_CONTENT);
}
	
	@PutMapping("/update/quantity")
	public ResponseEntity<String> updateQuantity( @RequestParam int id,@RequestParam int quantity) {
		Optional<Product> product2 = productService.findProductById(id);	
		if(product2.isPresent()) {
			productService.updateQuantity(id, quantity);
			return new ResponseEntity<String>("quantity Updated with Product Id :" + product2.get().getId(), HttpStatus.ACCEPTED);
		  
	}
		return new ResponseEntity<String>("Product Not Found" , HttpStatus.NO_CONTENT);
}
	@PreAuthorize("hasAuthority('ROLE_SELLER')")
	@PutMapping("/update/price")
	public ResponseEntity<String> updatePrice( @RequestParam int id,@RequestParam int price) {
		Optional<Product> product2 = productService.findProductById(id);	
		if(product2.isPresent()) {
			productService.updatePrice(id, price);
			return new ResponseEntity<String>("price Updated with Product Id :" + product2.get().getId(), HttpStatus.ACCEPTED);
		  
	}
		return new ResponseEntity<String>("Product Not Found" , HttpStatus.NO_CONTENT);
}
	@PreAuthorize("hasAuthority('ROLE_SELLER')")
	@DeleteMapping("/delete/product")
	public ResponseEntity<String> deleteCustomer(@RequestParam int id) {
		Optional<Product> product2 = productService.findProductById(id);

		if (product2.isPresent()) {
			productService.deleteProduct(id);
			return new ResponseEntity<String>("Customer deleted with Customer Id :" + product2.get().getId(),
					HttpStatus.ACCEPTED);

		}
		return new ResponseEntity<String>("Customer Not Found", HttpStatus.NO_CONTENT);
	}
}
