import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/")
public class Controller {

    private final Service service;

    public Controller(Service service) {
        this.service = service;
    }

    @GetMapping("/")
    public String home() {
        return "Hospital Queue API Running ✅";
    }

    // Create token
    @PostMapping("/token")
    public Model.Token create(@RequestParam String name,
                              @RequestParam String dept) {
        return service.createToken(name, dept);
    }

    // Get token
    @GetMapping("/token/{id}")
    public Model.Token get(@PathVariable Long id) {
        return service.getToken(id);
    }

    // Queue status
    @GetMapping("/status/{id}")
    public Map<String, Object> status(@PathVariable Long id) {

        Map<String, Object> res = new HashMap<>();
        res.put("position", service.getPosition(id));
        res.put("waitTime", service.getWaitTime(id));
        res.put("status", service.getToken(id).status);

        return res;
    }

    // Staff next
    @PostMapping("/next")
    public Model.Token next(@RequestParam String dept) {
        return service.serveNext(dept);
    }

    // Complete
    @PostMapping("/complete/{id}")
    public String complete(@PathVariable Long id) {
        service.complete(id);
        return "Done";
    }
}