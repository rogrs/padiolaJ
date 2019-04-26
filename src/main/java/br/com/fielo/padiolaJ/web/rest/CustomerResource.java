package br.com.fielo.padiolaJ.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.fielo.padiolaJ.domain.Customer;
import br.com.fielo.padiolaJ.repository.CustomerRepository;
import br.com.fielo.padiolaJ.web.rest.errors.BadRequestAlertException;
import br.com.fielo.padiolaJ.web.rest.utils.HeaderUtil;
import br.com.fielo.padiolaJ.web.rest.utils.PaginationUtil;

@RestController
@RequestMapping("/api")
public class CustomerResource {

	private final Logger log = LoggerFactory.getLogger(CustomerResource.class);

	private static final String ENTITY_NAME = "customer";

	private final CustomerRepository repository;

	public CustomerResource(CustomerRepository repository) {
		this.repository = repository;
	}

	@PostMapping("/customers")
	public ResponseEntity<Customer> create(@RequestBody Customer obj) throws URISyntaxException {
		log.debug("REST request to save {}: {}", ENTITY_NAME, obj);
		if (obj.getId() != null) {
			throw new BadRequestAlertException("A new  cannot already have an ID", ENTITY_NAME, "idexists");
		}
		Customer result = repository.save(obj);
		return ResponseEntity.created(new URI("/api/cargos/" + result.getId()))
				.headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString())).body(result);
	}

	@PutMapping("/customers")
	public ResponseEntity<Customer> update(@RequestBody Customer obj) throws URISyntaxException {
		log.debug("REST request to update {}: {}", ENTITY_NAME, obj);
		if (obj.getId() == null) {
			throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
		}
		Customer result = repository.save(obj);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, obj.getId().toString()))
				.body(result);
	}

	@GetMapping("/customers")
	public ResponseEntity<List<Customer>> getAll(Pageable pageable) {
		log.debug("REST request to get a page of {}", ENTITY_NAME);
		Page<Customer> page = repository.findAll(pageable);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/customers");
		return ResponseEntity.ok().headers(headers).body(page.getContent());
	}

	@GetMapping("/customers/{id}")
	public ResponseEntity<?> get(@PathVariable Long id) {
		log.debug("REST request to get {}: {}", ENTITY_NAME, id);
		Customer obj = repository.findOne(id);
		return new ResponseEntity<>(obj, HttpStatus.OK);
	}

	@DeleteMapping("/customers/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		log.debug("REST request to delete {}: {}", ENTITY_NAME, id);

		repository.delete(id);

		return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
	}

}
