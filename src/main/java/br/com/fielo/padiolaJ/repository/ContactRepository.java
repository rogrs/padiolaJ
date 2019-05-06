package br.com.fielo.padiolaJ.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.fielo.padiolaJ.domain.Contact;
import br.com.fielo.padiolaJ.domain.Customer;

@SuppressWarnings("unused")
@Repository
public interface ContactRepository extends JpaRepository<Contact, Long> {



}
