package com.apress.Controller;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.apress.domain.Vote;
import com.apress.dto.OptionCount;
import com.apress.dto.VoteResult;
import com.apress.repository.VoteRepository;

@RestController
public class ComputeResultController {
    @Inject
    private VoteRepository voteRepository;

    @GetMapping("/computeresult")
    public ResponseEntity<?> computeResult(@RequestParam Long pollId){
        VoteResult voteResult = new VoteResult();
        Iterable<Vote> allVotes = voteRepository.findByPoll(pollId);

        // algorithm to count votes
        int totalVotes = 0;
        Map<Long, OptionCount> tempMap = new HashMap<Long, OptionCount>();
        for(Vote v : allVotes){
            totalVotes++;

            // get OptionCount corresponding to this Option
            OptionCount optionCount = tempMap.get(v.getOption().getId());
            if(optionCount == null){
                optionCount = new OptionCount();
                optionCount.setOptionId(v.getOption().getId());
                tempMap.put(v.getOption().getId(), optionCount);
            }
            optionCount.setCount(optionCount.getCount()+1);
        }
        voteResult.setTotalVotes(totalVotes);
        voteResult.setResults(tempMap.values());

        return new ResponseEntity<VoteResult>(voteResult, HttpStatus.OK);
    }
    
}
