package com.pds1.ProjetoReferenciaPDS1.resourse;

import java.net.URI;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.pds1.ProjetoReferenciaPDS1.dto.CategoryDTO;
import com.pds1.ProjetoReferenciaPDS1.dto.ProductCategoriesDTO;
import com.pds1.ProjetoReferenciaPDS1.dto.ProductDTO;
import com.pds1.ProjetoReferenciaPDS1.services.ProductService;

@RestController
@RequestMapping(value = "/products")
public class ProductResourse {
	
	@Autowired
	private ProductService service;
	
	@GetMapping
	public ResponseEntity<Page<ProductDTO>> findAllPaged(
			@RequestParam(value = "name",defaultValue = "") String name,
			@RequestParam(value = "categories",defaultValue = "") String categories,
			@RequestParam(value = "page",defaultValue = "0") Integer page,
			@RequestParam(value = "linesPerPage",defaultValue = "12") Integer linesPerPage,
			@RequestParam(value = "orderBy",defaultValue = "name") String orderBy,
			@RequestParam(value = "direction",defaultValue = "ASC") String direction){
		
		PageRequest pageRequest = PageRequest.of(page,linesPerPage,Direction.valueOf(direction),orderBy);
		Page<ProductDTO> list = service.findByNameCategoryPaged(name, categories, pageRequest);				
		return ResponseEntity.ok().body(list);
	}
	
	@GetMapping(value = "category/{categoryId}")
	public ResponseEntity<Page<ProductDTO>> findByCategoryPaged(
			@PathVariable Long categoryId,
			@RequestParam(value = "page",defaultValue = "0") Integer page,
			@RequestParam(value = "linesPerPage",defaultValue = "12") Integer linesPerPage,
			@RequestParam(value = "orderBy",defaultValue = "name") String orderBy,
			@RequestParam(value = "direction",defaultValue = "ASC") String direction){
		
		PageRequest pageRequest = PageRequest.of(page,linesPerPage,Direction.valueOf(direction),orderBy);
		Page<ProductDTO> list = service.findByCategoryPaged(categoryId, pageRequest);				
		return ResponseEntity.ok().body(list);
	}
	
	@GetMapping(value = "/{id}")
	public ResponseEntity<ProductDTO> findById(@PathVariable Long id){
		ProductDTO dto = service.findById(id);
		return ResponseEntity.ok().body(dto);
	}
	
	@PostMapping
	@PreAuthorize("hasAnyRole('ADMIN')")
	public ResponseEntity<ProductDTO> insert(@Valid @RequestBody ProductCategoriesDTO dto){
		ProductDTO newDTO = service.insert(dto);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(newDTO.getId()).toUri();
		return ResponseEntity.created(uri).body(newDTO);
	}
	
	@PutMapping(value = "/{id}")
	@PreAuthorize("hasAnyRole('ADMIN')")
	public ResponseEntity<ProductDTO> update(@PathVariable Long id,@RequestBody ProductCategoriesDTO dto){
		ProductDTO newDTO = service.update(id, dto);
		return ResponseEntity.ok().body(newDTO);
	}
	
	@DeleteMapping(value = "/{id}")
	@PreAuthorize("hasAnyRole('ADMIN')")
	public ResponseEntity<Void> delete(@PathVariable Long id){
		service.delete(id);
		return ResponseEntity.noContent().build();
	}
	
	@PutMapping(value = "/{id}/addcategory")
	@PreAuthorize("hasAnyRole('ADMIN')")
	public ResponseEntity<Void> addCategory(@PathVariable Long id,@RequestBody CategoryDTO dto){
		service.addCategory(id, dto);
		return ResponseEntity.noContent().build();
	}
	
	@PutMapping(value = "/{id}/removecategory")
	@PreAuthorize("hasAnyRole('ADMIN')")
	public ResponseEntity<Void> removeCategory(@PathVariable Long id,@RequestBody CategoryDTO dto){
		service.removeCategory(id, dto);
		return ResponseEntity.noContent().build();
	}
	
	@PutMapping(value = "/{id}/setcategories")
	@PreAuthorize("hasAnyRole('ADMIN')")
	public ResponseEntity<Void> setCategories(@PathVariable Long id,@RequestBody List<CategoryDTO> dto){
		service.setCategories(id, dto);
		return ResponseEntity.noContent().build();
	}
}
