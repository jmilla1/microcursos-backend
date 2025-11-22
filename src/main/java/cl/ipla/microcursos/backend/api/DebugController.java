package cl.ipla.microcursos.backend.api;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/debug")
public class DebugController {

    @GetMapping("/me")
    public Map<String, Object> me(Authentication authentication) {
        if (authentication == null) {
            return Map.of(
                    "authenticated", false
            );
        }

        return Map.of(
                "authenticated", authentication.isAuthenticated(),
                "principal", authentication.getName(),
                "authorities", authentication.getAuthorities()
        );
    }
}
