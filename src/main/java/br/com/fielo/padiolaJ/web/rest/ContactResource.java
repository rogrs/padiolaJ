package br.com.fielo.padiolaJ.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

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

import br.com.fielo.padiolaJ.domain.Contact;
import br.com.fielo.padiolaJ.repository.ContactRepository;
import br.com.fielo.padiolaJ.web.rest.errors.BadRequestAlertException;
import br.com.fielo.padiolaJ.web.rest.utils.HeaderUtil;
import br.com.fielo.padiolaJ.web.rest.utils.PaginationUtil;

@RestController
@RequestMapping("/api")
public class ContactResource {

	private final Logger log = LoggerFactory.getLogger(ContactResource.class);

	private static final String ENTITY_NAME = "contact";

	private final ContactRepository repository;

	public ContactResource(ContactRepository repository) {
		this.repository = repository;
	}

	@PostMapping("/contacts")
	public ResponseEntity<Contact> create(@RequestBody Contact obj) throws URISyntaxException {
		log.debug("REST request to save {}: {}", ENTITY_NAME, obj);
		if (obj.getId() != null) {
			throw new BadRequestAlertException("A new  cannot already have an ID", ENTITY_NAME, "idexists");
		}
		Contact result = repository.save(obj);
		return ResponseEntity.created(new URI("/api/contacts/" + result.getId()))
				.headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString())).body(result);
	}

	@PutMapping("/contacts")
	public ResponseEntity<Contact> update(@RequestBody Contact obj) throws URISyntaxException {
		log.debug("REST request to update {}: {}", ENTITY_NAME, obj);
		if (obj.getId() == null) {
			throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
		}
		Contact result = repository.save(obj);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, obj.getId().toString()))
				.body(result);
	}

	@GetMapping("/contacts")
	public ResponseEntity<List<Contact>> getAll(Pageable pageable) {
		log.debug("REST request to get a page of {}", ENTITY_NAME);
		Page<Contact> page = repository.findAll(pageable);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/contacts");
		return ResponseEntity.ok().headers(headers).body(page.getContent());
	}

	@GetMapping("/contacts/{id}")
	public ResponseEntity<?> get(@PathVariable Long id) {
		log.debug("REST request to get {}: {}", ENTITY_NAME, id);
		Optional<Contact> obj = repository.findById(id);
		return new ResponseEntity<>(obj, HttpStatus.OK);
	}

	@DeleteMapping("/contacts/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		log.debug("REST request to delete {}: {}", ENTITY_NAME, id);

		repository.deleteById(id);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
	}

}
