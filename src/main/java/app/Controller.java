package app;

import app.first.Admin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
public class Controller {

    @Autowired
    private AdminRepo adminRepo;

    @GetMapping("/admins")
    public List<Admin> getAll(){
        return adminRepo.findAll();
    }

    @GetMapping("/admins/{id}")
    public Optional<Admin> getOne(@PathVariable long id) {
        return adminRepo.findById(id);
    }

    @PostMapping("/admins")
    public Admin createAdmin(@Valid @RequestBody Admin admin){
        return adminRepo.save(admin);
    }

    @DeleteMapping("/admins/delete/{id}")
    public void deleteAdmin(@PathVariable long id){
        adminRepo.deleteById(id);
    }
}
