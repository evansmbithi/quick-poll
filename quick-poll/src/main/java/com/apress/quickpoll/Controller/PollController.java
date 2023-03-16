package com.apress.quickpoll.Controller;

import java.net.URI;
import java.util.Optional;

import javax.inject.Inject;
import javax.validation.Valid;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.apress.quickpoll.customException.ResourceNotFoundException;
import com.apress.quickpoll.domain.Poll;
import com.apress.quickpoll.repository.PollRepository;

@RestController
public class PollController {
    /*
     * we use the @Inject annotation to inject an instance of 
     * PollRepository into our controller. 
     */
    @Inject
    private PollRepository pollRepository;

    @GetMapping("/polls")
    public ResponseEntity<Iterable<Poll>> getAllPolls(){
        Iterable<Poll> allPolls = pollRepository.findAll();
        return new ResponseEntity<>(allPolls, HttpStatus.OK);
    }

    /*
     *  The @RequestBody annotation tells Spring 
     * that the entire request body needs to be converted
     * to an instance of Poll.
     * The @Valid annotation instructs Spring to perform data validation 
     * after binding the user-submitted data. Spring delegates the actual
     * validation to a registered Validator
     */
    @PostMapping("/polls")
    public ResponseEntity<?> createPoll (@Valid @RequestBody Poll poll){
        poll = pollRepository.save(poll);

        HttpHeaders responseHeaders = new HttpHeaders();
        URI newPollUri = ServletUriComponentsBuilder
                            .fromCurrentRequest()
                            .path("/{id}")
                            .buildAndExpand(poll.getId())
                            .toUri();
        
        responseHeaders.setLocation(newPollUri);
        return new ResponseEntity<>(null, responseHeaders, HttpStatus.CREATED);
    }

    protected Poll verifyPoll(Long pollId) throws ResourceNotFoundException {
        Optional<Poll> poll = pollRepository.findById(pollId);
        if(!poll.isPresent()){
            /* 
             * poll ID validation
             * Instead of throw new Exception("Poll not found"); 
             * when validating pollId for nonexistant polls
             * Throw a custom exception ResourceNotFoundException
             */
            throw new ResourceNotFoundException("Poll with id " + pollId + " not found");
        }
        return poll.get();
    }

    // individual poll
    @GetMapping("/polls/{pollId}")
    public ResponseEntity <?> getPoll(@PathVariable Long pollId) {
        // poll ID validation
        return new ResponseEntity<>(verifyPoll(pollId), HttpStatus.OK);                
    }

    @PutMapping("/polls/{pollId}")
    public ResponseEntity<?> updatePoll(@RequestBody Poll poll, @PathVariable Long pollId){
        /*
            // set id
            poll.setId(pollId);
            // save the entity
            Poll newPoll = pollRepository.save(poll);
            return new ResponseEntity<>(newPoll,HttpStatus.OK);
        */
        verifyPoll(pollId);
        pollRepository.save(poll);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/polls/{pollId}")
    public ResponseEntity<?> deletePoll(@PathVariable Long pollId) {
        pollRepository.deleteById(pollId);
        // pollRepository.delete(pollId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
