package com.apress.quickpoll.Controller;

import java.net.URI;
import java.util.Optional;

import javax.inject.Inject;

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
     */
    @PostMapping("/polls")
    public ResponseEntity<?> createPoll (@RequestBody Poll poll){
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

    // individual poll
    @GetMapping("/polls/{pollId}")
    public ResponseEntity <?> getPoll(@PathVariable Long pollId) throws Exception{
        Optional<Poll> poll = pollRepository.findById(pollId);
        if(!poll.isPresent()){
            throw new Exception("Poll not found");
        }
        return new ResponseEntity<>(poll.get(), HttpStatus.OK);                
    }

    @PutMapping("/polls/{pollId}")
    public ResponseEntity<?> updatePoll(@RequestBody Poll poll, @PathVariable Long pollId){
        // set id
        poll.setId(pollId);
        // save the entity
        Poll newPoll = pollRepository.save(poll);
        return new ResponseEntity<>(newPoll,HttpStatus.OK);
    }

    @DeleteMapping("/polls/{pollId}")
    public ResponseEntity<?> deletePoll(@PathVariable Long pollId) {
        pollRepository.deleteById(pollId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
