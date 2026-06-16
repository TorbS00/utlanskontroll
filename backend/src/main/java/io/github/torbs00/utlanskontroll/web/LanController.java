package io.github.torbs00.utlanskontroll.web;

import io.github.torbs00.utlanskontroll.domain.Lan;
import io.github.torbs00.utlanskontroll.service.KvoteService;
import io.github.torbs00.utlanskontroll.service.Kvoteresultat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class LanController {

    private final KvoteService kvoteService;

    public LanController(KvoteService kvoteService) {
        this.kvoteService = kvoteService;
    }

    @GetMapping("/lan")
    public List<Lan> alleLan() {
        return kvoteService.finnAlleLan();
    }

    @GetMapping("/kvote")
    public List<Kvoteresultat> kvoter() {
        return kvoteService.beregnKvoter();
    }
}