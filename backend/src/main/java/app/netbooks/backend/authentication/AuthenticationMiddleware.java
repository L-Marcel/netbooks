package app.netbooks.backend.authentication;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import app.netbooks.backend.models.Role;
import app.netbooks.backend.models.User;
import app.netbooks.backend.services.RolesService;
import app.netbooks.backend.services.TokensService;
import app.netbooks.backend.services.UsersService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class AuthenticationMiddleware extends OncePerRequestFilter  {
    @Autowired
    private TokensService tokensService;

    @Autowired
    private UsersService usersService;

    @Autowired
    private RolesService rolesService;

    @Override
    @SuppressWarnings("null")
    protected void doFilterInternal(
        HttpServletRequest request, 
        HttpServletResponse response, 
        FilterChain filterChain
    ) throws ServletException, IOException {
        Optional<String> token = this.getToken(request);
        
        if(token.isPresent()) {
            UUID uuid = tokensService.validate(token.get())
                .orElseThrow(
                    () -> new BadCredentialsException("Acesso negado!")
                );

            try {
                User user = usersService.findById(uuid);
                List<Role> roles = rolesService.findAllByUser(uuid);

                AuthenticatedUser authenticatedUser = new AuthenticatedUser(
                    user,
                    roles
                );

                UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                        authenticatedUser, 
                        null, 
                        authenticatedUser.getAuthorities()
                    );

                SecurityContextHolder.getContext().setAuthentication(
                    authentication
                );
            } catch (Exception e) {
                throw new BadCredentialsException("Acesso negado!");
            };
        };
        
        filterChain.doFilter(request, response);
    };

    private Optional<String> getToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if(header != null && header.startsWith("Bearer ")) 
            return Optional.of(header.substring(7));
        else return Optional.empty();
    };
};
