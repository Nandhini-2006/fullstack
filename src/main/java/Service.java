import org.springframework.stereotype.Component;
import java.util.*;

@Component
public class Service {

    private final Repository repo;

    public Service(Repository repo) {
        this.repo = repo;
    }

    public Model.Token createToken(String name, String dept) {
        return repo.save(name, dept);
    }

    public Model.Token getToken(Long id) {
        return repo.findById(id);
    }

    public int getPosition(Long id) {
        Model.Token t = repo.findById(id);
        List<Model.Token> queue = repo.getQueue(t.department);

        for (int i = 0; i < queue.size(); i++) {
            if (queue.get(i).id.equals(id)) return i + 1;
        }
        return -1;
    }

    public int getWaitTime(Long id) {
        return getPosition(id) * 10;
    }

    public Model.Token serveNext(String dept) {
        List<Model.Token> queue = repo.getQueue(dept);

        if (queue.isEmpty()) return null;

        Model.Token next = queue.get(0);
        repo.updateStatus(next.id, "SERVING");

        return next;
    }

    public void complete(Long id) {
        repo.updateStatus(id, "DONE");
    }
}