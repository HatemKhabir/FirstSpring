package org.mik.first.spring.controller.rest;

import jakarta.annotation.PostConstruct;
import org.mik.first.spring.Const;
import org.mik.first.spring.domain.Person;
import org.mik.first.spring.service.CountryService;
import org.mik.first.spring.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping(Const.REST_API_URL+"/person")
public class PersonRestController {
    private final PersonService personService;

    private  final CountryService  countryService;

    @Autowired
    public PersonRestController (PersonService personService,CountryService countryService){
        this.countryService=countryService;
        this.personService=personService;
    }
    @GetMapping(value = "/all",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Person>> getAll(){
        List<Person> result=this.personService.getAll();
    return  ResponseEntity.ok(result);
    }
    @GetMapping(value = "/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Person> GetById(@PathVariable(required = true) Long id){
return  ResponseEntity.ok(this.personService.findById(id).orElseThrow(()->new ResourceNotFoundException(String.format("Cannot find the person with id:%id",id))));
    }
    @PostMapping(value = "/add",consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Person> add(@RequestBody  Person p,@PathVariable (required = false)Long countryId) {
        if (p.getId() != null)
            throw new ResourceAlreadyExistException("Cannot insert person with  id");
        if (p.getCountry() == null) {
            if (countryId == null)
                throw new ResourceNotFoundException("no country definned with the id");
            p.setCountry(this.countryService.findById(countryId).orElseThrow(
                    () -> new ResourceNotFoundException(String.format("Cannot Find a country with this id %s", countryId))
            ));
        }
    return ResponseEntity.ok(this.personService.save(p));
    }

    @PutMapping(value = "/update",consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Person> update(@RequestBody Person  p) {
        Person orig =this.personService.findById(p.getId()).orElseThrow(
                ()->new ResourceNotFoundException(String.format("Cannot find person with id : %d",p.getId()))
        );
        p.setVersion(orig.getVersion());
        if(p.getCountry().getId()==orig.getCountry().getId()){
            p.setCountry(orig.getCountry());
        }else {
            p.setCountry(this.countryService.findById(p.getId()).orElseThrow(
                    ()->new ResourceNotFoundException(String.format("cannot find country with id: %d",p.getCountry().getId()))
            ));
        }
        return ResponseEntity.ok(personService.save(p));
    }

    @DeleteMapping(value = "/delete")
    public ResponseEntity<Void> delete(@PathVariable(value = "id",required = true)Long id){
        Person p=this.personService.findById(id).orElseThrow(
                ()->new ResourceNotFoundException((String.format("Cannot find person with id: %id",id))
        ));
        this.personService.delete(id);
        return ResponseEntity.ok().build();
    }
}
